package org.laboratory;

import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtility {

    public static String generateID(Connection conn, String prefix, String tableName, String idColumn) throws SQLException {
        String year = String.valueOf(Year.now().getValue()); // Get the current year as a string
        String fullPrefix = prefix + year; // Combine the prefix and year to form the base of the ID
        // SQL to find the most recent ID that starts with this prefix (e.g., A2025)
        // Orders by ID descending so the latest ID appears first
        String sql = "SELECT " + idColumn + " FROM " + tableName + " WHERE " + idColumn + " LIKE ? ORDER BY " + idColumn + " DESC LIMIT 1";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fullPrefix + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String lastID = rs.getString(idColumn);
                    String numPart = lastID.substring(fullPrefix.length());
                    int num = Integer.parseInt(numPart);
                    return fullPrefix + String.format("%05d", num + 1);
                } else {
                    return fullPrefix + "00001";
                }
            }
        }
    }
    public static String generateSchoolEmail(String firstNameValue, String lastNameValue, String domain) {
        String firstName = firstNameValue.trim().toLowerCase().replaceAll("\\s+", "_");
        String lastName = lastNameValue.trim().toLowerCase();

        return firstName + "_" + lastName + "@" + domain;
    }
    public static List<Object[]> fetchHistoryLogs() {
        List<Object[]> logs = new ArrayList<>();
        // SQL query to select columns from history_logs table, ordered by newest first
        String sql = "SELECT logID, adminID, adminUsername, action, loginTimestamp " +
                "FROM history_logs ORDER BY loginTimestamp DESC";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            // Iterate over each row in the result set
            while (rs.next()) {
                Object[] row = new Object[] {
                        rs.getInt("logID"),
                        rs.getString("adminID"),
                        rs.getString("adminUsername"),
                        rs.getString("action"),
                        rs.getTimestamp("loginTimestamp")
                };
                logs.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }
}
