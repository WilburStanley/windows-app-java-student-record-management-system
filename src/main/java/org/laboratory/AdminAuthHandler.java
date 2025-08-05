package org.laboratory;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdminAuthHandler {
    public static void registerAdmin(JFrame currentFrame, JTextField usernameField, JTextField passwordField, JLabel usernameLabel, JLabel passwordLabel) {
        String username = usernameField.getText().trim();
        String plainPassword = passwordField.getText().trim();

        if ((username.isEmpty() || plainPassword.isEmpty())) {
            SwingUtility.showCustomMsgDialog(currentFrame, "MISSING CREDENTIALS", "Username and password must not be empty.", "OK", "warning_sign.png");
            clearFields(usernameField, passwordField);
            setTextFieldErrorState((SwingUtility.RoundedTextField) usernameField, usernameLabel);
            setPasswordFieldErrorState((SwingUtility.RoundedPasswordField) passwordField, passwordLabel);
            return;
        }
        // Encrypt the password for added security
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        try (Connection conn = DBHelper.getConnection()) {
            String newAdminID = DatabaseUtility.generateID(conn, "A", "admin_accounts", "adminID");

            String sql = "INSERT INTO admin_accounts (adminID, adminUsername, adminPassword) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newAdminID);
                stmt.setString(2, username);
                stmt.setString(3, hashedPassword);
                // Execute and check if insertion was successful
                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    SwingUtility.showCustomMsgDialog(currentFrame, "REGISTRATION SUCCESSFUL", "Admin account registered successfully! Your ID: " + newAdminID, "OK", "information_sign.png");
                    resetTextFieldState((SwingUtility.RoundedTextField) usernameField, usernameLabel);
                    resetPasswordFieldState((SwingUtility.RoundedPasswordField) passwordField, passwordLabel);
                } else {
                    SwingUtility.showCustomMsgDialog(currentFrame, "REGISTRATION FAILED", "Failed to register admin account.", "OK", "error_sign.png");
                    clearFields(usernameField, passwordField);
                    setTextFieldErrorState((SwingUtility.RoundedTextField) usernameField, usernameLabel);
                    setPasswordFieldErrorState((SwingUtility.RoundedPasswordField) passwordField, passwordLabel);
                }
            }

        } catch (SQLException e) {
            // Handle duplicate username error (error code 1062)
            if (e.getErrorCode() == 1062) {
                SwingUtility.showCustomMsgDialog(currentFrame, "USERNAME TAKEN", "This username is already registered. Please choose another.", "OK", "warning_sign.png");
                usernameField.setText("");
                usernameField.requestFocus();
                setTextFieldErrorState((SwingUtility.RoundedTextField) usernameField, usernameLabel);
                setPasswordFieldErrorState((SwingUtility.RoundedPasswordField) passwordField, passwordLabel);
            } else {
                // Show other SQL errors
                SwingUtility.showCustomMsgDialog(currentFrame, "ERROR", "Database error: " + e.getMessage(), "OK", "warning_sign.png");
                clearFields(usernameField, passwordField);
            }
            e.printStackTrace();
        }
    }
    public static void loginAdmin(JFrame appFrame, JTextField usernameField, JTextField passwordField, JLabel usernameLabel, JLabel passwordLabel) {
        String username = usernameField.getText().trim();
        String plainPassword = passwordField.getText().trim();

        if (username.isEmpty() || plainPassword.isEmpty()) {
            SwingUtility.showCustomMsgDialog(appFrame, "MISSING CREDENTIALS", "Username and password must not be empty.", "OK", "warning_sign.png");
            clearFields(usernameField, passwordField);
            setTextFieldErrorState((SwingUtility.RoundedTextField) usernameField, usernameLabel);
            setPasswordFieldErrorState((SwingUtility.RoundedPasswordField) passwordField, passwordLabel);
            return;
        }

        String sql = "SELECT adminID, adminPassword FROM admin_accounts WHERE adminUsername = ?";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String adminID = rs.getString("adminID");
                String hashedPassword = rs.getString("adminPassword");
                // Check if entered password matches the encrypted password
                if (BCrypt.checkpw(plainPassword, hashedPassword)) {
                    logAdminAction(conn, adminID, username, "LOGIN");
                    AdminSession.startSession(adminID, username);

                    SwingUtility.showCustomMsgDialog(appFrame, "LOGIN SUCCESSFUL", "Welcome, " + username + "! Let's get started.", "OK", "information_sign.png");
                    clearFields(usernameField, passwordField);
                    resetTextFieldState((SwingUtility.RoundedTextField) usernameField, usernameLabel);
                    resetPasswordFieldState((SwingUtility.RoundedPasswordField) passwordField, passwordLabel);

                    appFrame.dispose();
                    Main.launchDashboard();
                } else {
                    SwingUtility.showCustomMsgDialog(appFrame, "LOGIN FAILED", "Incorrect username or password!", "OK", "error_sign.png");
                    clearFields(usernameField, passwordField);
                    setTextFieldErrorState((SwingUtility.RoundedTextField) usernameField, usernameLabel);
                    setPasswordFieldErrorState((SwingUtility.RoundedPasswordField) passwordField, passwordLabel);
                }
            } else {
                SwingUtility.showCustomMsgDialog(appFrame, "LOGIN FAILED", "Username not found.", "OK", "error_sign.png");
                clearFields(usernameField, passwordField);
                setTextFieldErrorState((SwingUtility.RoundedTextField) usernameField, usernameLabel);
                setPasswordFieldErrorState((SwingUtility.RoundedPasswordField) passwordField, passwordLabel);
            }

        } catch (SQLException e) {
            SwingUtility.showCustomMsgDialog(appFrame, "ERROR", "Database error: " + e.getMessage(), "OK", "warning_sign.png");
            clearFields(usernameField, passwordField);
            setTextFieldErrorState((SwingUtility.RoundedTextField) usernameField, usernameLabel);
            setPasswordFieldErrorState((SwingUtility.RoundedPasswordField) passwordField, passwordLabel);
            e.printStackTrace();
        }
    }
    public static void setTextFieldErrorState(SwingUtility.RoundedTextField field, JLabel label) {
        field.setBorderColor(Color.RED);
        label.setForeground(Color.BLACK);
    }
    public static void setPasswordFieldErrorState(SwingUtility.RoundedPasswordField field, JLabel label) {
        field.setBorderColor(Color.RED);
        label.setForeground(Color.BLACK);
    }
    public static void resetTextFieldState(SwingUtility.RoundedTextField field, JLabel label) {
        field.setBorderColor(Color.BLACK);
        label.setForeground(Color.BLACK);
    }
    public static void resetPasswordFieldState(SwingUtility.RoundedPasswordField field, JLabel label) {
        field.setBorderColor(Color.BLACK);
        label.setForeground(Color.BLACK);
    }
    public static void logoutAdmin(String adminID, String adminUsername) {
        try (Connection conn = DBHelper.getConnection()) {
            logAdminAction(conn, adminID, adminUsername, "LOGOUT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void logAdminAction(Connection conn, String adminID, String adminUsername, String action) throws SQLException {
        String sql = "INSERT INTO history_logs (adminID, adminUsername, action) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, adminID);
            stmt.setString(2, adminUsername);
            stmt.setString(3, action);
            stmt.executeUpdate();
        }
    }
    private static void clearFields(JTextField usernameField, JTextField passwordField) {
        usernameField.setText("");
        passwordField.setText("");
    }
}