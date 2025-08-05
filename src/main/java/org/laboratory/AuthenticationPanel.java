package org.laboratory;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;

public class AuthenticationPanel extends JPanel {
    private record AuthenticationDialogComponents(JDialog dialog, JPasswordField passwordField, JButton loginButton, JButton cancelButton) { }

    public AuthenticationPanel(AppGraphics appGraphics) {
        appGraphics.setAppTitle("AUTHENTICATION");
        appGraphics.setScreenSize(900, 600);
        this.setBackground(AppColors.PRIMARY);
        this.setLayout(new BorderLayout());

        int mainAuthFieldContainerWidth = 500, mainAuthFieldContainerHeight = 460;

        JPanel mainAuthFieldContainer = SwingUtility.createRoundedPanel(AppColors.SECONDARY, 38, mainAuthFieldContainerWidth, mainAuthFieldContainerHeight);
        mainAuthFieldContainer.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel imageContainer = new JLabel(SwingUtility.loadScalableImageIcon("AppLogo.png", 100, 100));

        JLabel headLabel = SwingUtility.createLabel("LOGIN", 35, AppColors.SECONDARY, AppColors.PRIMARY, "bold");
        headLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel subLabel = SwingUtility.createLabel("Please enter log in details below", 20, AppColors.SECONDARY, Color.BLACK,  "plain");
        subLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel labelContainer = new JPanel(new GridLayout(2,1, 0, 0));
        labelContainer.setPreferredSize(new Dimension(300, 80));
        labelContainer.add(headLabel);
        labelContainer.add(subLabel);

        JPanel headerContainer = SwingUtility.createPlainPanel(AppColors.TRANSPARENT, mainAuthFieldContainerWidth, 130);
        headerContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        headerContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        headerContainer.add(imageContainer);
        headerContainer.add(labelContainer);

        JPanel authFieldContainer = SwingUtility.createPlainPanel(AppColors.SECONDARY, 300, 500);
        authFieldContainer.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel usernameFieldContainer = SwingUtility.createPlainPanel(AppColors.SECONDARY, authFieldContainer.getPreferredSize().width, 90);
        usernameFieldContainer.setLayout(new GridLayout(2, 1));

        JLabel usernameLabel = SwingUtility.createLabel("Username:",20,  AppColors.SECONDARY, Color.BLACK, "bold");
        SwingUtility.RoundedTextField usernameField = createCustomInputField(usernameFieldContainer, usernameLabel);
        usernameField.setPlaceholder("Enter username");

        JPanel passwordFieldContainer = SwingUtility.createPlainPanel(AppColors.SECONDARY, authFieldContainer.getPreferredSize().width, 90);
        passwordFieldContainer.setLayout(new GridLayout(2, 1));

        JLabel passwordLabel = SwingUtility.createLabel("Password:",20,  AppColors.SECONDARY, Color.BLACK, "bold");
        SwingUtility.RoundedPasswordField passwordField = createCustomPasswordField(passwordFieldContainer, passwordLabel);
        passwordField.setPlaceholder("Enter password");

        JPanel whiteSpace = SwingUtility.createPlainPanel(AppColors.SECONDARY, authFieldContainer.getPreferredSize().width, 10);

        SwingUtility.RoundedButton registerCreateAccBtn = SwingUtility.createRoundedButton("Register", 20, 20, authFieldContainer.getPreferredSize().width, 50, Color.BLACK, Color.WHITE, Color.BLACK, 2);
        registerCreateAccBtn.setVisible(false);
        SwingUtility.RoundedButton goToLoginPageBtn = SwingUtility.createRoundedButton("Login", 20, 20, authFieldContainer.getPreferredSize().width, 50, AppColors.SECONDARY, AppColors.PRIMARY, AppColors.SECONDARY, 2);
        goToLoginPageBtn.setVisible(false);
        goToLoginPageBtn.setBorder(null);

        SwingUtility.RoundedButton loginBtn = SwingUtility.createRoundedButton("Login", 20, 20, authFieldContainer.getPreferredSize().width, 50, Color.BLACK, Color.WHITE, Color.BLACK, 2);
        SwingUtility.RoundedButton registerBtn = SwingUtility.createRoundedButton("Register", 20, 20, authFieldContainer.getPreferredSize().width, 50, AppColors.SECONDARY, AppColors.PRIMARY, AppColors.SECONDARY, 2);
        registerBtn.setBorder(null);

        authFieldContainer.add(usernameFieldContainer);
        authFieldContainer.add(passwordFieldContainer);
        authFieldContainer.add(whiteSpace);
        authFieldContainer.add(registerCreateAccBtn);
        authFieldContainer.add(goToLoginPageBtn);
        authFieldContainer.add(loginBtn);
        authFieldContainer.add(registerBtn);

        mainAuthFieldContainer.add(headerContainer);
        mainAuthFieldContainer.add(authFieldContainer);

        registerBtn.addActionListener(v -> {
            // Reset the visual state of the password and username fields and their labels (e.g., remove error highlights)
            AdminAuthHandler.resetPasswordFieldState(passwordField, passwordLabel);
            AdminAuthHandler.resetTextFieldState(usernameField, usernameLabel);
            // Start an indefinite loop until the user either cancels or successfully authenticates
            while (true) {
                // Create the custom authentication dialog UI components
                AuthenticationDialogComponents authDialog = createAuthenticationDialog(appGraphics);
                JDialog dialog = authDialog.dialog;
                // Track whether the login button inside the dialog was clicked
                final boolean[] loginClicked = {false};
                // When the login button is clicked inside the dialog
                authDialog.loginButton.addActionListener(e -> {
                    loginClicked[0] = true; // Mark that login was attempted
                    dialog.dispose();       // Close the dialog
                });
                // When the cancel button is clicked, just close the dialog
                authDialog.cancelButton.addActionListener(e -> dialog.dispose());
                // Show the dialog and block further code until it's closed
                dialog.setVisible(true);
                // If login button was NOT clicked (user cancelled or closed dialog), exit the loop
                if (!loginClicked[0]) break;

                String passwordInput = new String(authDialog.passwordField.getPassword());
                String storedHash = PasswordManager.getStoredHashedPassword();

                if (!passwordInput.isEmpty()) {
                    if (BCrypt.checkpw(passwordInput, storedHash)) {
                        // If password matches, show the registration UI components
                        registerUi(headLabel, subLabel, registerBtn, registerCreateAccBtn, goToLoginPageBtn);
                        registerCreateAccBtn.addActionListener(e -> {
                            AdminAuthHandler.registerAdmin(appGraphics, usernameField, passwordField, usernameLabel, passwordLabel);
                        });
                        // Exit the loop after successful password validation
                        break;
                    } else {
                        SwingUtility.showCustomMsgDialog(appGraphics,"AUTHORIZATION DENIED", "Incorrect Password!", "OK", "error_sign.png");
                    }
                } else {
                    // If password input is empty, highlight the password field with default alert styling
                    SwingUtility.defaultAlert(authDialog.passwordField);
                }
            }
        });
        goToLoginPageBtn.addActionListener(v -> loginUi(headLabel, subLabel, registerBtn, registerCreateAccBtn, goToLoginPageBtn));

        loginBtn.addActionListener(v -> {
            AdminAuthHandler.loginAdmin(appGraphics, usernameField, passwordField, usernameLabel, passwordLabel);
        });

        appGraphics.add(SwingUtility.centerComponent(mainAuthFieldContainer, mainAuthFieldContainerWidth, mainAuthFieldContainerHeight));

        SwingUtility.initFrameVisibility(appGraphics);
    }
    private static void registerUi(JLabel headText, JLabel subText, SwingUtility.RoundedButton registerBtn, SwingUtility.RoundedButton registerCreateAccBtn, SwingUtility.RoundedButton goToLoginPageBtn) {
        headText.setText("REGISTER");
        subText.setText("Create your account to get started");
        subText.setFont(new Font("Arial", Font.PLAIN, 18));

        registerBtn.setVisible(false);
        registerCreateAccBtn.setVisible(true);
        goToLoginPageBtn.setVisible(true);
    }
    private static void loginUi(JLabel headText, JLabel subText, SwingUtility.RoundedButton registerBtn, SwingUtility.RoundedButton registerCreateAccBtn, SwingUtility.RoundedButton goToLoginPageBtn) {
        headText.setText("LOGIN");
        subText.setText("Please enter log in details below");
        subText.setFont(new Font("Arial", Font.PLAIN, 20));

        registerBtn.setVisible(true);
        registerCreateAccBtn.setVisible(false);
        goToLoginPageBtn.setVisible(false);
    }
    private static AuthenticationDialogComponents createAuthenticationDialog(JFrame currentFrame) {
        JPasswordField passwordField = SwingUtility.createPlainPasswordField(18, 250, 40);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel label = SwingUtility.createLabel("Enter system password", 20, AppColors.TRANSPARENT, Color.BLACK, "bold");
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 3, 0));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(label);
        panel.add(passwordField);

        SwingUtility.RoundedButton loginButton = SwingUtility.createRoundedButton("Login", 15, 15, 100, 40, Color.BLACK, Color.WHITE, Color.BLACK, 2);
        SwingUtility.RoundedButton cancelButton = SwingUtility.createRoundedButton("Cancel", 15, 15, 100, 40, Color.BLACK, Color.WHITE, Color.BLACK, 2);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel);

        JDialog dialog = new JDialog(currentFrame, "SYSTEM LOGIN", true);
        dialog.setIconImage(SwingUtility.loadIcon("AppLogo.png", 64));
        dialog.setContentPane(panel);
        dialog.setSize(330, 200);
        dialog.setLocationRelativeTo(currentFrame);

        return new AuthenticationDialogComponents(dialog, passwordField, loginButton, cancelButton);
    }
    private static SwingUtility.RoundedTextField createCustomInputField(JPanel fieldContainer, JLabel label) {
        label.setHorizontalAlignment(SwingConstants.LEFT);

        SwingUtility.RoundedTextField txtField = SwingUtility.createRoundedTextField(10, 20, 20, AppColors.SECONDARY, Color.BLACK, 2);

        fieldContainer.add(label);
        fieldContainer.add(txtField);

        return txtField;
    }
    private static SwingUtility.RoundedPasswordField createCustomPasswordField(JPanel fieldContainer, JLabel label) {
        label.setHorizontalAlignment(SwingConstants.LEFT);

        SwingUtility.RoundedPasswordField txtField = SwingUtility.createRoundedPasswordField(10, 20, 20, AppColors.SECONDARY, Color.BLACK, 2);

        fieldContainer.add(label);
        fieldContainer.add(txtField);

        return txtField;
    }
}