package org.laboratory;

import java.awt.*;
/**
 * Defines a centralized color palette for the application using named constants.
 * Provides easy access to commonly used colors via HEX and RGB,
 * and includes a utility method for converting hex strings to Color objects.
 */
public class AppColors {
    // HEX CODES
    public static final Color PRIMARY = Color.decode("#087830");
    public static final Color SECONDARY = Color.decode("#F7F7F7");
    public static final Color LIGHT_BLACK = Color.decode("#252525");
    public static final Color GRAY = Color.decode("#e8e8e8");
    // RGB
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static Color hex(String hex) {
        return Color.decode(hex);
    }
}
