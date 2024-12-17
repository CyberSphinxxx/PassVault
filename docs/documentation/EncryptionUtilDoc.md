
# EncryptionUtil.java

## Overview
`EncryptionUtil` is a utility class that provides **AES encryption and decryption** for secure data handling. It uses a predefined symmetric key to encrypt and decrypt sensitive information.

---

## Features
1. **Encryption**:
   - Encrypts a plain text input using the AES algorithm.
   - Encodes the encrypted result in Base64 for safe storage and transmission.

2. **Decryption**:
   - Decodes the Base64 input.
   - Decrypts the data using the AES algorithm and the predefined key.

3. **Symmetric Key**:
   - Utilizes a hardcoded 16-byte key: `1234567890123456`.
   - Key is compatible with AES-128 encryption.

---

## Code Structure

### Class Constants
```java
private static final String ALGORITHM = "AES";
private static final byte[] KEY = "1234567890123456".getBytes();
```
- **ALGORITHM**: Specifies the encryption algorithm (AES).
- **KEY**: A fixed 16-byte key used for both encryption and decryption.

---

### Method: `encrypt(String data)`
- **Purpose**: Encrypts a plain text input using AES encryption.
- **Parameters**:
  - `String data`: The text to encrypt.
- **Returns**: A Base64-encoded encrypted string.
- **Implementation**:
  - Initializes a `Cipher` instance for AES encryption mode.
  - Processes the input data and returns the Base64 string.

```java
byte[] encryptedBytes = cipher.doFinal(data.getBytes());
return Base64.getEncoder().encodeToString(encryptedBytes);
```

---

### Method: `decrypt(String encryptedData)`
- **Purpose**: Decrypts a Base64-encoded string back to its original form.
- **Parameters**:
  - `String encryptedData`: The encrypted Base64 string.
- **Returns**: The decrypted plain text.
- **Implementation**:
  - Decodes the Base64 string into bytes.
  - Decrypts the data using AES decryption mode.

```java
byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
return new String(decryptedBytes);
```

---

## Example Usage
```java
public static void main(String[] args) {
    try {
        String originalData = "MySecurePassword";
        String encryptedData = EncryptionUtil.encrypt(originalData);
        String decryptedData = EncryptionUtil.decrypt(encryptedData);

        System.out.println("Original Data: " + originalData);
        System.out.println("Encrypted Data: " + encryptedData);
        System.out.println("Decrypted Data: " + decryptedData);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

### Output:
```
Original Data: MySecurePassword
Encrypted Data: <Base64 Encrypted String>
Decrypted Data: MySecurePassword
```

---

## Notes
- The hardcoded key (`1234567890123456`) must be kept secure, as it is required for decryption.
- AES encryption provides strong data protection, but avoid exposing the key or relying solely on hardcoded keys in production.
- Base64 encoding ensures encrypted data can be safely stored in text-based formats.

---

## Dependencies
- **Java Crypto Library** (`javax.crypto`).
- **Base64 Encoder/Decoder** (`java.util.Base64`).

---

## Security Considerations
- Hardcoding encryption keys is not recommended for production systems. Use a secure key management solution.
- Always validate the encrypted data before processing.