import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SavePasswordDialog {
    public static void showDialog(Component parent, PasswordManager passwordManager, String initialPassword, String initialLabel, String initialUsername, int editIndex) {
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
                int strength = PasswordStrengthCalculator.calculateStrength(password);
                updateStrengthMeter(strengthMeter, strength);
            }
        });

        updateStrengthMeter(strengthMeter, PasswordStrengthCalculator.calculateStrength(initialPassword));

        int result = JOptionPane.showConfirmDialog(parent, panel, editIndex >= 0 ? "Edit Password" : "Save Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String label = labelField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (!label.isEmpty() && !password.isEmpty()) {
                PasswordEntry entry = new PasswordEntry(label, username, password);
                if (editIndex >= 0) {
                    passwordManager.editPassword(editIndex, entry);
                    JOptionPane.showMessageDialog(parent, "Password updated successfully!");
                } else {
                    passwordManager.addPassword(entry);
                    JOptionPane.showMessageDialog(parent, "Password saved successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(parent, "Label and password are required.");
            }
        }
    }

    private static void updateStrengthMeter(JProgressBar meter, int strength) {
        meter.setValue(strength);
        String strengthText;
        Color color;

        if (strength <= 2) {
            strengthText = "Weak";
            color = new Color(255, 87, 87);
        } else if (strength <= 4) {
            strengthText = "Medium";
            color = new Color(255, 206, 86);
        } else {
            strengthText = "Strong";
            color = new Color(97, 255, 66);
        }

        meter.setString(strengthText);
        meter.setForeground(color);
    }
}

