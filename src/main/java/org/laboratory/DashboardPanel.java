package org.laboratory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class DashboardPanel extends JPanel {
    private final TablePanel studentInformationTable;
    public DashboardPanel(AppGraphics appGraphics)  {
        appGraphics.setAppTitle("DASHBOARD - " + AdminSession.getAdminUsername().toUpperCase());
        appGraphics.setScreenSize(1200, 800);
        this.setBackground(AppColors.PRIMARY);
        this.setLayout(new BorderLayout());

        int screenWidth = appGraphics.getScreenWidth();
        int screenHeight = appGraphics.getScreenHeight();

        int menuNavigationPanelWidth = screenWidth / 5;

        JPanel menuNavigationPanel = SwingUtility.createPlainPanel(AppColors.LIGHT_BLACK, menuNavigationPanelWidth, screenHeight - 30);
        menuNavigationPanel.setLayout(new BorderLayout());

        menuNavigationPanel.setPreferredSize(new Dimension(menuNavigationPanelWidth, getHeight()));

        JPanel mainContentPanel = SwingUtility.createPlainPanel(AppColors.GRAY, 0, screenHeight - 30);
        mainContentPanel.setLayout(new BorderLayout());

        JPanel cardContainer = new JPanel(new CardLayout());
        cardContainer.setBackground(AppColors.GRAY);
        mainContentPanel.add(SwingUtility.centerComponent(cardContainer, 900, 1000));

        JPanel navigationLogoutBtnContainer = SwingUtility.createPlainPanel(AppColors.LIGHT_BLACK, menuNavigationPanelWidth, 80);
        SwingUtility.GradientButton navigationLogoutBtn = new SwingUtility.GradientButton(AppColors.LIGHT_BLACK,"Logout", 19, Color.WHITE, "exit_icon.png", 23, 23, 30, menuNavigationPanelWidth, 50);

        navigationLogoutBtnContainer.add(navigationLogoutBtn);
        navigationLogoutBtnContainer.add(createWhiteSpace(AppColors.LIGHT_BLACK, menuNavigationPanelWidth, 20));

        JPanel navigationContainer = SwingUtility.createPlainPanel(AppColors.LIGHT_BLACK, menuNavigationPanelWidth, 500);
        navigationContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JPanel boardContainer = SwingUtility.createPlainPanel(AppColors.LIGHT_BLACK, menuNavigationPanelWidth, 80);
        boardContainer.setLayout(new BorderLayout());

        JLabel imageContainer = new JLabel(SwingUtility.loadScalableImageIcon("AppLogo.png", 40, 40));
        imageContainer.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));

        JPanel boardLabelContainer = SwingUtility.createPlainPanel(AppColors.LIGHT_BLACK, 0, 0);
        boardLabelContainer.setLayout(new GridLayout(2, 1, 0, 0));
        boardLabelContainer.setBorder(BorderFactory.createEmptyBorder(18, 0, 18, 0));

        JLabel boardHeadLabel = SwingUtility.createLabel("Student Information",18,  AppColors.LIGHT_BLACK, AppColors.PRIMARY, "bold");
        JLabel boardSubLabel = SwingUtility.createLabel("Management System",14,  AppColors.LIGHT_BLACK, Color.WHITE, "bold");

        boardLabelContainer.add(boardHeadLabel);
        boardLabelContainer.add(boardSubLabel);

        boardContainer.add(imageContainer, BorderLayout.WEST);
        boardContainer.add(boardLabelContainer, BorderLayout.CENTER);

        SwingUtility.GradientButton navigationHomeBtn = new SwingUtility.GradientButton(AppColors.LIGHT_BLACK,"Home", 18, Color.WHITE, "home_icon.png", 20, 20, 10,  menuNavigationPanelWidth, 35);
        navigationHomeBtn.setHorizontalAlignment(SwingConstants.CENTER);
        navigationHomeBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
        navigationHomeBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel mainNavigationBtnContainer = SwingUtility.createPlainPanel(AppColors.LIGHT_BLACK, menuNavigationPanelWidth, 300);
        SwingUtility.GradientButton navigationAddBtn = new SwingUtility.GradientButton(AppColors.LIGHT_BLACK,"Add", 19, Color.WHITE, "add_icon.png", 23, 23, 30, menuNavigationPanelWidth, 50);
        SwingUtility.GradientButton navigationUpdateBtn = new SwingUtility.GradientButton(AppColors.LIGHT_BLACK,"Update", 19, Color.WHITE, "Update_icon.png", 23, 23, 30, menuNavigationPanelWidth, 50);
        SwingUtility.GradientButton navigationSearchBtn = new SwingUtility.GradientButton(AppColors.LIGHT_BLACK,"Search", 19, Color.WHITE, "search_icon.png", 23, 23, 30, menuNavigationPanelWidth, 50);
        SwingUtility.GradientButton navigationLogsBtn = new SwingUtility.GradientButton(AppColors.LIGHT_BLACK,"Logs", 19, Color.WHITE, "arrow_icon.png", 23, 23, 30, menuNavigationPanelWidth, 50);

        mainNavigationBtnContainer.add(navigationAddBtn);
        mainNavigationBtnContainer.add(navigationUpdateBtn);
        mainNavigationBtnContainer.add(navigationSearchBtn);
        mainNavigationBtnContainer.add(navigationLogsBtn);

        List<SwingUtility.GradientButton> allButtons = Arrays.asList(navigationHomeBtn, navigationAddBtn, navigationUpdateBtn, navigationSearchBtn, navigationLogsBtn);

        SwingUtility.setActiveButton(navigationHomeBtn, allButtons);

        navigationContainer.add(boardContainer);
        navigationContainer.add(createWhiteSpace(AppColors.LIGHT_BLACK, menuNavigationPanelWidth, 30));
        navigationContainer.add(navigationHomeBtn);
        navigationContainer.add(createWhiteSpace(AppColors.LIGHT_BLACK, menuNavigationPanelWidth, 20));
        navigationContainer.add(mainNavigationBtnContainer);

        menuNavigationPanel.add(navigationContainer, BorderLayout.NORTH);
        menuNavigationPanel.add(navigationLogoutBtnContainer, BorderLayout.SOUTH);

        JPanel mainContentHeader = new SwingUtility.GradientPanel(130);
        mainContentHeader.setLayout(new GridBagLayout());

        JLabel mainContentHeaderLabel = SwingUtility.createLabel("Student Records Dashboard", 50, AppColors.TRANSPARENT, Color.WHITE, "bold");

        mainContentHeader.add(mainContentHeaderLabel);

        mainContentPanel.add(mainContentHeader, BorderLayout.NORTH);

        this.add(menuNavigationPanel, BorderLayout.WEST);
        this.add(mainContentPanel, BorderLayout.CENTER);

        String[] studentsColumnDataNames = {"Student ID", "Last Name", "First Name", "Middle Name", "Age", "Gender", "Course", "Year Level", "School Email"};
        studentInformationTable = new TablePanel(studentsColumnDataNames, 0, 0);
        refreshStudentInformationTable();

        JPanel fieldContainer = SwingUtility.createRoundedPanel(AppColors.SECONDARY, 38, 300, 600);
        fieldContainer.setVisible(false);
        fieldContainer.setLayout(new GridLayout(1, 2));

        JPanel columnAFieldContainer = SwingUtility.createRoundedPanel(AppColors.TRANSPARENT, 38, 300, fieldContainer.getPreferredSize().height);
        columnAFieldContainer.setLayout(new GridLayout(4, 1,0, 30));
        columnAFieldContainer.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 10));

        LabeledField firstNameAddedValue = new LabeledField("First Name:", "Enter First name");
        LabeledField lastNameAddedValue = new LabeledField("Last Name:", "Enter Last name");
        LabeledField middleNameAddedValue = new LabeledField("Middle Name:", "Enter Middle name");

        JPanel columnALastRowField = new JPanel(new GridLayout(1, 2, 10, 0));
        columnALastRowField.setBackground(AppColors.SECONDARY);

        String[] genderOptions = {"Male", "Female"};
        LabeledDropdown genderField = new LabeledDropdown("Gender", genderOptions);

        LabeledField ageAddedValue = new LabeledField("Age:", "Enter age");

        columnALastRowField.add(genderField.getContainer());
        columnALastRowField.add(ageAddedValue.container);

        columnAFieldContainer.add(firstNameAddedValue.container);
        columnAFieldContainer.add(lastNameAddedValue.container);
        columnAFieldContainer.add(middleNameAddedValue.container);
        columnAFieldContainer.add(columnALastRowField);

        JPanel columnBFieldContainer = SwingUtility.createRoundedPanel(AppColors.TRANSPARENT, 38, 0, fieldContainer.getPreferredSize().height);
        columnBFieldContainer.setLayout(new BorderLayout(0, 30));
        columnBFieldContainer.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 25));

        JPanel columnBSetAContainer = SwingUtility.createRoundedPanel(AppColors.TRANSPARENT, 38, 0, 265);
        columnBSetAContainer.setLayout(new GridLayout(2, 1,0, 30));
        JPanel columnBSetBContainer = SwingUtility.createRoundedPanel(AppColors.TRANSPARENT, 38, 300, 70);
        columnBSetBContainer.setLayout(new FlowLayout(FlowLayout.RIGHT));

        SwingUtility.GradientButton addCredentialsBtn = persistentGradientBtn("Add", 25, 150, 50, allButtons);

        columnBSetBContainer.add(addCredentialsBtn);

        columnBFieldContainer.add(columnBSetAContainer, BorderLayout.NORTH);
        columnBFieldContainer.add(columnBSetBContainer, BorderLayout.SOUTH);

        LabeledField courseAddedValue = new LabeledField("Course:", "Enter Course");
        LabeledField yearLevelAddedValue = new LabeledField("Year Level:", "Enter year level (e.g. 1–4...)");

        columnBSetAContainer.add(courseAddedValue.container);
        columnBSetAContainer.add(yearLevelAddedValue.container);

        fieldContainer.add(columnAFieldContainer);
        fieldContainer.add(columnBFieldContainer);

        cardContainer.add(studentInformationTable, "TABLE");
        cardContainer.add(fieldContainer, "FORM");

        addCredentialsBtn.addActionListener(v -> {
            String firstName = firstNameAddedValue.textField.getText().trim();
            String lastName = lastNameAddedValue.textField.getText().trim();
            String middleName = middleNameAddedValue.textField.getText().trim();

            int age;
            try {
                // Try parsing and validating the age input
                age = Integer.parseInt(ageAddedValue.textField.getText().trim());
                if (age <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                // Show error dialog for invalid age input and reset age field
                SwingUtility.showCustomMsgDialog(appGraphics, "INVALID AGE", "Enter a valid positive age.", "OK", "warning_sign.png");
                ageAddedValue.textField.setText("1");
                return;
            }

            int yearLevel;
            try {
                // Try parsing and validating the year level input
                yearLevel = Integer.parseInt(yearLevelAddedValue.textField.getText().trim());
                if (yearLevel < 1) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                // Show error dialog for invalid year level and reset the field
                SwingUtility.showCustomMsgDialog(appGraphics, "INVALID YEAR LEVEL", "Enter a valid year level (1 or above).", "OK", "warning_sign.png");
                yearLevelAddedValue.textField.setText("1");
                return;
            }

            String gender = genderField.getSelectedValue();
            String course = courseAddedValue.textField.getText().trim();
            // Attempt to add the student record
            StudentInformationHandler.StudentRecord student = StudentInformationHandler.addStudent(appGraphics, firstName, lastName, middleName, age, gender, course, yearLevel);

            if (student != null) {
                // If student creation is successful, add the record to the table
                studentInformationTable.addRow(new Object[] {
                        student.studentID,
                        student.lastName,
                        student.firstName,
                        student.middleName,
                        student.age,
                        student.gender,
                        student.course,
                        student.yearLevel,
                        student.email
                });
                // Clear all input fields
                firstNameAddedValue.textField.setText("");
                lastNameAddedValue.textField.setText("");
                middleNameAddedValue.textField.setText("");
                ageAddedValue.textField.setText("");
                courseAddedValue.textField.setText("");
                yearLevelAddedValue.textField.setText("");
                genderField.getComboBox().setSelectedIndex(0);

                SwingUtility.showCustomMsgDialog(appGraphics, "OPERATION SUCCESSFUL", "Student added successfully.", "OK", "information_sign.png");
            } else {
                SwingUtility.showCustomMsgDialog(appGraphics, "OPERATION FAILED", "Failed to add student.", "OK", "error_sign.png");
            }
        });
        String[] historyLogsColumnDataNames = {"Log ID", "Admin ID", "Admin Username", "Action", "Time Stamp"};
        TablePanel historyLogsTable = new TablePanel(historyLogsColumnDataNames, 0, 0);
        // Fetch the history logs data from the database as a list of object arrays (each array represents a row)
        List<Object[]> logsTable = DatabaseUtility.fetchHistoryLogs();
        // Iterate through each log record and add it as a row to the history logs table
        for (Object[] row : logsTable) {
            historyLogsTable.addRow(row);
        }
        cardContainer.add(historyLogsTable, "SESSION LOGS");

        CardLayout cl = (CardLayout)(cardContainer.getLayout());

        JPanel searchPanelContainer = SwingUtility.createPlainPanel(AppColors.GRAY, 0, 0);
        searchPanelContainer.setLayout(new BorderLayout(0, 20));

        JPanel searchEngineFieldContainer = SwingUtility.createRoundedPanel(AppColors.SECONDARY, 38, 0, 150);
        searchEngineFieldContainer.setLayout(new BorderLayout(0, 30));
        searchEngineFieldContainer.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        LabeledField searchEngineField = new LabeledField("Search", "Ex. Enter Student ID (e.g., S20250001)");
        searchEngineField.container.setPreferredSize(new Dimension(450, searchEngineFieldContainer.getPreferredSize().height));

        JPanel searchStudentIDBtnContainer = SwingUtility.createPlainPanel(AppColors.SECONDARY, 150, searchEngineFieldContainer.getPreferredSize().height);
        searchStudentIDBtnContainer.setLayout(new BorderLayout());

        SwingUtility.GradientButton searchStudentIDBtn = persistentGradientBtn("Search", 25, 150, 50, allButtons);

        searchStudentIDBtnContainer.add(searchStudentIDBtn, BorderLayout.SOUTH);

        searchEngineFieldContainer.add(searchEngineField.container, BorderLayout.WEST);
        searchEngineFieldContainer.add(searchStudentIDBtnContainer, BorderLayout.EAST);

        String[] queryColumnDataNames = {"Student ID", "Last Name", "First Name", "Middle Name", "Age", "Gender", "Course", "Year Level", "School Email"};
        TablePanel searchEngineTable = new TablePanel(queryColumnDataNames, 0, 420);

        Runnable performLiveSearch = () -> {
            String query = searchEngineField.textField.getText().trim();
            // Query the student database for matching records
            List<Object[]> results = StudentInformationHandler.searchStudents(query);
            // Update the search results table with the query results
            searchEngineTable.updateTable(results);
        };
        // Add a listener to the search text field to detect changes and trigger live search
        searchEngineField.textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Run live search when text is inserted
                performLiveSearch.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Run live search when text is removed
                performLiveSearch.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // No action needed for attribute changes in plain text components
            }
        });
        // Add an action listener to the search button to perform search and clear the input field
        searchStudentIDBtn.addActionListener(e -> {
            performLiveSearch.run();
            searchEngineField.textField.setText("");
        });

        searchPanelContainer.add(searchEngineFieldContainer, BorderLayout.NORTH);
        searchPanelContainer.add(searchEngineTable, BorderLayout.SOUTH);

        cardContainer.add(searchPanelContainer, "SEARCH ENGINE");

        JPanel updatePanelContainer = SwingUtility.createPlainPanel(AppColors.GRAY, 0, 0);
        updatePanelContainer.setLayout(new BorderLayout(0, 20));

        JPanel updateEngineFieldContainer = SwingUtility.createRoundedPanel(AppColors.SECONDARY, 38, 0, 250);
        updateEngineFieldContainer.setLayout(new GridLayout(1, 2, 20, 0));
        updateEngineFieldContainer.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel updateEngineColumnAContainer = SwingUtility.createPlainPanel(AppColors.SECONDARY, 0, updateEngineFieldContainer.getPreferredSize().height);
        updateEngineColumnAContainer.setLayout(new GridLayout(2, 1, 0, 20));

        JPanel updateEngineColumnBContainer = SwingUtility.createPlainPanel(AppColors.SECONDARY, 0, updateEngineFieldContainer.getPreferredSize().height);
        updateEngineColumnBContainer.setLayout(new GridLayout(2, 1, 0, 20));

        LabeledField updateEngineField = new LabeledField("Enter Data to Modify", "Enter variable to modify");
        updateEngineField.container.setPreferredSize(new Dimension(updateEngineColumnAContainer.getPreferredSize().width, 0));

        LabeledField updateSearchEngineField = new LabeledField("Search ID", "Enter Student ID (e.g., S20250001)");
        updateSearchEngineField.container.setPreferredSize(new Dimension(updateEngineColumnAContainer.getPreferredSize().width, 0));

        updateEngineColumnAContainer.add(updateEngineField.container);
        updateEngineColumnAContainer.add(updateSearchEngineField.container);

        LabeledField changerEngineField = new LabeledField("As", "Changes");
        changerEngineField.container.setPreferredSize(new Dimension(updateEngineColumnBContainer.getPreferredSize().width, 0));

        JPanel updateBtnContainer = SwingUtility.createPlainPanel(AppColors.SECONDARY, 300, updateEngineFieldContainer.getPreferredSize().height);
        updateBtnContainer.setLayout(new GridLayout(1, 2, 20, 0));
        updateBtnContainer.setBorder(BorderFactory.createEmptyBorder(40, 20, 0, 20));

        SwingUtility.GradientButton editStudentBtn = persistentGradientBtn("Edit", 25, 150, 50, allButtons);
        SwingUtility.GradientButton deleteStudentBtn = persistentGradientBtn("Delete", 25, 150, 50, allButtons);

        updateBtnContainer.add(editStudentBtn);
        updateBtnContainer.add(deleteStudentBtn);

        updateEngineColumnBContainer.add(changerEngineField.container);
        updateEngineColumnBContainer.add(updateBtnContainer);

        updateEngineFieldContainer.add(updateEngineColumnAContainer);
        updateEngineFieldContainer.add(updateEngineColumnBContainer);

        String[] ED_queryColumnNames = {"Student ID", "Last Name", "First Name", "Middle Name", "Age", "Gender", "Course", "Year Level", "School Email"};
        TablePanel updateEngineTable = new TablePanel(ED_queryColumnNames, 0, 330);
        // Add a DocumentListener to the text field to respond to text input changes for live searching
        updateSearchEngineField.textField.getDocument().addDocumentListener(new DocumentListener() {
            // Helper method to perform search when text changes
            private void search() {
                String input = updateSearchEngineField.textField.getText().trim();
                // If input is not empty, perform search by student ID
                if (!input.isEmpty()) {
                    // Search students by ID and get a list of matching StudentRecord objects
                    List<StudentInformationHandler.StudentRecord> results = StudentInformationHandler.searchStudentsByID(input);
                    // Update the table data with the search results
                    updateEngineTable.updateTableData(results);
                } else {
                    // If input is empty, clear the table by passing an empty list
                    updateEngineTable.updateTableData(Collections.emptyList());
                }
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Run search when text is inserted
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Run search when text is removed
                search();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                // Run search when attributes change (e.g., styling), mostly for completeness
                search();
            }
        });

        editStudentBtn.addActionListener(e -> {
            String studentID = updateSearchEngineField.textField.getText().trim();
            String valueToModify = updateEngineField.textField.getText().trim();
            String newValue = changerEngineField.textField.getText().trim();

            if (studentID.isEmpty() || valueToModify.isEmpty() || newValue.isEmpty()) {
                SwingUtility.showCustomMsgDialog(appGraphics, "EMPTY CREDENTIALS", "All fields must be filled.", "OK", "warning_sign.png");
                return;
            }
            // Call the handler method to update the student record field with the new value
            boolean success = StudentInformationHandler.updateStudentField(studentID, valueToModify, newValue);

            if (success) {
                SwingUtility.showCustomMsgDialog(appGraphics, "OPERATION SUCCESSFUL", "Student record updated successfully", "OK", "information_sign.png");
                // Search again for the updated student by ID to refresh the displayed data
                List<StudentInformationHandler.StudentRecord> updatedResults = StudentInformationHandler.searchStudentsByID(studentID);
                // Update the update engine table with the refreshed student data
                updateEngineTable.updateTableData(updatedResults);
                // Refresh the main student information table elsewhere in the UI (method assumed implemented)
                refreshStudentInformationTable();
                // Clear the input fields for modification and new value
                updateEngineField.textField.setText("");
                changerEngineField.textField.setText("");
            } else {
                SwingUtility.showCustomMsgDialog(appGraphics, "OPERATION FAILED", "No matching field found for the value to modify.", "OK", "warning_sign.png");
            }
        });
        deleteStudentBtn.addActionListener(e -> {
            // Get the selected row index from the update engine table
            int selectedRow = updateEngineTable.table.getSelectedRow();
            // If no row is selected, show a warning dialog and exit
            if (selectedRow == -1) {
                SwingUtility.showCustomMsgDialog(appGraphics, "NO SELECTION", "Please select a student to delete.", "OK", "warning_sign.png");
                return;
            }
            // Retrieve the Student ID from the selected row (column 0 assumed to be student ID)
            String studentID = (String) updateEngineTable.table.getValueAt(selectedRow, 0);
            // Show a confirmation dialog asking user to confirm deletion of the selected student
            boolean confirm = SwingUtility.showCustomConfirmDialog(appGraphics, "Confirm Deletion", "Are you sure you want to delete student ID: " + studentID + "?", "question_sign.png");

            if (confirm) {
                // Attempt to delete the student by ID via the handler method
                boolean success = StudentInformationHandler.deleteStudentByID(studentID);
                if (success) {
                    SwingUtility.showCustomMsgDialog(appGraphics, "OPERATION SUCCESSFUL", "Student deleted successfully.", "OK", "information_sign.png");
                    // Refresh the table with the current query (re-run search by ID from input field)
                    String currentQuery = updateSearchEngineField.textField.getText().trim();
                    List<StudentInformationHandler.StudentRecord> refreshedResults = StudentInformationHandler.searchStudentsByID(currentQuery);
                    updateEngineTable.updateTableData(refreshedResults);
                    // Refresh the main student information table elsewhere in the UI
                    refreshStudentInformationTable();
                    // Clear the fields for updating/editing student data
                    updateEngineField.textField.setText("");
                    changerEngineField.textField.setText("");
                } else {
                    SwingUtility.showCustomMsgDialog(appGraphics, "OPERATION FAILED", "Failed to delete student.", "OK", "warning_sign.png");
                }
            }
        });

        updatePanelContainer.add(updateEngineFieldContainer, BorderLayout.NORTH);
        updatePanelContainer.add(updateEngineTable, BorderLayout.SOUTH);

        cardContainer.add(updatePanelContainer, "UPDATE ENGINE");

        navigationHomeBtn.addActionListener(e -> {
            cl.show(cardContainer, "TABLE");
            mainContentHeader.setVisible(true);
            mainContentHeaderLabel.setText("Student Records Dashboard");
            SwingUtility.setActiveButton(navigationHomeBtn, allButtons);
        });
        navigationAddBtn.addActionListener(e -> {
            cl.show(cardContainer, "FORM");
            mainContentHeader.setVisible(false);
            SwingUtility.setActiveButton(navigationAddBtn, allButtons);
        });
        navigationUpdateBtn.addActionListener(e -> {
            cl.show(cardContainer, "UPDATE ENGINE");
            searchPanelContainer.setVisible(false);
            mainContentHeader.setVisible(false);
            SwingUtility.setActiveButton(navigationUpdateBtn, allButtons);
        });
        navigationSearchBtn.addActionListener(e -> {
            cl.show(cardContainer, "SEARCH ENGINE");
            searchPanelContainer.setVisible(true);
            mainContentHeader.setVisible(false);
            SwingUtility.setActiveButton(navigationSearchBtn, allButtons);
        });
        navigationLogsBtn.addActionListener(e -> {
            cl.show(cardContainer, "SESSION LOGS");
            mainContentHeader.setVisible(true);
            mainContentHeaderLabel.setText("Session Logs");
            SwingUtility.setActiveButton(navigationLogsBtn, allButtons);
        });
        navigationLogoutBtn .addActionListener(v -> {
            // Log out the current admin with their ID and username
            AdminAuthHandler.logoutAdmin(AdminSession.getAdminID(), AdminSession.getAdminUsername());
            // End the current admin session locally
            AdminSession.endSession();
            // Launch the authentication screen (login)
            Main.launchAuthentication();
        });

        SwingUtility.initFrameVisibility(appGraphics);
    }
    private void refreshStudentInformationTable() {
        List<Object[]> studentRows = StudentInformationHandler.fetchAllStudents();
        studentInformationTable.updateTable(studentRows);
    }
    private static SwingUtility.GradientButton persistentGradientBtn(String text, int fontSize, int width, int height, List<SwingUtility.GradientButton> allButtons) {
        SwingUtility.GradientButton gradientBtn = createGradientButton(text, fontSize, width, height);
        gradientBtn.setHorizontalAlignment(SwingConstants.CENTER);
        gradientBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
        gradientBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gradientBtn.setPersistentActive(true);
        gradientBtn.setActive(true);
        gradientBtn.setCornerRadius(20);

        SwingUtility.setActiveButton(gradientBtn, allButtons);

        return gradientBtn;
    }
    private static SwingUtility.GradientButton createGradientButton(String text, int fontSize, int width, int height) {
        SwingUtility.GradientButton gradientBtn = new SwingUtility.GradientButton(AppColors.LIGHT_BLACK,text, fontSize, Color.WHITE, "", 0, 0, 0,  width, height);
        gradientBtn.setHorizontalAlignment(SwingConstants.CENTER);
        gradientBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
        gradientBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gradientBtn.setPersistentActive(true);
        gradientBtn.setActive(true);
        gradientBtn.setCornerRadius(20);

        return gradientBtn;
    }
    private static JPanel createWhiteSpace(Color background, int width, int height) {
        return SwingUtility.createPlainPanel(background, width, height);
    }
    private static SwingUtility.RoundedTextField createCustomInputField(JPanel fieldContainer, JLabel label) {
        label.setHorizontalAlignment(SwingConstants.LEFT);

        SwingUtility.RoundedTextField txtField = SwingUtility.createRoundedTextField(10, 25, 20, AppColors.GRAY, AppColors.GRAY, 2);

        fieldContainer.add(label);
        fieldContainer.add(txtField);

        return txtField;
    }
    public static class LabeledField {
        private JPanel container;
        public SwingUtility.RoundedTextField textField;

        public LabeledField(String labelText, String placeholder) {
            container = SwingUtility.createPlainPanel(AppColors.SECONDARY, 300, 90);
            container.setLayout(new GridLayout(2, 1));

            JLabel label = SwingUtility.createLabel(labelText, 30, AppColors.SECONDARY, Color.BLACK, "bold");
            textField = createCustomInputField(container, label);
            textField.setPlaceholder(placeholder);

            container.add(label);
            container.add(textField);
        }
    }
    public static class LabeledDropdown {
        private JPanel container;
        private JComboBox<String> comboBox;

        public LabeledDropdown(String labelText, String[] options) {
            container = SwingUtility.createPlainPanel(AppColors.SECONDARY, 300, 90);
            container.setLayout(new GridLayout(2, 1));

            JLabel label = SwingUtility.createLabel(labelText, 30, AppColors.SECONDARY, Color.BLACK, "bold");

            comboBox = new JComboBox<>(options) {
                @Override
                public Dimension getPreferredSize() {
                    Dimension size = super.getPreferredSize();
                    return new Dimension(size.width, 100);
                }
            };

            comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
            comboBox.setUI(new SwingUtility.RoundedComboBoxUI());

            container.add(label);
            container.add(comboBox);
        }

        public JPanel getContainer() {
            return container;
        }

        public JComboBox<String> getComboBox() {
            return comboBox;
        }

        public String getSelectedValue() {
            return (String) comboBox.getSelectedItem();
        }
    }

}
