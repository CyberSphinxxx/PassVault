import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class PasswordStrengthCheckerPanel extends JPanel {
    private JTextField passwordField;
    private JProgressBar strengthMeter;
    private JLabel strengthLabel;
    private List<JPanel> requirementPanels;
    private Color successColor = new Color(34, 197, 94);
    private Color warningColor = new Color(234, 179, 8);
    private Color errorColor = new Color(239, 68, 68);
    private Color textColor = new Color(55, 65, 81);

    public PasswordStrengthCheckerPanel() {
        setLayout(new BorderLayout(30, 30));
        setBorder(new EmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);
        requirementPanels = new ArrayList<>();

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
        JLabel titleLabel = new JLabel("Check Password Strength");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Password input section
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Enter your password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(textColor);
        inputPanel.add(passwordLabel);
        inputPanel.add(Box.createVerticalStrut(10));

        // Styled password field
        passwordField = new JTextField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(400, 40));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        inputPanel.add(passwordField);
        inputPanel.add(Box.createVerticalStrut(20));

        // Strength meter section
        JPanel strengthPanel = new JPanel();
        strengthPanel.setLayout(new BoxLayout(strengthPanel, BoxLayout.Y_AXIS));
        strengthPanel.setBackground(Color.WHITE);
        strengthPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        strengthLabel = new JLabel("Password Strength: ");
        strengthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        strengthLabel.setForeground(textColor);
        strengthPanel.add(strengthLabel);
        strengthPanel.add(Box.createVerticalStrut(10));

        strengthMeter = new JProgressBar(0, 5);
        strengthMeter.setPreferredSize(new Dimension(400, 8));
        strengthMeter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        strengthMeter.setStringPainted(false);
        strengthPanel.add(strengthMeter);
        strengthPanel.add(Box.createVerticalStrut(30));

        inputPanel.add(strengthPanel);
        mainPanel.add(inputPanel);

        // Requirements section
        JLabel requirementsTitle = new JLabel("Password Requirements");
        requirementsTitle.setFont(new Font("Arial", Font.BOLD, 16));
        requirementsTitle.setForeground(textColor);
        requirementsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(requirementsTitle);
        mainPanel.add(Box.createVerticalStrut(15));

        // Add requirement indicators
        addRequirement(mainPanel, "Length", "At least 8 characters");
        addRequirement(mainPanel, "Uppercase", "At least one uppercase letter");
        addRequirement(mainPanel, "Lowercase", "At least one lowercase letter");
        addRequirement(mainPanel, "Numbers", "At least one number");
        addRequirement(mainPanel, "Symbols", "At least one special character");

        // Add the main panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Set up listeners
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updatePasswordStrength();
            }
        });

        updatePasswordStrength();
    }

    private void addRequirement(JPanel container, String title, String description) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(new EmptyBorder(5, 0, 5, 0));

        // Status icon (circle)
        JPanel statusIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(229, 231, 235));
                g2d.fillOval(0, 0, 16, 16);
                g2d.dispose();
            }
        };
        statusIcon.setPreferredSize(new Dimension(16, 16));
        statusIcon.setMaximumSize(new Dimension(16, 16));
        statusIcon.setOpaque(false);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(107, 114, 128));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(descLabel);

        panel.add(statusIcon);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(textPanel);

        container.add(panel);
        container.add(Box.createVerticalStrut(10));
        requirementPanels.add(panel);
    }

    private void updatePasswordStrength() {
        String password = passwordField.getText();
        int strength = PasswordStrengthCalculator.calculateStrength(password);
        strengthMeter.setValue(strength);

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

        strengthLabel.setText("Password Strength: " + strengthText);
        strengthMeter.setForeground(color);
        strengthMeter.setBackground(new Color(229, 231, 235));

        // Update requirement indicators
        updateRequirementStatus(0, password.length() >= 8);
        updateRequirementStatus(1, password.matches(".*[A-Z].*"));
        updateRequirementStatus(2, password.matches(".*[a-z].*"));
        updateRequirementStatus(3, password.matches(".*\\d.*"));
        updateRequirementStatus(4, password.matches(".*[^A-Za-z0-9].*"));
    }

    private void updateRequirementStatus(int index, boolean fulfilled) {
        if (index < requirementPanels.size()) {
            JPanel panel = requirementPanels.get(index);
            Component[] components = panel.getComponents();
            for (Component component : components) {
                if (component instanceof JPanel && component.getPreferredSize().width == 16) {
                    JPanel statusIcon = (JPanel) component;
                    statusIcon.repaint();
                    
                    // Update the status icon
                    statusIcon.removeAll();
                    JPanel newStatusIcon = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            Graphics2D g2d = (Graphics2D) g.create();
                            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2d.setColor(fulfilled ? successColor : new Color(229, 231, 235));
                            g2d.fillOval(0, 0, 16, 16);
                            g2d.dispose();
                        }
                    };
                    newStatusIcon.setPreferredSize(new Dimension(16, 16));
                    newStatusIcon.setMaximumSize(new Dimension(16, 16));
                    newStatusIcon.setOpaque(false);
                    
                    statusIcon.setLayout(new BorderLayout());
                    statusIcon.add(newStatusIcon);
                    statusIcon.revalidate();
                    break;
                }
            }
        }
    }
}

