package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    public static Connection getConnection() {
        try {
            System.out.println("Attempting to connect to the database...");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}
