package com.kiosk.web;

import com.kiosk.repository.PublicationRepository;
import com.kiosk.repository.jdbc.JdbcPublicationRepository;
import com.kiosk.service.PublicationService;
import com.kiosk.service.impl.PublicationServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class AppContextListener implements ServletContextListener {

    private Connection connection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.postgresql.Driver");
            Properties props = new Properties();
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties")) {
                props.load(is);
            }
            connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            );
            try (java.sql.Statement st = connection.createStatement()) {
                st.execute("SET client_encoding TO 'UTF8'");
            }
            PublicationRepository repository = new JdbcPublicationRepository(connection);
            PublicationService service = new PublicationServiceImpl(repository);
            sce.getServletContext().setAttribute("service", service);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to initialize application", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ignored) {}
    }
}
