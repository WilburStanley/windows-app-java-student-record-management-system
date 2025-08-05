package org.laboratory;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentInformationHandler {

    public static class StudentRecord {
        public String studentID;
        public String firstName;
        public String lastName;
        public String middleName;
        public int age;
        public String gender;
        public String course;
        public int yearLevel;
        public String email;

        public StudentRecord(String studentID, String firstName, String lastName, String middleName, int age, String gender, String course, int yearLevel, String email) {
            this.studentID = studentID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.middleName = middleName;
            this.age = age;
            this.gender = gender;
            this.course = course;
            this.yearLevel = yearLevel;
            this.email = email;
        }

        public Object[] toTableRow() {
            return new Object[] { studentID, lastName, firstName, middleName, age, gender, course, yearLevel, email };
        }
    }
    public static StudentRecord addStudent(JFrame appGraphics, String firstName, String lastName, String middleName, int age, String gender, String course, int yearLevel) {
        if (firstName == null || firstName.trim().isEmpty() ||
                lastName == null || lastName.trim().isEmpty() ||
                gender == null || gender.trim().isEmpty() ||
                course == null || course.trim().isEmpty() ||
                age <= 0 || yearLevel <= 0) {
            SwingUtility.showCustomMsgDialog(appGraphics, "EMPTY CREDENTIALS", "All fields must be filled.", "OK", "warning_sign.png");
            return null;
        }

        if (middleName == null || middleName.trim().isEmpty()) {
            middleName = "_";
        }

        try (Connection conn = DBHelper.getConnection()) {
            String studentID = DatabaseUtility.generateID(conn, "S", "student_records", "studentID");
            String email = DatabaseUtility.generateSchoolEmail(firstName, lastName, "dlsl.edu.ph");

            String sql = "INSERT INTO student_records (studentID, firstName, lastName, middleName, age, gender, course, yearLevel, school_email) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, studentID);
                stmt.setString(2, firstName);
                stmt.setString(3, lastName);
                stmt.setString(4, middleName);
                stmt.setInt(5, age);
                stmt.setString(6, gender);
                stmt.setString(7, course);
                stmt.setInt(8, yearLevel);
                stmt.setString(9, email);
                // Execute insert and return record if successful
                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    return new StudentRecord(studentID, firstName, lastName, middleName, age, gender, course, yearLevel, email);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //Fetches all student records from the database and returns them as a list of Object arrays.
    public static List<Object[]> fetchAllStudents() {
        List<Object[]> students = new ArrayList<>();
        String sql = "SELECT studentID, lastName, firstName, middleName, age, gender, course, yearLevel, school_email FROM student_records";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            // Iterate through result set and populate rows
            while (rs.next()) {
                Object[] row = {
                        rs.getString("studentID"),
                        rs.getString("lastName"),
                        rs.getString("firstName"),
                        rs.getString("middleName"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("course"),
                        rs.getInt("yearLevel"),
                        rs.getString("school_email")
                };
                students.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    // Converts common string inputs to a numeric year level
    private static Integer parseYearLevel(String input) {
        input = input.toLowerCase();
        if (input.contains("1st")) return 1;
        if (input.contains("2nd")) return 2;
        if (input.contains("3rd")) return 3;
        if (input.contains("4th")) return 4;
        if (input.contains("first")) return 1;
        if (input.contains("second")) return 2;
        if (input.contains("third")) return 3;
        if (input.contains("fourth")) return 4;
        if (input.matches("[1-5]")) return Integer.parseInt(input);
        return null;
    }
    // Searches students based on a flexible query that matches names, ID, gender, email, etc.
    public static List<Object[]> searchStudents(String query) {
        // If query is empty, return all records
        List<Object[]> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return fetchAllStudents();
        }

        query = query.trim().toLowerCase();
        String sql = "SELECT studentID, lastName, firstName, middleName, age, gender, course, yearLevel, school_email FROM student_records WHERE ";

        List<String> conditions = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();
        // Add LIKE conditions for various string fields
        String likeQuery = "%" + query + "%";
        conditions.add("LOWER(studentID) LIKE ?");
        parameters.add(likeQuery);
        conditions.add("LOWER(firstName) LIKE ?");
        parameters.add(likeQuery);
        conditions.add("LOWER(lastName) LIKE ?");
        parameters.add(likeQuery);
        conditions.add("LOWER(middleName) LIKE ?");
        parameters.add(likeQuery);
        conditions.add("LOWER(course) LIKE ?");
        parameters.add(likeQuery);
        conditions.add("LOWER(school_email) LIKE ?");
        parameters.add(likeQuery);
        // Exact gender match
        if ("male".equals(query) || "female".equals(query)) {
            conditions.add("LOWER(gender) = ?");
            parameters.add(query);
        }
        // Numeric queries: match against age and year level
        if (query.matches("\\d+")) {
            int num = Integer.parseInt(query);

            conditions.add("age = ?");
            parameters.add(num);

            if (num >= 1 && num <= 5) {
                conditions.add("yearLevel = ?");
                parameters.add(num);
            }
        } else {
            Integer yearLevel = parseYearLevel(query);
            if (yearLevel != null) {
                conditions.add("yearLevel = ?");
                parameters.add(yearLevel);
            }
        }
        // Join conditions with OR
        sql += String.join(" OR ", conditions);

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Bind parameters to the statement
            for (int i = 0; i < parameters.size(); i++) {
                Object param = parameters.get(i);
                if (param instanceof String) {
                    stmt.setString(i + 1, (String) param);
                } else if (param instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) param);
                }
            }
            // Execute and build results
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                            rs.getString("studentID"),
                            rs.getString("lastName"),
                            rs.getString("firstName"),
                            rs.getString("middleName"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("course"),
                            rs.getInt("yearLevel"),
                            rs.getString("school_email")
                    };
                    results.add(row);
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error during search: " + e.getMessage());
        }

        return results;
    }
    // Searches for students whose ID contains a specific fragment (case-insensitive).
    public static List<StudentRecord> searchStudentsByID(String idFragment) {
        List<StudentRecord> matched = new ArrayList<>();
        for (Object[] row : fetchAllStudents()) {
            String studentID = ((String) row[0]).toLowerCase();
            if (studentID.contains(idFragment.toLowerCase())) {
                matched.add(new StudentRecord(
                        (String) row[0],
                        (String) row[2],
                        (String) row[1],
                        (String) row[3],
                        (int) row[4],
                        (String) row[5],
                        (String) row[6],
                        (int) row[7],
                        (String) row[8]
                ));
            }
        }
        return matched;
    }
    // Updates a student's field (firstName, lastName, etc.) if the current value matches the oldValue.
    public static boolean updateStudentField(String studentID, String oldValue, String newValue) {
        String[] columns = {"firstName", "lastName", "middleName", "age", "gender", "course", "yearLevel", "school_email"};

        try (Connection conn = DBHelper.getConnection()) {
            // Step 1: Get student record by ID
            String query = "SELECT * FROM student_records WHERE studentID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, studentID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        for (String col : columns) {
                            String dbValue;
                            boolean isNumeric = col.equals("age") || col.equals("yearLevel");

                            if (isNumeric) {
                                dbValue = String.valueOf(rs.getInt(col));
                            } else {
                                dbValue = rs.getString(col);
                            }

                            if (dbValue == null) continue;

                            // Case-insensitive & trimmed comparison for strings
                            if (isNumeric ? dbValue.equals(oldValue) : dbValue.trim().equalsIgnoreCase(oldValue.trim())) {
                                // Step 2: Perform update
                                String updateSql = "UPDATE student_records SET " + col + " = ? WHERE studentID = ?";
                                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                                    if (isNumeric) {
                                        updateStmt.setInt(1, Integer.parseInt(newValue));
                                    } else {
                                        updateStmt.setString(1, newValue);
                                    }
                                    updateStmt.setString(2, studentID);
                                    int updated = updateStmt.executeUpdate();
                                    return updated > 0;
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }

        return false;
    }
    // Deletes a student from the database using the student ID.
    public static boolean deleteStudentByID(String studentID) {
        String sql = "DELETE FROM student_records WHERE studentID = ?";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentID);
            int deleted = stmt.executeUpdate();
            return deleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
