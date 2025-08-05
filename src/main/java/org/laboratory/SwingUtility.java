package org.laboratory;
/**
 * A utility class for creating custom Swing UI components and dialogs with reusable methods
 * to reduce redundant UI code (e.g., creating labels, panels, text fields, message/confirmation dialogs)
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.swing.text.*;

public class SwingUtility {
    public static void showCustomMsgDialog(JFrame parent, String title, String message, String btnMessage, String filename) {
        JDialog dialog = new JDialog(parent, title, true);

        int maxDialogWidth = 500;

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        Icon icon = SwingUtility.loadScalableImageIcon(filename, 60, 60);
        JLabel iconLabel = new JLabel(icon);
        assert icon != null;
        iconLabel.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));

        JTextArea messageArea = getJTextArea(message, maxDialogWidth);

        JPanel textPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        textPanel.add(messageArea, gbc);

        JPanel iconAndMessagePanel = new JPanel(new BorderLayout(10, 0));
        iconAndMessagePanel.add(iconLabel, BorderLayout.WEST);
        iconAndMessagePanel.add(textPanel, BorderLayout.CENTER);

        contentPanel.add(iconAndMessagePanel, BorderLayout.CENTER);

        JPanel btnContainer = new JPanel();
        btnContainer.setPreferredSize(new Dimension(dialog.getWidth(), 60));
        RoundedButton btn = createRoundedButton(btnMessage, 15, 15, 100, 40, Color.BLACK, Color.WHITE, Color.BLACK, 2);
        btn.addActionListener(e -> dialog.dispose());
        btnContainer.add(btn);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(btnContainer, BorderLayout.SOUTH);

        dialog.setIconImage(SwingUtility.loadIcon("AppLogo.png", 64));
        dialog.setContentPane(mainPanel);
        dialog.pack();

        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    public static boolean showCustomConfirmDialog(JFrame parent, String title, String message, String filename) {
        JDialog dialog = new JDialog(parent, title, true);

        int maxDialogWidth = 500;
        final boolean[] result = {false};

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        Icon icon = SwingUtility.loadScalableImageIcon(filename, 60, 60);
        JLabel iconLabel = new JLabel(icon);
        assert icon != null;
        iconLabel.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));

        JTextArea messageArea = getJTextArea(message, maxDialogWidth);

        JPanel textPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        textPanel.add(messageArea, gbc);

        JPanel iconAndMessagePanel = new JPanel(new BorderLayout(10, 0));
        iconAndMessagePanel.add(iconLabel, BorderLayout.WEST);
        iconAndMessagePanel.add(textPanel, BorderLayout.CENTER);

        contentPanel.add(iconAndMessagePanel, BorderLayout.CENTER);

        JPanel btnContainer = new JPanel();
        btnContainer.setPreferredSize(new Dimension(dialog.getWidth(), 60));

        RoundedButton yesBtn = createRoundedButton("Yes", 15, 15, 100, 40, Color.BLACK, Color.WHITE, Color.BLACK, 2);
        RoundedButton noBtn = createRoundedButton("No", 15, 15, 100, 40, Color.BLACK, Color.WHITE, Color.BLACK, 2);

        yesBtn.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });

        noBtn.addActionListener(e -> {
            result[0] = false;
            dialog.dispose();
        });

        btnContainer.add(yesBtn);
        btnContainer.add(noBtn);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(btnContainer, BorderLayout.SOUTH);

        dialog.setIconImage(SwingUtility.loadIcon("AppLogo.png", 64));
        dialog.setContentPane(mainPanel);
        dialog.pack();

        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        return result[0];
    }
    private static JTextArea getJTextArea(String message, int maxDialogWidth) {
        JTextArea messageArea = new JTextArea(message);
        messageArea.setFont(new Font("Arial", Font.BOLD, 20));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setEditable(false);
        messageArea.setFocusable(false);
        messageArea.setOpaque(false);
        messageArea.setBorder(null);

        FontMetrics fm = messageArea.getFontMetrics(messageArea.getFont());
        int textWidth = fm.stringWidth(message);

        int padding = 40;
        int calculatedWidth = Math.min(textWidth + padding, maxDialogWidth - 100);

        messageArea.setSize(calculatedWidth, Short.MAX_VALUE);

        int preferredTextHeight = messageArea.getPreferredSize().height;
        messageArea.setPreferredSize(new Dimension(calculatedWidth, preferredTextHeight));
        return messageArea;
    }

    public static JLabel createLabel(String text, int textSize, Color backgroundColor, Color textColor, String type) {
        JLabel label = new JLabel(text);
        label.setBackground(backgroundColor);
        label.setForeground(textColor);
        label.setOpaque(true);

        switch (type.toLowerCase()) {
            case "bold":
                label.setFont(new Font("Arial", Font.BOLD, textSize));
                break;
            case "italic":
                label.setFont(new Font("Arial", Font.ITALIC, textSize));
                break;
            default:
                label.setFont(new Font("Arial", Font.PLAIN, textSize));
        }

        return label;
    }
    public static JPanel createPlainPanel(Color backgroundColor, int width, int height) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(backgroundColor);

        return panel;
    }
    public static JButton createPlainButton(String text, int fontSize, Color backgroundColor, Color foreground, int width, int height) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setBackground(backgroundColor);
        button.setForeground(foreground);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Arial", Font.BOLD, fontSize));

        SwingUtility.setHandCursorOnHover(button);

        return button;
    }
    public static Icon loadScalableImageIcon(String filename, int width, int height) {
        try {
            URL resource = SwingUtility.class.getResource("/assets/" + filename);
            if (resource == null) throw new IllegalArgumentException("Image not found: " + filename);

            BufferedImage original = ImageIO.read(resource);

            return new ScalableIcon(original, width, height);
        } catch (Exception e) {
            System.err.println("Failed to load image: " + e.getMessage());
            return null;
        }
    }
    private record ScalableIcon(BufferedImage image, int width, int height) implements Icon {
        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.drawImage(image, x, y, width, height, null);

            g2.dispose();
        }
    }
    public static Image loadIcon(String filename, int targetSize) {
        try {
            URL resource = SwingUtility.class.getResource("/assets/" + filename);
            if (resource == null) throw new IllegalArgumentException("Image not found: " + filename);

            BufferedImage original = ImageIO.read(resource);
            BufferedImage resized = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2 = resized.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.drawImage(original, 0, 0, targetSize, targetSize, null);
            g2.dispose();

            return resized;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    public static void initFrameVisibility(JFrame... frames) {
        for (JFrame frame : frames) {
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
    public static JPasswordField createPlainPasswordField(int fontSize, int width, int height) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(width, height));
        passwordField.setBorder(new LineBorder(Color.BLACK, 3));
        passwordField.setFont(new Font("Arial", Font.BOLD, fontSize));

        return passwordField;
    }
    public static Box centerComponent(JComponent component, int width, int height) {
        component.setMaximumSize(new Dimension(width, height));
        component.setMinimumSize(new Dimension(width, height));

        Box box = new Box(BoxLayout.Y_AXIS);
        box.add(Box.createVerticalGlue());
        box.add(component);
        box.add(Box.createVerticalGlue());

        return box;
    }
    public static JPanel createRoundedPanel(Color background, int radius, int width, int height) {
        return new JPanel() {
            {
                this.setBackground(background);
                this.setOpaque(false);
                this.setPreferredSize(new Dimension(width, height));
            }

            @Override
            protected void paintComponent(Graphics g) {
                paintRoundedBackground(g, this, getBackground(), radius);
                super.paintComponent(g);
            }
        };
    }
    public static class RoundedTextField extends JTextField {
        private Color backgroundColor;
        private Color borderColor;
        private final int radius;
        private final int borderThickness;
        private String placeholder = "";
        private Color placeholderColor = new Color(150, 150, 150);
        private final int textSize;

        public RoundedTextField(int columns, int textSize, int radius, Color background, Color borderColor, int borderThickness) {
            super(columns);
            this.backgroundColor = background;
            this.borderColor = borderColor;
            this.radius = radius;
            this.borderThickness = borderThickness;
            this.textSize = textSize;

            this.setFont(new Font("Arial", Font.PLAIN, textSize));
            this.setOpaque(false);
            this.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

            this.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    repaint();
                }
            });
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
            repaint();
        }

        public void setPlaceholderColor(Color color) {
            this.placeholderColor = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            SwingUtility.paintRoundedBackground(g, this, backgroundColor, radius);

            if (getText().isEmpty() && !isFocusOwner() && placeholder != null && !placeholder.isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                Font placeholderFont = new Font("Arial", Font.PLAIN, textSize);
                g2.setFont(placeholderFont);
                g2.setColor(placeholderColor);

                Insets insets = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                int x = insets.left;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

                g2.drawString(placeholder, x, y);
                g2.dispose();
            }

            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            SwingUtility.paintRoundedBorder(g, this, borderColor, radius, borderThickness);
        }

        public void setBorderColor(Color color) {
            this.borderColor = color;
            repaint();
        }

        public void setBackgroundColor(Color color) {
            this.backgroundColor = color;
            repaint();
        }
    }

    public static RoundedTextField createRoundedTextField(int columns, int textSize, int radius, Color background, Color borderColor, int borderThickness) {
        return new RoundedTextField(columns, textSize, radius, background, borderColor, borderThickness);
    }
    public static class RoundedPasswordField extends JPasswordField {
        private Color backgroundColor;
        private Color borderColor;
        private final int radius;
        private final int borderThickness;
        private String placeholder = "";
        private Color placeholderColor = new Color(150, 150, 150);
        private final int textSize;

        public RoundedPasswordField(int columns, int textSize, int radius, Color background, Color borderColor, int borderThickness) {
            super(columns);
            this.backgroundColor = background;
            this.borderColor = borderColor;
            this.radius = radius;
            this.borderThickness = borderThickness;
            this.textSize = textSize;

            this.setFont(new Font("Arial", Font.PLAIN, textSize));
            this.setOpaque(false);
            this.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

            this.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    repaint();
                }
            });
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
            repaint();
        }

        public void setPlaceholderColor(Color color) {
            this.placeholderColor = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            SwingUtility.paintRoundedBackground(g, this, backgroundColor, radius);

            if (getText().isEmpty() && !isFocusOwner() && placeholder != null && !placeholder.isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                Font placeholderFont = new Font("Arial", Font.PLAIN, textSize);
                g2.setFont(placeholderFont);
                g2.setColor(placeholderColor);

                Insets insets = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                int x = insets.left;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

                g2.drawString(placeholder, x, y);
                g2.dispose();
            }

            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            SwingUtility.paintRoundedBorder(g, this, borderColor, radius, borderThickness);
        }

        public void setBorderColor(Color color) {
            this.borderColor = color;
            repaint();
        }

        public void setBackgroundColor(Color color) {
            this.backgroundColor = color;
            repaint();
        }
    }

    public static RoundedPasswordField createRoundedPasswordField(int columns, int textSize, int radius, Color background, Color borderColor, int borderThickness) {
        return new RoundedPasswordField(columns, textSize, radius, background, borderColor, borderThickness);
    }

    public static abstract class RoundedButton extends JButton {
        public abstract void setBorderColor(Color color);

        public abstract Color getBorderColor();
    }

    public static RoundedButton createRoundedButton(String text, int fontSize, int radius, int width, int height, Color background, Color foreground, Color borderColor, int borderThickness) {
        return new RoundedButton() {
            private Color dynamicBorderColor = borderColor;

            {
                this.setText(text);
                this.setFont(new Font("Arial", Font.BOLD, fontSize));
                this.setPreferredSize(new Dimension(width, height));
                this.setForeground(foreground);
                this.setBackground(background);
                this.setOpaque(false);
                this.setContentAreaFilled(false);
                this.setFocusPainted(false);
                this.setFocusable(false);

                SwingUtility.setHandCursorOnHover(this);
            }

            @Override
            protected void paintComponent(Graphics g) {
                paintRoundedBackground(g, this, getBackground(), radius);
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                paintRoundedBorder(g, this, dynamicBorderColor, radius, borderThickness);
            }

            @Override
            public void setBorderColor(Color color) {
                this.dynamicBorderColor = color;
                repaint();
            }

            @Override
            public Color getBorderColor() {
                return dynamicBorderColor;
            }
        };
    }
    private static void paintRoundedBackground(Graphics g, JComponent comp, Color color, int radius) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillRoundRect(0, 0, comp.getWidth(), comp.getHeight(), radius, radius);
        g2.dispose();
    }

    private static void paintRoundedBorder(Graphics g, JComponent comp, Color color, int radius, int thickness) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));

        int offset = thickness / 2;
        int width = comp.getWidth() - thickness;
        int height = comp.getHeight() - thickness;

        g2.drawRoundRect(offset, offset, width, height, radius, radius);
        g2.dispose();
    }
    public static void defaultAlert(JComponent component) {
        Toolkit.getDefaultToolkit().beep();
        UIManager.getLookAndFeel().provideErrorFeedback(component);
    }
    private static final Color GRADIENT_START = new Color(0, 183, 255);
    private static final Color GRADIENT_END = new Color(102, 255, 102);

    public static GradientPaint createHorizontalGradient(int width) {
        return new GradientPaint(0, 0, GRADIENT_START, width, 0, GRADIENT_END);
    }
    public static class GradientButton extends JButton {
        private boolean isActive = false;
        private final Color plainColor;
        private boolean persistentActive = false;
        private int cornerRadius = 0;

        public void setCornerRadius(int radius) {
            this.cornerRadius = radius;
            repaint();
        }

        public void setActive(boolean active) {
            this.isActive = active;
            repaint();
        }

        public boolean isActive() {
            return isActive;
        }

        public GradientButton(Color plainColor, String text, int fontSize, Color foreground, String iconFileName, int iconWidth, int iconHeight, int iconTextGap, int width, int height) {
            super(text);
            this.plainColor = plainColor;

            this.setPreferredSize(new Dimension(width, height));
            this.setContentAreaFilled(false);
            this.setFocusPainted(false);
            this.setBorderPainted(false);
            this.setForeground(foreground);
            this.setFont(new Font("Arial", Font.BOLD, fontSize));
            this.setHorizontalAlignment(SwingConstants.LEFT);
            this.setHorizontalTextPosition(SwingConstants.RIGHT);
            this.setIconTextGap(iconTextGap);
            this.setIcon(SwingUtility.loadScalableImageIcon(iconFileName, iconWidth, iconHeight));
            this.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
            this.setMargin(new Insets(10, 20, 10, 20));

            SwingUtility.setHandCursorOnHover(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            Shape roundedRect = new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius);

            if (isActive) {
                GradientPaint gradient = SwingUtility.createHorizontalGradient(width);
                g2.setPaint(gradient);
            } else {
                g2.setPaint(plainColor);
            }

            g2.fill(roundedRect);
            g2.dispose();

            super.paintComponent(g);
        }
        public void setPersistentActive(boolean persistent) {
            this.persistentActive = persistent;
        }

        public boolean isPersistentActive() {
            return persistentActive;
        }
    }
    public static void setActiveButton(GradientButton activeButton, List<GradientButton> allButtons) {
        for (GradientButton btn : allButtons) {
            if (!btn.isPersistentActive()) {
                btn.setActive(btn == activeButton);
            }
        }
    }
    private static void setHandCursorOnHover(AbstractButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setCursor(Cursor.getDefaultCursor());
            }
        });
    }
    public static class GradientPanel extends JPanel {
        private final int preferredHeight;

        public GradientPanel(int preferredHeight) {
            this.preferredHeight = preferredHeight;
            this.setOpaque(false);
            this.setLayout(new BorderLayout());
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, preferredHeight);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gradient = SwingUtility.createHorizontalGradient(getWidth());
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();

            super.paintComponent(g);
        }
    }
    public static class RoundedComboBoxUI extends BasicComboBoxUI {
        private final int cornerRadius = 15;
        private final Font itemFont = new Font("Arial", Font.PLAIN, 20);
        private final int itemHeight = 40;

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            comboBox.setOpaque(false);
            comboBox.setBackground(AppColors.GRAY);
            comboBox.setBorder(new EmptyBorder(5, 10, 5, 10));
            comboBox.setFont(itemFont);
            comboBox.setRenderer(new DefaultListCellRenderer() {
                private final Insets padding = new Insets(0, 10, 0, 10);
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setFont(itemFont);
                    label.setOpaque(true);
                    label.setBorder(BorderFactory.createEmptyBorder(padding.top, padding.left, padding.bottom, padding.right));
                    label.setPreferredSize(new Dimension(label.getPreferredSize().width, itemHeight));
                    return label;
                }
            });

            if (comboBox.isEditable()) {
                Component editor = comboBox.getEditor().getEditorComponent();
                if (editor instanceof JComponent) {
                    ((JComponent) editor).setOpaque(false);
                }
            }

            comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, itemHeight));
        }
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(AppColors.GRAY);
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), cornerRadius, cornerRadius);

            super.paint(g, c);
            g2.dispose();
        }

        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton("▼");
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setForeground(Color.BLACK);
            button.setPreferredSize(new Dimension(30, itemHeight));
            return button;
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox);
            popup.getList().setFont(itemFont);
            popup.getList().setFixedCellHeight(itemHeight);
            popup.getList().setSelectionBackground(AppColors.PRIMARY);
            popup.getList().setSelectionForeground(Color.WHITE);
            return popup;
        }
    }

}