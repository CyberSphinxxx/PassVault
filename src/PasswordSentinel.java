import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;

public class PasswordSentinel extends JFrame {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    private static final File STORAGE_FILE = new File("passwords.dat");

    private static final Color THEME_COLOR = new Color(230, 240, 255); // Light blue
    private static final Color ACCENT_COLOR = new Color(70, 130, 180); // Steel blue

    private JTextField passwordField;
    private JSlider lengthSlider;
    private JLabel lengthValueLabel;
    private JCheckBox includeUppercase, includeLowercase, includeNumbers, includeSymbols;
    private JProgressBar strengthIndicator;
    private List<PasswordEntry> savedPasswords;
    private DefaultTableModel tableModel;
    private JTable savedPasswordsTable;

    public PasswordSentinel() {
        setTitle("Password Sentinel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(THEME_COLOR);

        savedPasswords = new ArrayList<>();
        tableModel = new DefaultTableModel(new String[]{"Label", "Username", "Password"}, 0);

        initializeUI();
        loadPasswords();

        setVisible(true);
    }

    private void initializeUI() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(THEME_COLOR);
        tabbedPane.setForeground(ACCENT_COLOR);
        tabbedPane.addTab("Generator", createGeneratorPanel());
        tabbedPane.addTab("Saved Passwords", createSavedPasswordsPanel());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createGeneratorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(THEME_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(THEME_COLOR);
        passwordField = new JTextField(20);
        passwordField.setEditable(false);
        passwordField.setFont(new Font("Monospaced", Font.PLAIN, 16));
        passwordField.setBackground(Color.WHITE);
        topPanel.add(passwordField, BorderLayout.CENTER);

        JButton copyButton = createStyledButton("Copy");
        copyButton.addActionListener(e -> copyPassword());
        topPanel.add(copyButton, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(THEME_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        JLabel lengthLabel = new JLabel("Password Length:");
        lengthLabel.setForeground(ACCENT_COLOR);
        centerPanel.add(lengthLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        lengthSlider = new JSlider(6, 30, 12);
        lengthSlider.setBackground(THEME_COLOR);
        lengthSlider.setForeground(ACCENT_COLOR);
        centerPanel.add(lengthSlider, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        lengthValueLabel = new JLabel("12");
        lengthValueLabel.setForeground(ACCENT_COLOR);
        centerPanel.add(lengthValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        includeUppercase = createStyledCheckBox("Uppercase", true);
        centerPanel.add(includeUppercase, gbc);

        gbc.gridy++;
        includeLowercase = createStyledCheckBox("Lowercase", true);
        centerPanel.add(includeLowercase, gbc);

        gbc.gridy++;
        includeNumbers = createStyledCheckBox("Numbers", true);
        centerPanel.add(includeNumbers, gbc);

        gbc.gridy++;
        includeSymbols = createStyledCheckBox("Symbols", true);
        centerPanel.add(includeSymbols, gbc);

        gbc.gridy++;
        gbc.gridwidth = 3;
        strengthIndicator = new JProgressBar(0, 5);
        strengthIndicator.setStringPainted(true);
        strengthIndicator.setString("Weak");
        centerPanel.add(strengthIndicator, gbc);

        panel.add(centerPanel, BorderLayout.CENTER);

        JButton generateButton = createStyledButton("Generate Password");
        generateButton.addActionListener(e -> generatePassword());
        panel.add(generateButton, BorderLayout.SOUTH);

        setupListeners();

        return panel;
    }

    private JPanel createSavedPasswordsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(THEME_COLOR);

        tableModel = new DefaultTableModel(new String[]{"Label", "Username", "Password", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only the "Actions" column is editable
            }
        };
        savedPasswordsTable = new JTable(tableModel);
        savedPasswordsTable.setBackground(Color.WHITE);
        savedPasswordsTable.setForeground(ACCENT_COLOR);
        savedPasswordsTable.setSelectionBackground(ACCENT_COLOR);
        savedPasswordsTable.setSelectionForeground(Color.WHITE);
        savedPasswordsTable.setGridColor(ACCENT_COLOR);

        // Set up the "Reveal" button column
        savedPasswordsTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        savedPasswordsTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(savedPasswordsTable);
        scrollPane.getViewport().setBackground(THEME_COLOR);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(THEME_COLOR);
        JButton saveButton = createStyledButton("Save Generated Password");
        saveButton.addActionListener(e -> savePassword());
        buttonPanel.add(saveButton);

        JButton deleteButton = createStyledButton("Delete Selected");
        deleteButton.addActionListener(e -> deletePassword());
        buttonPanel.add(deleteButton);

        JButton addManualButton = createStyledButton("Add Manual Entry");
        addManualButton.addActionListener(e -> addManualEntry());
        buttonPanel.add(addManualButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    private JCheckBox createStyledCheckBox(String text, boolean selected) {
        JCheckBox checkBox = new JCheckBox(text, selected);
        checkBox.setBackground(THEME_COLOR);
        checkBox.setForeground(ACCENT_COLOR);
        return checkBox;
    }

    private void setupListeners() {
        lengthSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int length = lengthSlider.getValue();
                lengthValueLabel.setText(String.valueOf(length));
                generatePassword();
            }
        });

        ActionListener checkboxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePassword();
            }
        };

        includeUppercase.addActionListener(checkboxListener);
        includeLowercase.addActionListener(checkboxListener);
        includeNumbers.addActionListener(checkboxListener);
        includeSymbols.addActionListener(checkboxListener);
    }

    private void generatePassword() {
        int length = lengthSlider.getValue();
        StringBuilder charSet = new StringBuilder();
        if (includeUppercase.isSelected()) charSet.append(UPPERCASE);
        if (includeLowercase.isSelected()) charSet.append(LOWERCASE);
        if (includeNumbers.isSelected()) charSet.append(NUMBERS);
        if (includeSymbols.isSelected()) charSet.append(SYMBOLS);

        if (charSet.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one character type.");
            return;
        }

        String password = generateRandomPassword(charSet.toString(), length);
        passwordField.setText(password);
        int strength = calculatePasswordStrength(password);
        updateStrengthMeter(strengthIndicator, strength);
    }

    private String generateRandomPassword(String charSet, int length) {
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            password.append(charSet.charAt(random.nextInt(charSet.length())));
        }
        return password.toString();
    }

    private int calculatePasswordStrength(String password) {
        int strength = 0;
        if (password.length() >= 8 && password.length() <= 12) strength += 1;
        else if (password.length() > 12) strength += 2;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*\\d.*")) strength++;
        if (password.matches(".*[^A-Za-z0-9].*")) strength++;
        return strength;
    }

    private void updateStrengthMeter(JProgressBar meter, int strength) {
        meter.setValue(strength);
        String strengthText;
        Color color;

        if (strength <= 2) {
            strengthText = "Weak";
            color = new Color(255, 87, 87); // Light red
        } else if (strength <= 4) {
            strengthText = "Medium";
            color = new Color(255, 206, 86); // Light yellow
        } else {
            strengthText = "Strong";
            color = new Color(97, 255, 66); // Light green
        }

        meter.setString(strengthText);
        meter.setForeground(color);
    }

    private void copyPassword() {
        String password = passwordField.getText();
        if (!password.isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(password), null);
            JOptionPane.showMessageDialog(this, "Password copied to clipboard!");
        }
    }

