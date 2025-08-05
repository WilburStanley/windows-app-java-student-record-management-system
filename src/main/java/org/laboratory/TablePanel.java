package org.laboratory;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class TablePanel extends JPanel {

    private final DefaultTableModel model;
    final JTable table;
    private final JScrollPane scrollPane;

    public TablePanel(String[] columnNames, int tableWidth, int tableHeight) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        JPanel tableCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        tableCard.setOpaque(false);
        tableCard.setLayout(new BorderLayout());

        model = new DefaultTableModel(columnNames, 0);
        table = createTable();
        JTableHeader header = createTableHeader(table);
        table.setTableHeader(header);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        scrollPane.setPreferredSize(new Dimension(tableWidth, tableHeight));

        tableCard.add(scrollPane, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(tableCard, gbc);

        configureColumnWidths(table);
    }

    public void updateTable(List<Object[]> newData) {
        model.setRowCount(0);
        for (Object[] row : newData) {
            model.addRow(row);
        }
        configureColumnWidths(table);
    }
    public void updateTableData(List<StudentInformationHandler.StudentRecord> students) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (StudentInformationHandler.StudentRecord s : students) {
            model.addRow(new Object[] {
                    s.studentID, s.lastName, s.firstName, s.middleName,
                    s.age, s.gender, s.course, s.yearLevel, s.email
            });
        }
        configureColumnWidths(table);
    }

    private JTable createTable() {
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setRowHeight(40);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        return table;
    }

    private JTableHeader createTableHeader(JTable table) {
        JTableHeader header = new JTableHeader(table.getColumnModel()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = SwingUtility.createHorizontalGradient(getWidth());
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.dispose();

                for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
                    TableColumn column = table.getColumnModel().getColumn(i);
                    Rectangle rect = getHeaderRect(i);
                    TableCellRenderer renderer = column.getHeaderRenderer();
                    if (renderer == null) {
                        renderer = getDefaultRenderer();
                    }
                    Component comp = renderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, -1, i);
                    comp.setBounds(rect);
                    SwingUtilities.paintComponent(g, comp, this, rect);
                }
            }
        };

        header.setPreferredSize(new Dimension(100, 45));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            {
                setOpaque(false);
                setHorizontalAlignment(CENTER);
                setForeground(Color.WHITE);
                setFont(new Font("Arial", Font.BOLD, 20));
                setBorder(BorderFactory.createEmptyBorder());
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                setText(value != null ? value.toString() : "");
                return this;
            }
        });

        return header;
    }
    private void configureColumnWidths(JTable table) {
        TableModel model = table.getModel();
        FontMetrics fm = table.getFontMetrics(table.getFont());

        for (int col = 0; col < model.getColumnCount(); col++) {
            int maxWidth = fm.stringWidth(model.getColumnName(col)) + 30;

            for (int row = 0; row < model.getRowCount(); row++) {
                Object value = model.getValueAt(row, col);
                if (value != null) {
                    int cellWidth = fm.stringWidth(value.toString()) + 30;
                    maxWidth = Math.max(maxWidth, cellWidth);
                }
            }

            TableColumn column = table.getColumnModel().getColumn(col);
            column.setPreferredWidth(maxWidth);
            column.setMinWidth(40);

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            column.setCellRenderer(renderer);
        }
    }

    public void addRow(Object[] rowData) {
        model.addRow(rowData);
        configureColumnWidths(table);
    }
}
