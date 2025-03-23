package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

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

    private static SessionFactory sessionFactory;

    private static void getConnectionHibernate() {
        try {
            Configuration configuration = new Configuration();
            Properties properties = new Properties();

            properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            properties.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres");
            properties.put("hibernate.connection.username", "admin");
            properties.put("hibernate.connection.password", "admin");
            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("hibernate.show_sql", "true");
            properties.put("hibernate.format_sql", "true");

            configuration.setProperties(properties);
            configuration.addAnnotatedClass(User.class);

            sessionFactory = configuration.buildSessionFactory();
            System.out.println("Hibernate SessionFactory успешно создан!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Ошибка при инициализации Hibernate!" + e);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            System.out.println("ℹ️ SessionFactory инициализируется...");
            getConnectionHibernate();
        }
        if (sessionFactory == null) {
            throw new IllegalStateException("❌ SessionFactory не инициализирован!");
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("SessionFactory closed successfully.");
        }
    }
}