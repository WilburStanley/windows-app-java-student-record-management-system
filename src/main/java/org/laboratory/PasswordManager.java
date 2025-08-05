package org.laboratory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PasswordManager {
    private static final String PROPERTIES_PATH = "/config/password.properties";
    // Reads and returns the stored hashed password from the properties file
    public static String getStoredHashedPassword() {
        try (InputStream input = PasswordManager.class.getResourceAsStream(PROPERTIES_PATH)) {
            if (input == null) {
                throw new RuntimeException(PROPERTIES_PATH + " not found in classpath");
            }
            // Load properties from the input stream
            Properties prop = new Properties();
            prop.load(input);
            // Retrieve the 'hashedPassword' property value
            String hashed = prop.getProperty("hashedPassword");
            // Validate that the hashed password exists and is not empty
            if (hashed == null || hashed.isEmpty()) {
                throw new RuntimeException("hashedPassword not set in properties file");
            }
            // Return the hashed password string
            return hashed;
        } catch (IOException e) {
            throw new RuntimeException("Error loading " + PROPERTIES_PATH, e);
        }
    }
}