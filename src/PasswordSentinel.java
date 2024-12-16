import java.awt.*;
import javax.swing.*;

public class PasswordSentinel extends JFrame {
    public static final Color THEME_COLOR = new Color(240, 240, 250);
    public static final Color ACCENT_COLOR = new Color(100, 100, 200);
    public static final Color TEXT_COLOR = new Color(50, 50, 50);

    private PasswordManager passwordManager;

    public PasswordSentinel() {
        setTitle("Password Sentinel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLayout(new BorderLayout());
        getContentPane().setBackground(THEME_COLOR);

        passwordManager = new PasswordManager();
        passwordManager.loadPasswords();

        initializeUI();

        setVisible(true);
    }

    private void initializeUI() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(THEME_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.addTab("Generator", new GeneratorPanel(passwordManager));
        tabbedPane.addTab("Saved Passwords", new SavedPasswordsPanel(passwordManager));
        tabbedPane.addTab("Password Strength Checker", new PasswordStrengthCheckerPanel());
        add(tabbedPane, BorderLayout.CENTER);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFooterPanel(), BorderLayout.SOUTH);
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

        JLabel copyrightLabel = new JLabel("©2024 Password Sentinel. All rights reserved.\n \nCreated by: @CyberSphinxxx, @davenarchives, @rhoii, @BisayangProgrammer, @HarryGwapo");
        copyrightLabel.setForeground(Color.WHITE);
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(copyrightLabel);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginDialog::showLoginDialog);
    }
}

