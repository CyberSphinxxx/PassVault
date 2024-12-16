import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordSentinel extends JFrame {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    private static final File STORAGE_FILE = new File("passwords.dat");

    private static final Color THEME_COLOR = new Color(240, 240, 250); // Light lavender
    private static final Color ACCENT_COLOR = new Color(100, 100, 200); // Medium blue
    private static final Color TEXT_COLOR = new Color(50, 50, 50); // Dark gray

    private JTextField passwordField;
    private JSlider lengthSlider;
    private JLabel lengthValueLabel;
    private JCheckBox includeUppercase, includeLowercase, includeNumbers, includeSymbols;
    private JProgressBar strengthIndicator;
    private List<PasswordEntry> savedPasswords;
    private DefaultTableModel tableModel;
    private JTable savedPasswordsTable;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public PasswordSentinel() {
        setTitle("Password Sentinel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());
        getContentPane().setBackground(THEME_COLOR);

        savedPasswords = new ArrayList<>();
        tableModel = new DefaultTableModel(new String[]{"Label", "Username", "Password", "Actions"}, 0);

        initializeUI();
        loadPasswords();

        setVisible(true);
    }

    private void initializeUI() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(THEME_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.addTab("Generator", createGeneratorPanel());
        tabbedPane.addTab("Saved Passwords", createSavedPasswordsPanel());
        tabbedPane.addTab("Password Strength Checker", createPasswordStrengthCheckerPanel());
        add(tabbedPane, BorderLayout.CENTER);

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(ACCENT_COLOR);
        panel.setPreferredSize(new Dimension(getWidth(), 60));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));

        JLabel titleLabel = new JLabel("Password Sentinel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(ACCENT_COLOR);
        panel.setPreferredSize(new Dimension(getWidth(), 30));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

        JLabel copyrightLabel = new JLabel("© 2023 Password Sentinel. All rights reserved.");
        copyrightLabel.setForeground(Color.WHITE);
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(copyrightLabel);

        return panel;
    }

    private JPanel createGeneratorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(THEME_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(THEME_COLOR);
        passwordField = new JTextField(20);
        passwordField.setEditable(false);
        passwordField.setFont(new Font("Monospaced", Font.PLAIN, 18));
        passwordField.setBackground(Color.WHITE);
        topPanel.add(passwordField, BorderLayout.CENTER);

        JButton copyButton = createStyledButton("Copy");
        copyButton.addActionListener(e -> copyPassword());
        topPanel.add(copyButton, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(THEME_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        JLabel lengthLabel = new JLabel("Password Length:");
        lengthLabel.setForeground(TEXT_COLOR);
        lengthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        centerPanel.add(lengthLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        lengthSlider = new JSlider(6, 30, 12);
        lengthSlider.setBackground(THEME_COLOR);
        lengthSlider.setForeground(TEXT_COLOR);
        centerPanel.add(lengthSlider, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        lengthValueLabel = new JLabel("12");
        lengthValueLabel.setForeground(TEXT_COLOR);
        lengthValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        strengthIndicator = new JProgressBar(0, 5);
        strengthIndicator.setStringPainted(true);
        strengthIndicator.setString("Weak");
        strengthIndicator.setPreferredSize(new Dimension(300, 30));
        centerPanel.add(strengthIndicator, gbc);

        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(THEME_COLOR);

        JButton generateButton = createStyledButton("Generate Password");
        generateButton.addActionListener(e -> generatePassword());
        buttonPanel.add(generateButton);

        JButton saveButton = createStyledButton("Save Password");
        saveButton.addActionListener(e -> savePassword());
        buttonPanel.add(saveButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        setupListeners();

        return panel;
    }

    private JPanel createSavedPasswordsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(THEME_COLOR);

        // Add search field
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(THEME_COLOR);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        panel.add(searchPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Label", "Username", "Password", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only the "Actions" column is editable
            }
        };
        savedPasswordsTable = new JTable(tableModel);
        savedPasswordsTable.setBackground(Color.WHITE);
        savedPasswordsTable.setForeground(TEXT_COLOR);
        savedPasswordsTable.setSelectionBackground(ACCENT_COLOR);
        savedPasswordsTable.setSelectionForeground(Color.WHITE);
        savedPasswordsTable.setGridColor(ACCENT_COLOR);
        savedPasswordsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        savedPasswordsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Set up the "Reveal" button column
        savedPasswordsTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        savedPasswordsTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Set up the sorter for filtering
        sorter = new TableRowSorter<>(tableModel);
        savedPasswordsTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(savedPasswordsTable);
        scrollPane.getViewport().setBackground(THEME_COLOR);
        panel.add(scrollPane, BorderLayout.CENTER);

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
        buttonPanel.setBackground(THEME_COLOR);

        JButton deleteButton = createStyledButton("Delete Selected");
        deleteButton.addActionListener(e -> deletePassword());
        buttonPanel.add(deleteButton);

        JButton editButton = createStyledButton("Edit Password");
        editButton.addActionListener(e -> editPassword());
        buttonPanel.add(editButton);

        JButton addManualButton = createStyledButton("Add Manual Entry");
        addManualButton.addActionListener(e -> addManualEntry());
        buttonPanel.add(addManualButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String label = entry.getStringValue(0).toLowerCase();
                String username = entry.getStringValue(1).toLowerCase();
                return label.contains(searchText) || username.contains(searchText);
            }
        });
    }

    private JPanel createPasswordStrengthCheckerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(THEME_COLOR);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(THEME_COLOR);

        JTextField passwordField = new JTextField(20);
        passwordField.setFont(new Font("Monospaced", Font.PLAIN, 16));
        inputPanel.add(passwordField, BorderLayout.CENTER);

        JLabel strengthLabel = new JLabel("Password Strength: ");
        strengthLabel.setForeground(TEXT_COLOR);
        strengthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(strengthLabel, BorderLayout.WEST);

        panel.add(inputPanel, BorderLayout.NORTH);

        JProgressBar strengthIndicator = new JProgressBar(0, 5);
        strengthIndicator.setStringPainted(true);
        strengthIndicator.setString("Weak");
        strengthIndicator.setPreferredSize(new Dimension(300, 30));
        JPanel strengthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        strengthPanel.setBackground(THEME_COLOR);
        strengthPanel.add(strengthIndicator);
        panel.add(strengthPanel, BorderLayout.CENTER);

        passwordField.getDocument().addDocumentListener(new DocumentListener() {
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
                String password = passwordField.getText();
                int strength = calculatePasswordStrength(password);
                updateStrengthMeter(strengthIndicator, strength);
            }
        });

        // Add password strength tips
        JTextArea tipsArea = new JTextArea();
        tipsArea.setEditable(false);
        tipsArea.setBackground(THEME_COLOR);
        tipsArea.setForeground(TEXT_COLOR);
        tipsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        tipsArea.setText(
            "Password Strength Requirements:\n" +
            "• Minimum 12 characters length\n" +
            "• Mix of uppercase and lowercase letters\n" +
            "• Include numbers and special characters\n" +
            "• Avoid personal information\n" +
            "• Use unique passwords for each account\n" +
            "• Consider using passphrases\n\n" +
            "Security Best Practices:\n" +
            "• Regular Updates: Update passwords every 3-6 months\n" +
            "• Unique Passwords: Never reuse passwords across accounts\n" +
            "• Password Manager: Use a secure password manager\n" +
            "• Privacy Settings: Regularly review social media privacy"
        );
        JScrollPane tipsScrollPane = new JScrollPane(tipsArea);
        tipsScrollPane.setPreferredSize(new Dimension(300, 200));
        panel.add(tipsScrollPane, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private JCheckBox createStyledCheckBox(String text, boolean selected) {
        JCheckBox checkBox = new JCheckBox(text, selected);
        checkBox.setBackground(THEME_COLOR);
        checkBox.setForeground(TEXT_COLOR);
        checkBox.setFont(new Font("Arial", Font.PLAIN, 14));
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
        showSavePasswordDialog(passwordField.getText(), "", "");
    }

    private void showSavePasswordDialog(String initialPassword, String initialLabel, String initialUsername) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        JTextField labelField = new JTextField(initialLabel);
        JTextField usernameField = new JTextField(initialUsername);
        JPasswordField passwordField = new JPasswordField(initialPassword);
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        JProgressBar strengthMeter = new JProgressBar(0, 5);
        strengthMeter.setStringPainted(true);

        panel.add(new JLabel("Label:"));
        panel.add(labelField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(showPasswordCheckBox);
        panel.add(strengthMeter);

        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        passwordField.getDocument().addDocumentListener(new DocumentListener() {
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
                String password = new String(passwordField.getPassword());
                int strength = calculatePasswordStrength(password);
                updateStrengthMeter(strengthMeter, strength);
            }
        });

        updateStrengthMeter(strengthMeter, calculatePasswordStrength(initialPassword));

        int result = JOptionPane.showConfirmDialog(this, panel, "Save Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String label = labelField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (!label.isEmpty() && !password.isEmpty()) {
                savedPasswords.add(new PasswordEntry(label, username, password));
                tableModel.addRow(new Object[]{label, username, "********", "Reveal"});
                savePasswords();
                JOptionPane.showMessageDialog(this, "Password saved successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Label and password are required.");
            }
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

    private void editPassword() {
        int selectedRow = savedPasswordsTable.getSelectedRow();
        if (selectedRow != -1) {
            PasswordEntry entry = savedPasswords.get(selectedRow);
            showSavePasswordDialog(entry.password, entry.label, entry.username);
            tableModel.setValueAt(entry.label, selectedRow, 0);
            tableModel.setValueAt(entry.username, selectedRow, 1);
            tableModel.setValueAt("********", selectedRow, 2);
            savePasswords();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a password to edit.");
        }
    }

    private void addManualEntry() {
        showSavePasswordDialog("", "", "");
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

    private void togglePasswordVisibility(int viewRow) {
        if (viewRow != -1) {
            int modelRow = savedPasswordsTable.convertRowIndexToModel(viewRow);
            String currentValue = (String) tableModel.getValueAt(modelRow, 2);
            String actualPassword = savedPasswords.get(modelRow).password;
            tableModel.setValueAt(currentValue.equals("********") ? actualPassword : "********", modelRow, 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showLoginDialog();
            }
        });
    }

    private static void showLoginDialog() {
        JDialog loginDialog = new JDialog();
        loginDialog.setTitle("Login");
        loginDialog.setSize(400, 250);
        loginDialog.setLayout(new BorderLayout());
        loginDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        loginDialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(THEME_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Password Sentinel Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(ACCENT_COLOR);
        panel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(TEXT_COLOR);
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        JTextField userField = new JTextField(20);
        panel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(TEXT_COLOR);
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passField = new JPasswordField(20);
        panel.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(ACCENT_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        panel.add(loginButton, gbc);

        loginDialog.add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (validateLogin(username, password)) {
                loginDialog.dispose();
                new PasswordSentinel();
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginDialog.setVisible(true);
    }

    private static boolean validateLogin(String username, String password) {
        // Replace with actual validation logic
        return "admin".equals(username) && "admin".equals(password);
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
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "1234567890123456".getBytes();

    public static String encrypt(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}