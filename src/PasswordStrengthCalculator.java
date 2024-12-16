public class PasswordStrengthCalculator {
    public static int calculateStrength(String password) {
        int strength = 0;
        if (password.length() >= 8 && password.length() <= 12) strength += 1;
        else if (password.length() > 12) strength += 2;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*\\d.*")) strength++;
        if (password.matches(".*[^A-Za-z0-9].*")) strength++;
        return Math.min(strength, 5);
    }
}

