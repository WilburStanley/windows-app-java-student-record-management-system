package org.laboratory;
/**
 * Manages the session state for an admin user.
 * This class uses static methods and variables to track whether an admin is logged in,
 * and stores their ID and username for the duration of the session.
 */
public class AdminSession {
    private static String adminID;
    private static String adminUsername;

    public static void startSession(String id, String username) {
        adminID = id;
        adminUsername = username;
    }

    public static void endSession() {
        adminID = null;
        adminUsername = null;
    }

    public static boolean isLoggedIn() {
        return adminID != null && adminUsername != null;
    }

    public static String getAdminID() {
        return adminID;
    }

    public static String getAdminUsername() {
        return adminUsername;
    }
}
