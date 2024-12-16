import java.util.Random;

public class PasswordGenerator {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    public static String generatePassword(int length, boolean includeUppercase, boolean includeLowercase,
                                          boolean includeNumbers, boolean includeSymbols) {
        StringBuilder charSet = new StringBuilder();
        if (includeUppercase) charSet.append(UPPERCASE);
        if (includeLowercase) charSet.append(LOWERCASE);
        if (includeNumbers) charSet.append(NUMBERS);
        if (includeSymbols) charSet.append(SYMBOLS);

        if (charSet.length() == 0) {
            throw new IllegalArgumentException("At least one character type must be selected.");
        }

        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            password.append(charSet.charAt(random.nextInt(charSet.length())));
        }
        return password.toString();
    }
}

