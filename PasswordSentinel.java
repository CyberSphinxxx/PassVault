import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

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
    private JList<String> savedPasswordsList;
    private DefaultListModel<String> listModel;

    private final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private final String NUMBERS = "0123456789";
    private final String SYMBOLS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    private static final String STORAGE_FILE = "passwords.dat"; // File to store the passwords

    public PasswordSentinel() {
        savedPasswords = new ArrayList<>();
        listModel = new DefaultListModel<>();

        frame = new JFrame("Password Sentinel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        // Load saved passwords from file
        loadPasswords();

        createUI();

        frame.setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        mainPanel.setBackground(new Color(240, 248, 255)); // Light blue background

        JPanel generatorPanel = createGeneratorPanel();
        JPanel savedPanel = createSavedPasswordsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, generatorPanel, savedPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5); // Split pane proportions
        mainPanel.add(splitPane, BorderLayout.CENTER);

        frame.add(mainPanel, BorderLayout.CENTER);

        // Add listeners
        lengthSlider.addChangeListener(e -> {
            int length = lengthSlider.getValue();
            lengthValueLabel.setText("Length: " + length);
            generatePassword();
        });

        includeUppercase.addActionListener(e -> generatePassword());
        includeLowercase.addActionListener(e -> generatePassword());
        includeNumbers.addActionListener(e -> generatePassword());
        includeSymbols.addActionListener(e -> generatePassword());

        // Generate initial password
        generatePassword();
    }

    private JPanel createGeneratorPanel() {
        JPanel generatorPanel = new JPanel();
        generatorPanel.setLayout(new GridBagLayout());
        generatorPanel.setBorder(BorderFactory.createTitledBorder("Password Generator"));
        generatorPanel.setBackground(new Color(224, 255, 255)); // Light cyan background
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST; // Align components to the left
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        generatorPanel.add(new JLabel("Generate Your Password"), gbc);
    
        gbc.gridwidth = 1;
        gbc.gridy++;
        passwordField = new JTextField(20);
        passwordField.setEditable(false);
        passwordField.setBackground(Color.WHITE); // Background color for text field
        generatorPanel.add(passwordField, gbc);
    
        gbc.gridx++;
        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(e -> copyPassword());
        copyButton.setBackground(new Color(173, 216, 230)); // Light blue button
        generatorPanel.add(copyButton, gbc);
    
        gbc.gridx = 0;
        gbc.gridy++;
        generatorPanel.add(new JLabel("Password Length:"), gbc);
    
        gbc.gridx++;
        lengthSlider = new JSlider(6, 30, 12);
        lengthSlider.setBackground(new Color(224, 255, 255)); // Light cyan background
        generatorPanel.add(lengthSlider, gbc);
    
        gbc.gridx++;
        lengthValueLabel = new JLabel("Length: 12");
        generatorPanel.add(lengthValueLabel, gbc);
    
        // Align checkboxes in a single column
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2; // Span both columns
        includeUppercase = new JCheckBox("Uppercase", true);
        includeUppercase.setBackground(new Color(224, 255, 255)); // Light cyan background
        generatorPanel.add(includeUppercase, gbc);
    
        gbc.gridy++;
        includeLowercase = new JCheckBox("Lowercase", true);
        includeLowercase.setBackground(new Color(224, 255, 255)); // Light cyan background
        generatorPanel.add(includeLowercase, gbc);
    
        gbc.gridy++;
        includeNumbers = new JCheckBox("Numbers", true);
        includeNumbers.setBackground(new Color(224, 255, 255)); // Light cyan background
        generatorPanel.add(includeNumbers, gbc);
    
        gbc.gridy++;
        includeSymbols = new JCheckBox("Symbols", true);
        includeSymbols.setBackground(new Color(224, 255, 255)); // Light cyan background
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
        savedPanel.setBackground(new Color(240, 255, 240)); // Honeydew background

        JLabel savedLabel = new JLabel("Saved Passwords");
        savedLabel.setHorizontalAlignment(JLabel.CENTER); // Center the label
        savedPanel.add(savedLabel, BorderLayout.NORTH);

        savedPasswordsList = new JList<>(listModel);
        savedPanel.add(new JScrollPane(savedPasswordsList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 255, 240)); // Honeydew background

        JButton saveButton = new JButton("Save Generated Password");
        saveButton.addActionListener(e -> savePassword());
        saveButton.setBackground(new Color(144, 238, 144)); // Light green button
        buttonPanel.add(saveButton);

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deletePassword());
        deleteButton.setBackground(new Color(255, 182, 193)); // Light pink button
        buttonPanel.add(deleteButton);

        JButton addManualButton = new JButton("Add Manual Entry");
        addManualButton.addActionListener(e -> addManualEntry());
        addManualButton.setBackground(new Color(173, 216, 230)); // Light blue button
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
            strength++; // Uppercase
        if (password.matches(".*[a-z].*"))
            strength++; // Lowercase
        if (password.matches(".*\\d.*"))
            strength++; // Numbers
        if (password.matches(".*[^A-Za-z0-9].*"))
            strength++; // Symbols

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
            savedPasswords.add(new PasswordEntry(label, password));
            listModel.addElement(label + ": " + password); // Add password in readable format
            savePasswords(); // Save the updated list to file
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter a label.");
        }
    }

    private void deletePassword() {
        int selectedIndex = savedPasswordsList.getSelectedIndex();
        if (selectedIndex != -1) {
            savedPasswords.remove(selectedIndex);
            listModel.remove(selectedIndex);
            savePasswords(); // Save the updated list to file
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
                savedPasswords.add(new PasswordEntry(label, user, pass)); // store full entry
                listModel.addElement(label + " (" + user + "): " + pass); // display in list
                savePasswords(); // Save the updated list to file
            } else {
                JOptionPane.showMessageDialog(frame, "All fields are required.");
            }
        }
    }

    private void savePasswords() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(STORAGE_FILE))) {
            out.writeObject(savedPasswords); // Serialize the list of passwords
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPasswords() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(STORAGE_FILE))) {
            savedPasswords = (List<PasswordEntry>) in.readObject(); // Deserialize the saved passwords
            for (PasswordEntry entry : savedPasswords) {
                listModel.addElement(entry.label + " (" + entry.username + "): " + entry.password);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
