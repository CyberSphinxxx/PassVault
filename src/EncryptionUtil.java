import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String KEY_FILE = "secret.key";
    private static SecretKey secretKey;

    static {
        try {
            File keyFile = new File(KEY_FILE);
            if (keyFile.exists()) {
                // Load existing key from file
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(keyFile))) {
                    secretKey = (SecretKey) in.readObject();
                }
            } else {
                // Generate new key and store it in file
                KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
                keyGen.init(256); // 256-bit key
                secretKey = keyGen.generateKey();
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(keyFile))) {
                    out.writeObject(secretKey);
                }
            }
        } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return data; // Return original data in case of error
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return encryptedData; // Return encrypted data in case of error
        }
    }
}