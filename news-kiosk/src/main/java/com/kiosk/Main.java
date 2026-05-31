package com.kiosk;

import com.kiosk.repository.PublicationRepository;
import com.kiosk.repository.jdbc.JdbcPublicationRepository;
import com.kiosk.service.PublicationService;
import com.kiosk.service.impl.PublicationServiceImpl;
import com.kiosk.ui.ConsoleUI;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        Properties props = new Properties();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("db.properties")) {
            props.load(is);
        }

        try (Connection connection = DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"))) {

            connection.createStatement().execute("SET client_encoding TO 'UTF8'");
            PublicationRepository repository = new JdbcPublicationRepository(connection);
            PublicationService service = new PublicationServiceImpl(repository);
            ConsoleUI ui = new ConsoleUI(service);
            ui.start();
        }
    }
}
