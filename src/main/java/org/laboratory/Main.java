package org.laboratory;

import javax.swing.*;

public class Main {
    private static AppGraphics appGraphics;

    public static void main(String[] args) {
        // Ensures GUI creation runs on the Event Dispatch Thread (best practice for Swing apps)
        SwingUtilities.invokeLater(() -> {
            appGraphics = new AppGraphics(); // Create the main application frame
            if (AdminSession.isLoggedIn()) { // Check if an admin is already logged in
                launchDashboard(); // Launch the dashboard panel if logged in
            } else {
                launchAuthentication(); // Otherwise, show the authentication/login panel
            }
        });
    }
    public static void launchAuthentication() {
        appGraphics.clearContent(); // Remove all existing content from the frame
        AuthenticationPanel authPanel = new AuthenticationPanel(appGraphics);  // Create Authentication Panel
        appGraphics.add(authPanel);  // Add authentication panel to the frame
        SwingUtility.initFrameVisibility(appGraphics); // Make the frame visible and ready
    }
    public static void launchDashboard() {
        appGraphics.clearContent(); // Remove all existing content from the frame
        DashboardPanel dashboardPanel = new DashboardPanel(appGraphics); // Create Dashboard Panel
        appGraphics.add(dashboardPanel); // Add dashboard panel to the frame
        SwingUtility.initFrameVisibility(appGraphics); // Make the frame visible and ready
    }
}