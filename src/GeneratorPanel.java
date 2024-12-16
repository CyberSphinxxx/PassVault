import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.datatransfer.StringSelection;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.border.CompoundBorder;

public class GeneratorPanel extends JPanel {
    private JTextField passwordField;
    private JSlider lengthSlider;
    private JLabel lengthValueLabel;
    private JCheckBox includeUppercase, includeLowercase, includeNumbers, includeSymbols;
    private JProgressBar strengthIndicator;
    private PasswordManager passwordManager;
    private Color successColor = new Color(34, 197, 94);
    private Color warningColor = new Color(234, 179, 8);
    private Color errorColor = new Color(239, 68, 68);
    private Color textColor = new Color(55, 65, 81);

    public GeneratorPanel(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
        setLayout(new BorderLayout(30, 30));
        setBorder(new EmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);

        initializeComponents();
    }

    private void initializeComponents() {
        // Main content panel with card-like styling
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(30, 30, 30, 30)
        ));

        // Title
        JLabel titleLabel = new JLabel("Generate Secure Password");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Password display and copy section
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JTextField(20);
        passwordField.setEditable(false);
        passwordField.setFont(new Font("Monospaced", Font.PLAIN, 18));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        JButton copyButton = createStyledButton("Copy");
        copyButton.addActionListener(e -> copyPassword());
        passwordPanel.add(copyButton, BorderLayout.EAST);

        mainPanel.add(passwordPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Password length section
        JPanel lengthPanel = new JPanel(new BorderLayout(10, 5));
        lengthPanel.setBackground(Color.WHITE);
        lengthPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        lengthPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lengthLabel = new JLabel("Password Length:");
        lengthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        lengthLabel.setForeground(textColor);
        lengthPanel.add(lengthLabel, BorderLayout.NORTH);

        lengthSlider = new JSlider(6, 30, 12);
        lengthSlider.setBackground(Color.WHITE);
        lengthSlider.setForeground(textColor);
        lengthPanel.add(lengthSlider, BorderLayout.CENTER);

        lengthValueLabel = new JLabel("12");
        lengthValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        lengthValueLabel.setForeground(textColor);
        lengthPanel.add(lengthValueLabel, BorderLayout.EAST);

        mainPanel.add(lengthPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Character types section
        JPanel typesPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        typesPanel.setBackground(Color.WHITE);
        typesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        typesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        includeUppercase = createStyledCheckBox("Uppercase (A-Z)", true);
        includeLowercase = createStyledCheckBox("Lowercase (a-z)", true);
        includeNumbers = createStyledCheckBox("Numbers (0-9)", true);
        includeSymbols = createStyledCheckBox("Symbols (!@#$%^&*)", true);

        typesPanel.add(includeUppercase);
        typesPanel.add(includeLowercase);
        typesPanel.add(includeNumbers);
        typesPanel.add(includeSymbols);

        mainPanel.add(typesPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Strength indicator
        JPanel strengthPanel = new JPanel(new BorderLayout(10, 5));
        strengthPanel.setBackground(Color.WHITE);
        strengthPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        strengthPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel strengthLabel = new JLabel("Password Strength:");
        strengthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        strengthLabel.setForeground(textColor);
        strengthPanel.add(strengthLabel, BorderLayout.NORTH);

        strengthIndicator = new JProgressBar(0, 5);
        strengthIndicator.setStringPainted(true);
        strengthIndicator.setString("Weak");
        strengthIndicator.setPreferredSize(new Dimension(300, 30));
        strengthPanel.add(strengthIndicator, BorderLayout.CENTER);

        mainPanel.add(strengthPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton generateButton = createStyledButton("Generate Password");
        generateButton.addActionListener(e -> generatePassword());
        buttonPanel.add(generateButton);

        JButton saveButton = createStyledButton("Save Password");
        saveButton.addActionListener(e -> savePassword());
        buttonPanel.add(saveButton);

        mainPanel.add(buttonPanel);

        // Add the main panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        setupListeners();
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
        String password = PasswordGenerator.generatePassword(length, includeUppercase.isSelected(),
                includeLowercase.isSelected(), includeNumbers.isSelected(), includeSymbols.isSelected());
        passwordField.setText(password);
        int strength = PasswordStrengthCalculator.calculateStrength(password);
        updateStrengthMeter(strengthIndicator, strength);
    }

    private void copyPassword() {
        String password = passwordField.getText();
        if (!password.isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(password), null);
            JOptionPane.showMessageDialog(this, "Password copied to clipboard!");
        }
    }

    private void savePassword() {
        String password = passwordField.getText();
        if (!password.isEmpty()) {
            SavePasswordDialog.showDialog(this, passwordManager, password, "", "", -1);
        } else {
            JOptionPane.showMessageDialog(this, "Please generate a password first.");
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(59, 130, 246));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 40)); //buttons sizes for gen password panel
        return button;
    }

    private JCheckBox createStyledCheckBox(String text, boolean selected) {
        JCheckBox checkBox = new JCheckBox(text, selected);
        checkBox.setBackground(Color.WHITE);
        checkBox.setForeground(textColor);
        checkBox.setFont(new Font("Arial", Font.PLAIN, 14));
        return checkBox;
    }

    private void updateStrengthMeter(JProgressBar meter, int strength) {
        meter.setValue(strength);
        String strengthText;
        Color color;

        if (strength <= 2) {
            strengthText = "Weak";
            color = errorColor;
        } else if (strength <= 4) {
            strengthText = "Medium";
            color = warningColor;
        } else {
            strengthText = "Strong";
            color = successColor;
        }

        meter.setString(strengthText);
        meter.setForeground(color);
    }
}

