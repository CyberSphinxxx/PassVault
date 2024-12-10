import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PasswordSentinel {
    private JFrame frame;
    private JTextField passwordField;
    private JSlider lengthSlider;
    private JLabel lengthValueLabel;
    private JCheckBox includeUppercase;
    private JCheckBox includeLowercase;
    private JCheckBox includeNumbers;
    private JCheckBox includeSymbols;
    private JLabel strengthIndicator;
    private JLabel strengthImage;
    private List<PasswordEntry> savedPasswords;
    private JTable savedPasswordsTable;
    private DefaultTableModel tableModel;

    private final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private final String NUMBERS = "0123456789";
    private final String SYMBOLS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    private static final File STORAGE_FILE = new File("passwords.dat");

    public PasswordSentinel() {
        savedPasswords = new ArrayList<>();
        tableModel = new DefaultTableModel(new String[]{"Label", "Username", "Password"}, 0);

        frame = new JFrame("Password Sentinel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Load saved passwords from file
        loadPasswords();

        createUI();

        frame.setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel generatorPanel = createGeneratorPanel();
        JPanel savedPanel = createSavedPasswordsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, generatorPanel, savedPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        frame.add(mainPanel, BorderLayout.CENTER);

        lengthSlider.addChangeListener(e -> {
            int length = lengthSlider.getValue();
            lengthValueLabel.setText("Length: " + length);
            generatePassword();
        });

        includeUppercase.addActionListener(e -> generatePassword());
        includeLowercase.addActionListener(e -> generatePassword());
        includeNumbers.addActionListener(e -> generatePassword());
        includeSymbols.addActionListener(e -> generatePassword());

        generatePassword();
    }

    private JPanel createGeneratorPanel() {
        JPanel generatorPanel = new JPanel();
        generatorPanel.setLayout(new GridBagLayout());
        generatorPanel.setBorder(BorderFactory.createTitledBorder("Password Generator"));
        generatorPanel.setBackground(new Color(224, 255, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        generatorPanel.add(new JLabel("Generate Your Password"), gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        passwordField = new JTextField(20);
        passwordField.setEditable(false);
        passwordField.setBackground(Color.WHITE);
        generatorPanel.add(passwordField, gbc);

        gbc.gridx++;
        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(e -> copyPassword());
        generatorPanel.add(copyButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        generatorPanel.add(new JLabel("Password Length:"), gbc);

        gbc.gridx++;
        lengthSlider = new JSlider(6, 30, 12);
        generatorPanel.add(lengthSlider, gbc);

        gbc.gridx++;
        lengthValueLabel = new JLabel("Length: 12");
        generatorPanel.add(lengthValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        includeUppercase = new JCheckBox("Uppercase", true);
        generatorPanel.add(includeUppercase, gbc);

        gbc.gridy++;
        includeLowercase = new JCheckBox("Lowercase", true);
        generatorPanel.add(includeLowercase, gbc);

        gbc.gridy++;
        includeNumbers = new JCheckBox("Numbers", true);
        generatorPanel.add(includeNumbers, gbc);

        gbc.gridy++;
        includeSymbols = new JCheckBox("Symbols", true);
        generatorPanel.add(includeSymbols, gbc);

        gbc.gridy++;
        strengthIndicator = new JLabel("Password Strength: Weak");
        generatorPanel.add(strengthIndicator, gbc);

        gbc.gridy++;
        strengthImage = new JLabel();
        generatorPanel.add(strengthImage, gbc);

        return generatorPanel;
    }

    private JPanel createSavedPasswordsPanel() {
        JPanel savedPanel = new JPanel();
        savedPanel.setLayout(new BorderLayout());
        savedPanel.setBorder(BorderFactory.createTitledBorder("Saved Passwords"));
        savedPanel.setBackground(new Color(240, 255, 240));

        savedPasswordsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(savedPasswordsTable);
        savedPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 255, 240));

        JButton saveButton = new JButton("Save Generated Password");
        saveButton.addActionListener(e -> savePassword());
        buttonPanel.add(saveButton);

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deletePassword());
        buttonPanel.add(deleteButton);

        JButton addManualButton = new JButton("Add Manual Entry");
        addManualButton.addActionListener(e -> addManualEntry());
        buttonPanel.add(addManualButton);

        savedPanel.add(buttonPanel, BorderLayout.SOUTH);

        return savedPanel;
    }

    private void generatePassword() {
        int length = lengthSlider.getValue();
        StringBuilder charSet = new StringBuilder();
        if (includeUppercase.isSelected())
            charSet.append(UPPERCASE);
        if (includeLowercase.isSelected())
            charSet.append(LOWERCASE);
        if (includeNumbers.isSelected())
            charSet.append(NUMBERS);
        if (includeSymbols.isSelected())
            charSet.append(SYMBOLS);

        String password = generateRandomPassword(charSet.toString(), length);
        passwordField.setText(password);
        evaluateStrength(password);
    }

    private String generateRandomPassword(String charSet, int length) {
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            password.append(charSet.charAt(random.nextInt(charSet.length())));
        }
        return password.toString();
    }

    private void evaluateStrength(String password) {
        int strength = 0;

        if (password.length() >= 8 && password.length() <= 12)
            strength += 1;
        else if (password.length() > 12)
            strength += 2;

        if (password.matches(".*[A-Z].*"))
            strength++;
        if (password.matches(".*[a-z].*"))
            strength++;
        if (password.matches(".*\\d.*"))
            strength++;
        if (password.matches(".*[^A-Za-z0-9].*"))
            strength++;

        if (strength <= 2) {
            strengthIndicator.setText("Password Strength: Weak");
            strengthImage.setIcon(new ImageIcon("weak.png"));
        } else if (strength >= 3 && strength <= 4) {
            strengthIndicator.setText("Password Strength: Medium");
            strengthImage.setIcon(new ImageIcon("medium.png"));
        } else if (strength >= 5) {
            strengthIndicator.setText("Password Strength: Strong");
            strengthImage.setIcon(new ImageIcon("strong.png"));
        }
    }

    private void copyPassword() {
        String password = passwordField.getText();
        StringSelection selection = new StringSelection(password);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        JOptionPane.showMessageDialog(frame, "Password copied to clipboard!");
    }

    private void savePassword() {
        String label = JOptionPane.showInputDialog(frame, "Enter label:");
        String password = passwordField.getText();
        if (label != null && !label.isEmpty()) {
            savedPasswords.add(new PasswordEntry(label, "", password));
            tableModel.addRow(new Object[]{label, "", password});
            savePasswords();
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter a label.");
        }
    }

    private void deletePassword() {
        int selectedRow = savedPasswordsTable.getSelectedRow();
        if (selectedRow != -1) {
            savedPasswords.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            savePasswords();
        }
    }

    private void addManualEntry() {
        JTextField labelField = new JTextField();
        JTextField userField = new JTextField();
        JTextField passField = new JTextField();
        Object[] fields = {
                "Label:", labelField,
                "Username:", userField,
                "Password:", passField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Add Manual Entry", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String label = labelField.getText();
            String user = userField.getText();
            String pass = passField.getText();

            if (!label.isEmpty() && !user.isEmpty() && !pass.isEmpty()) {
                savedPasswords.add(new PasswordEntry(label, user, pass));
                tableModel.addRow(new Object[]{label, user, pass});
                savePasswords();
            } else {
                JOptionPane.showMessageDialog(frame, "All fields are required.");
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
            JOptionPane.showMessageDialog(frame, "Error saving passwords: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                String decryptedEntry = EncryptionUtil.decrypt(encryptedEntry);
                String[] parts = decryptedEntry.split(",", 3);
                if (parts.length == 3) {
                    savedPasswords.add(new PasswordEntry(parts[0], parts[1], parts[2]));
                    tableModel.addRow(new Object[]{parts[0], parts[1], parts[2]});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading passwords: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PasswordSentinel::new);
    }
}

class PasswordEntry implements Serializable {
    String label;
    String username;
    String password;

    public PasswordEntry(String label, String password) {
        this.label = label;
        this.username = "";
        this.password = password;
    }

    public PasswordEntry(String label, String username, String password) {
        this.label = label;
        this.username = username;
        this.password = password;
    }
}