    private void savePassword() {
        String label = JOptionPane.showInputDialog(this, "Enter label:");
        String username = JOptionPane.showInputDialog(this, "Enter username (optional):");
        String password = passwordField.getText();
        if (label != null && !label.isEmpty() && !password.isEmpty()) {
            savedPasswords.add(new PasswordEntry(label, username, password));
            tableModel.addRow(new Object[]{label, username, "********", "Reveal"});
            JOptionPane.showMessageDialog(this, "Password saved:\nLabel: " + label + "\nUsername: " + username + "\nPassword: " + password);
            savePasswords();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a label and generate a password.");
        }
    }

    private void deletePassword() {
        int selectedRow = savedPasswordsTable.getSelectedRow();
        if (selectedRow != -1) {
            savedPasswords.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            savePasswords();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a password to delete.");
        }
    }

    private void addManualEntry() {
        JTextField labelField = new JTextField();
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JProgressBar strengthMeter = new JProgressBar(0, 5);
        strengthMeter.setValue(0);
        strengthMeter.setStringPainted(true);
        strengthMeter.setString("Weak");

        passField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateStrength();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateStrength();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateStrength();
            }

            private void updateStrength() {
                String password = new String(passField.getPassword());
                int strength = calculatePasswordStrength(password);
                updateStrengthMeter(strengthMeter, strength);
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Label:"));
        panel.add(labelField);
        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(strengthMeter);

        int option = JOptionPane.showConfirmDialog(this, panel, "Add Manual Entry", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String label = labelField.getText();
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (!label.isEmpty() && !pass.isEmpty()) {
                savedPasswords.add(new PasswordEntry(label, user, pass));
                tableModel.addRow(new Object[]{label, user, "********"});
                savePasswords();
            } else {
                JOptionPane.showMessageDialog(this, "Label and password are required.");
            }
        }
    }

    private void savePasswords() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(STORAGE_FILE))) {
            List<String> encryptedPasswords = new ArrayList<>();
            for (PasswordEntry entry : savedPasswords) {
                String serializedEntry = entry.label + "," + entry.username + "," + entry.password;
                String encryptedEntry = EncryptionUtil.encrypt(serializedEntry);
                encryptedPasswords.add(encryptedEntry);
            }
            out.writeObject(encryptedPasswords);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving passwords: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadPasswords() {
        if (!STORAGE_FILE.exists()) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(STORAGE_FILE))) {
            List<String> encryptedPasswords = (List<String>) in.readObject();
            for (String encryptedEntry : encryptedPasswords) {
                try {
                    String decryptedEntry = EncryptionUtil.decrypt(encryptedEntry);
                    String[] parts = decryptedEntry.split(",", 3);
                    if (parts.length == 3) {
                        savedPasswords.add(new PasswordEntry(parts[0], parts[1], parts[2]));
                        tableModel.addRow(new Object[]{parts[0], parts[1], "********", "Reveal"});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error decrypting password: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading passwords: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void togglePasswordVisibility(int row) {
        if (row != -1) {
            String currentValue = (String) tableModel.getValueAt(row, 2);
            String actualPassword = savedPasswords.get(row).password;
            tableModel.setValueAt(currentValue.equals("********") ? actualPassword : "********", row, 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PasswordSentinel();
            }
        });
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

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                togglePasswordVisibility(savedPasswordsTable.getSelectedRow());
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
}

class PasswordEntry implements Serializable {
    String label;
    String username;
    String password;

    public PasswordEntry(String label, String username, String password) {
        this.label = label;
        this.username = username;
        this.password = password;
    }
}

class EncryptionUtil {
    // Placeholder for encryption and decryption methods
    public static String encrypt(String data) {
        // Implement encryption logic here
        return data;
    }

    public static String decrypt(String encryptedData) {
        // Implement decryption logic here
        return encryptedData;
    }
}