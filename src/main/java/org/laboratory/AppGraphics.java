package org.laboratory;

import javax.swing.*;
import java.awt.*;
/**
 * A custom JFrame wrapper that centralizes application window setup and customization.
 * Provides methods for setting screen size, app title, icon, background color,
 * and clearing content. Designed to simplify and unify the graphical appearance of the app.
 */
public class AppGraphics extends JFrame {
    private int screenWidth;
    private int screenHeight;
    private String appTitle;

    public AppGraphics() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(SwingUtility.loadIcon("AppLogo.png", 64));
        this.setLocationRelativeTo(null);
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenSize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.setSize(width, height);
    }

    public void setAppTitle(String title) {
        this.appTitle = title;
        super.setTitle(title);
    }

    public String getAppTitle() {
        return this.appTitle;
    }

    public void setAppBackground(Color color) {
        this.getContentPane().setBackground(color);
        this.getContentPane().repaint();
        this.getContentPane().revalidate();
    }

    public void clearContent() {
        this.getContentPane().removeAll();
        this.revalidate();
        this.repaint();
    }
}
