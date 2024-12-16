import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

public class SavedPasswordsPanel extends JPanel {
    private PasswordManager passwordManager;
    private DefaultTableModel tableModel;
    private JTable savedPasswordsTable;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public SavedPasswordsPanel(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(PasswordSentinel.THEME_COLOR);

        initializeComponents();
        passwordManager.addChangeListener(this::refreshTable);
    }

    private void initializeComponents() {
        // Add search field
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(PasswordSentinel.THEME_COLOR);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        add(searchPanel, BorderLayout.NORTH);

        // Create table
        tableModel = new DefaultTableModel(new String[]{"Label", "Username", "Password", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        savedPasswordsTable = new JTable(tableModel);
        savedPasswordsTable.setBackground(Color.WHITE);
        savedPasswordsTable.setForeground(PasswordSentinel.TEXT_COLOR);
        savedPasswordsTable.setSelectionBackground(PasswordSentinel.ACCENT_COLOR);
        savedPasswordsTable.setSelectionForeground(Color.WHITE);
        savedPasswordsTable.setGridColor(PasswordSentinel.ACCENT_COLOR);
        savedPasswordsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        savedPasswordsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        savedPasswordsTable.setRowHeight(30); // Increase row height for better navigation
        savedPasswordsTable.setIntercellSpacing(new Dimension(10, 10)); // Add more space between cells

        // Set up the "Reveal" button column
        savedPasswordsTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        savedPasswordsTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Set up the sorter for filtering
        sorter = new TableRowSorter<>(tableModel);
        savedPasswordsTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(savedPasswordsTable);
        scrollPane.getViewport().setBackground(PasswordSentinel.THEME_COLOR);
        add(scrollPane, BorderLayout.CENTER);

        // Add search functionality
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(PasswordSentinel.THEME_COLOR);

        JButton deleteButton = createStyledButton("Delete Selected");
        deleteButton.addActionListener(e -> deletePassword());
        buttonPanel.add(deleteButton);

        JButton editButton = createStyledButton("Edit Password");
        editButton.addActionListener(e -> editPassword());
        buttonPanel.add(editButton);

        JButton addManualButton = createStyledButton("Add Manual Entry");
        addManualButton.addActionListener(e -> addManualEntry());
        buttonPanel.add(addManualButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Add double-click listener for editing
        savedPasswordsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int column = savedPasswordsTable.columnAtPoint(e.getPoint());
                if (e.getClickCount() == 2 && column != 3) { // Check if it's a double-click and not on the action column
                    editPassword();
                }
            }
        });

        refreshTable();
    }

    private void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
    }

    private void deletePassword() {
        int selectedRow = savedPasswordsTable.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = savedPasswordsTable.convertRowIndexToModel(selectedRow);
            String label = (String) tableModel.getValueAt(modelRow, 0);
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the account '" + label + "'?\nThis action cannot be undone.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                passwordManager.deletePassword(modelRow);
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a password to delete.");
        }
    }

    private void editPassword() {
        int selectedRow = savedPasswordsTable.getSelectedRow();
        int selectedColumn = savedPasswordsTable.getSelectedColumn();
        if (selectedRow != -1 && selectedColumn != 3) {
            int modelRow = savedPasswordsTable.convertRowIndexToModel(selectedRow);
            PasswordEntry entry = passwordManager.getPasswordEntry(modelRow);
            SavePasswordDialog.showDialog(this, passwordManager, entry.getPassword(), entry.getLabel(), entry.getUsername(), modelRow);
        } else if (selectedColumn == 3) {
            JOptionPane.showMessageDialog(this, "Use the 'Reveal' button to show/hide the password.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a password to edit.");
        }
    }

    private void addManualEntry() {
        SavePasswordDialog.showDialog(this, passwordManager, "", "", "", -1);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PasswordSentinel.ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (PasswordEntry entry : passwordManager.getSavedPasswords()) {
            tableModel.addRow(new Object[]{entry.getLabel(), entry.getUsername(), "********", "Reveal"});
        }
    }

    private void togglePasswordVisibility(int viewRow) {
        if (viewRow != -1) {
            int modelRow = savedPasswordsTable.convertRowIndexToModel(viewRow);
            String currentValue = (String) tableModel.getValueAt(modelRow, 2);
            String actualPassword = passwordManager.getPasswordEntry(modelRow).getPassword();
            tableModel.setValueAt(currentValue.equals("********") ? actualPassword : "********", modelRow, 2);
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                togglePasswordVisibility(row);
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    public void cleanup() {
        passwordManager.removeChangeListener(this::refreshTable);
    }
}