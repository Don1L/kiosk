package com.kiosk.repository.jdbc;

import com.kiosk.model.Book;
import com.kiosk.model.Magazine;
import com.kiosk.model.Newspaper;
import com.kiosk.model.Publication;
import com.kiosk.repository.DataAccessException;
import com.kiosk.repository.PublicationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class JdbcPublicationRepository implements PublicationRepository {

    private static final String INSERT_SQL =
            "INSERT INTO publications (type, title, price, quantity, issue_number, pub_date, month_year, author, isbn) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID =
            "SELECT * FROM publications WHERE id = ?";

    private static final String SELECT_ALL =
            "SELECT * FROM publications";

    private static final String UPDATE_SQL =
            "UPDATE publications "
            + "SET type=?, title=?, price=?, quantity=?, issue_number=?, pub_date=?, month_year=?, author=?, isbn=? "
            + "WHERE id=?";

    private static final String DELETE_SQL =
            "DELETE FROM publications WHERE id=?";

    private final Connection connection;

    public JdbcPublicationRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long save(Publication publication) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(ps, publication);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    long id = keys.getLong(1);
                    publication.setId(id);
                    return id;
                }
                throw new DataAccessException("No generated key returned after insert", null);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to save publication", e);
        }
    }

    @Override
    public Optional<Publication> findById(long id) {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find publication by id: " + id, e);
        }
    }

    @Override
    public List<Publication> findAll() {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            List<Publication> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRow(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch all publications", e);
        }
    }

    @Override
    public void update(Publication publication) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {
            fillStatement(ps, publication);
            ps.setLong(10, publication.getId());
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new NoSuchElementException("Publication not found: " + publication.getId());
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update publication", e);
        }
    }

    @Override
    public void delete(long id) {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, id);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new NoSuchElementException("Publication not found: " + id);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete publication", e);
        }
    }

    private void fillStatement(PreparedStatement ps, Publication publication) throws SQLException {
        if (publication instanceof Newspaper n) {
            ps.setString(1, "NEWSPAPER");
            ps.setString(2, n.getTitle());
            ps.setDouble(3, n.getPrice());
            ps.setInt(4, n.getQuantity());
            ps.setInt(5, n.getIssueNumber());
            ps.setString(6, n.getDate());
            ps.setNull(7, Types.VARCHAR);
            ps.setNull(8, Types.VARCHAR);
            ps.setNull(9, Types.VARCHAR);
        } else if (publication instanceof Magazine m) {
            ps.setString(1, "MAGAZINE");
            ps.setString(2, m.getTitle());
            ps.setDouble(3, m.getPrice());
            ps.setInt(4, m.getQuantity());
            ps.setInt(5, m.getIssueNumber());
            ps.setNull(6, Types.VARCHAR);
            ps.setString(7, m.getMonthYear());
            ps.setNull(8, Types.VARCHAR);
            ps.setNull(9, Types.VARCHAR);
        } else if (publication instanceof Book b) {
            ps.setString(1, "BOOK");
            ps.setString(2, b.getTitle());
            ps.setDouble(3, b.getPrice());
            ps.setInt(4, b.getQuantity());
            ps.setNull(5, Types.INTEGER);
            ps.setNull(6, Types.VARCHAR);
            ps.setNull(7, Types.VARCHAR);
            ps.setString(8, b.getAuthor());
            ps.setString(9, b.getIsbn());
        }
    }

    private Publication mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String type = rs.getString("type");
        String title = rs.getString("title");
        double price = rs.getDouble("price");
        int quantity = rs.getInt("quantity");

        return switch (type) {
            case "NEWSPAPER" -> new Newspaper(id, title, price, quantity,
                    rs.getInt("issue_number"), rs.getString("pub_date"));
            case "MAGAZINE" -> new Magazine(id, title, price, quantity,
                    rs.getInt("issue_number"), rs.getString("month_year"));
            case "BOOK" -> new Book(id, title, price, quantity,
                    rs.getString("author"), rs.getString("isbn"));
            default -> throw new IllegalStateException("Unknown publication type: " + type);
        };
    }
}
