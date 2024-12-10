# AES Encryption and Decryption using Java

## Key Generation

The `EncryptionUtil` class generates a secret key using the `KeyGenerator` class from the Java Cryptography Architecture (JCA). The key is generated with a strength of **256 bits**, which is a common key size for AES encryption.

## Key Storage

The generated secret key is stored in a file named `secret.key` using an `ObjectOutputStream`. This file is used to store the key securely so that it can be loaded later for decryption.

## Encryption

The `encrypt` method takes a string input (`data`) and performs the following steps:

1. **Create a Cipher instance**:  
   A `Cipher` instance is created with the specified transformation `AES/ECB/PKCS5Padding`.

2. **Initialize the Cipher**:  
   The `Cipher` is initialized with the secret key and the encryption mode (`ENCRYPT_MODE`).

3. **Encrypt the data**:  
   The input data is encrypted using the `doFinal` method of the `Cipher` instance, which returns the encrypted bytes.

4. **Base64 encoding**:  
   The encrypted bytes are encoded using Base64 to produce a string output.

## Decryption

The `decrypt` method takes a string input (`encryptedData`) and performs the following steps:

1. **Create a Cipher instance**:  
   A `Cipher` instance is created with the specified transformation `AES/ECB/PKCS5Padding`.

2. **Initialize the Cipher**:  
   The `Cipher` is initialized with the secret key and the decryption mode (`DECRYPT_MODE`).

3. **Base64 decoding**:  
   The input string is decoded from Base64 to produce the encrypted bytes.

4. **Decrypt the data**:  
   The encrypted bytes are decrypted using the `doFinal` method of the `Cipher` instance, which returns the decrypted bytes.

5. **Convert to string**:  
   The decrypted bytes are converted to a string output.

## AES Encryption Details

The encryption process uses the **Advanced Encryption Standard (AES)** algorithm with the following parameters:

- **Mode**: ECB (Electronic Codebook) mode, which is a simple and widely used mode.
- **Padding**: PKCS5 padding, which is a standardized padding scheme that adds bytes to the plaintext to ensure that it is a multiple of the block size.

### About AES

AES is a symmetric-key block cipher that operates on fixed-size blocks of plaintext and ciphertext. The encryption process involves a series of rounds that transform the plaintext into ciphertext using a combination of:

- **Substitution**  
- **Permutation**  
- **Mixing operations**  

## Bug Fixes and Updates

- Passwords are now encrypted using the AES (Advanced Encryption Standard) algorithm.
- Resolved warnings related to unchecked or unsafe operations by introducing type-safe methods and ensuring compatibility with -Xlint:unchecked.
- Replaced JList: The JList in the saved passwords section is now a JTable for better data representation.