import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
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

    public PasswordSentinel() {
        savedPasswords = new ArrayList<>();
        listModel = new DefaultListModel<>();

        frame = new JFrame("Password Sentinel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.DARK_GRAY);

        createUI();

        frame.setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Password Generator Panel
        JPanel generatorPanel = new JPanel();
        generatorPanel.setLayout(new GridBagLayout());
        generatorPanel.setBorder(BorderFactory.createTitledBorder("Password Generator"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        generatorPanel.add(new JLabel("Generate Your Password"), gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        passwordField = new JTextField(20);
        passwordField.setEditable(false);
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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(generatorPanel, gbc);

        // Saved Passwords Panel
        JPanel savedPanel = new JPanel();
        savedPanel.setLayout(new BorderLayout());
        savedPanel.setBorder(BorderFactory.createTitledBorder("Saved Passwords"));

        JLabel savedLabel = new JLabel("Saved Passwords");
        savedPanel.add(savedLabel, BorderLayout.NORTH);

        savedPasswordsList = new JList<>(listModel);
        savedPanel.add(new JScrollPane(savedPasswordsList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save Password");
        saveButton.addActionListener(e -> savePassword());
        buttonPanel.add(saveButton);

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deletePassword());
        buttonPanel.add(deleteButton);

        savedPanel.add(buttonPanel, BorderLayout.SOUTH);

        gbc.gridy++;
        mainPanel.add(savedPanel, gbc);

        frame.add(mainPanel, BorderLayout.CENTER);

        // Add listeners
        lengthSlider.addChangeListener(e -> {
            int length = lengthSlider.getValue();
            lengthValueLabel.setText("Length : " + length);
            generatePassword();
        });

        includeUppercase.addActionListener(e -> generatePassword());
        includeLowercase.addActionListener(e -> generatePassword());
        includeNumbers.addActionListener(e -> generatePassword());
        includeSymbols.addActionListener(e -> generatePassword());

        // Generate initial password
        generatePassword();
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
        String email = JOptionPane.showInputDialog(frame, "Enter label:");
        String password = passwordField.getText();
        if (email != null && !email.isEmpty()) {
            savedPasswords.add(new PasswordEntry(email, password));
            listModel.addElement(email + ": " + password);
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter a label.");
        }
    }

    private void deletePassword() {
        int selectedIndex = savedPasswordsList.getSelectedIndex();
        if (selectedIndex != -1) {
            savedPasswords.remove(selectedIndex);
            listModel.remove(selectedIndex);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PasswordSentinel::new);
    }
}

class PasswordEntry {
    String email;
    String password;

    public PasswordEntry(String email, String password) {
        this.email = email;
        this.password = password;
    }
}