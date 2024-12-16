import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PasswordManager {
    private static final File STORAGE_FILE = new File("passwords.dat");
    private List<PasswordEntry> savedPasswords;
    private List<Runnable> changeListeners = new ArrayList<>();

    public PasswordManager() {
        savedPasswords = new ArrayList<>();
    }

    public void addChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(Runnable listener) {
        changeListeners.remove(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : changeListeners) {
            listener.run();
        }
    }

    public void addPassword(PasswordEntry entry) {
        savedPasswords.add(entry);
        savePasswords();
        notifyListeners();
    }

    public void deletePassword(int index) {
        savedPasswords.remove(index);
        savePasswords();
        notifyListeners();
    }

    public void editPassword(int index, PasswordEntry newEntry) {
        savedPasswords.set(index, newEntry);
        savePasswords();
        notifyListeners();
    }

    public List<PasswordEntry> getSavedPasswords() {
        return savedPasswords;
    }

    public PasswordEntry getPasswordEntry(int index) {
        return savedPasswords.get(index);
    }

    public void savePasswords() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(STORAGE_FILE))) {
            List<String> encryptedPasswords = new ArrayList<>();
            for (PasswordEntry entry : savedPasswords) {
                String serializedEntry = entry.getLabel() + "," + entry.getUsername() + "," + entry.getPassword();
                String encryptedEntry = EncryptionUtil.encrypt(serializedEntry);
                encryptedPasswords.add(encryptedEntry);
            }
            out.writeObject(encryptedPasswords);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving passwords: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadPasswords() {
        if (!STORAGE_FILE.exists()) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(STORAGE_FILE))) {
            List<String> encryptedPasswords = (List<String>) in.readObject();
            savedPasswords.clear();
            for (String encryptedEntry : encryptedPasswords) {
                try {
                    String decryptedEntry = EncryptionUtil.decrypt(encryptedEntry);
                    String[] parts = decryptedEntry.split(",", 3);
                    if (parts.length == 3) {
                        savedPasswords.add(new PasswordEntry(parts[0], parts[1], parts[2]));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error decrypting password: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading passwords: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

