package org.laboratory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    // Static block runs once when the class is first loaded
    static {
        // Load the properties file from the resources folder
        try (InputStream input = DBHelper.class.getClassLoader().getResourceAsStream("config/db/db.properties")) {
            if (input == null) {
                throw new RuntimeException("Cannot find db.properties");
            }
            // Create a Properties object to read key-value pairs from the file
            Properties prop = new Properties();
            prop.load(input);
            // Read database connection details from the properties file
            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.user");
            PASSWORD = prop.getProperty("db.password");
            // Load and register the MySQL JDBC driver (ensures Java can connect to MySQL)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load DB config or driver.");
        }
    }
    // This method returns a Connection object to interact with the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}