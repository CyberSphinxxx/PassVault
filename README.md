# PasswordSentinel

## About

PasswordSentinel is a secure, local-only password manager built with Java, using Java Swing GUI. All data stays on your device, ensuring complete privacy while offering strong password generation and safe storage.

## Project Structure

The project consists of the following main components:

1. PasswordSentinel.java - Main application class
2. LoginDialog.java - Handles user authentication
3. PasswordManager.java - Manages password storage and retrieval
4. PasswordGenerator.java - Generates secure passwords
5. PasswordStrengthCalculator.java - Calculates password strength
6. GeneratorPanel.java - UI for password generation
7. SavedPasswordsPanel.java - UI for managing saved passwords
8. PasswordStrengthCheckerPanel.java - UI for checking password strength
9. SavePasswordDialog.java - Dialog for saving or editing passwords
10. PasswordEntry.java - Represents a single password entry
11. EncryptionUtil.java - Handles encryption and decryption of password data

## Main Features

1. Secure local storage of passwords
2. Password generation with customizable options
3. Password strength calculation and visual feedback
4. User-friendly interface for managing passwords
5. Search functionality for saved passwords
6. Password reveal/hide functionality
7. Edit and delete options for saved passwords

## File Descriptions

### PasswordSentinel.java

This is the main class of the application. It sets up the main window and initializes the different panels (Generator, Saved Passwords, and Password Strength Checker).

Key features:
- Initializes the main UI components
- Sets up the tabbed pane for different functionalities
- Creates header and footer panels

### LoginDialog.java

Handles user authentication to access the password manager.

Key features:
- Creates a login form with username and password fields
- Validates user credentials (currently set to admin/admin)
- Launches the main PasswordSentinel window upon successful login

### PasswordManager.java

Manages the storage, retrieval, and manipulation of password entries.

Key features:
- Adds, edits, and deletes password entries
- Saves passwords to a file (passwords.dat)
- Loads passwords from the file
- Implements change listeners to notify UI components of data changes

### PasswordGenerator.java

Generates secure passwords based on user-defined criteria.

Key features:
- Generates passwords with specified length
- Allows inclusion/exclusion of uppercase, lowercase, numbers, and symbols

### PasswordStrengthCalculator.java

Calculates the strength of a given password.

Key features:
- Evaluates password strength based on length, character types, and complexity
- Returns a strength score from 0 to 5

### GeneratorPanel.java

Provides a user interface for generating and customizing passwords.

Key features:
- Allows users to specify password length and character types
- Displays generated password and its strength
- Provides options to copy and save generated passwords

### SavedPasswordsPanel.java

Displays and manages saved passwords.

Key features:
- Shows a table of saved passwords with labels and usernames
- Provides search functionality
- Allows editing and deleting of saved passwords
- Implements a "reveal" button to show/hide passwords

### PasswordStrengthCheckerPanel.java

Allows users to check the strength of any password.

Key features:
- Provides a password input field
- Displays password strength with a progress bar
- Shows which password requirements are met

### SavePasswordDialog.java

A dialog for saving new passwords or editing existing ones.

Key features:
- Allows users to enter or edit label, username, and password
- Displays password strength in real-time
- Handles both new password entries and edits to existing entries

### PasswordEntry.java

Represents a single password entry with label, username, and password.

Key features:
- Implements Serializable for easy storage and retrieval
- Provides getters and setters for all fields


## Encryption

The application uses AES (Advanced Encryption Standard) to encrypt password data before storing it in the passwords.dat file. This ensures that even if someone gains access to the file, they cannot read the passwords without the encryption key.

### EncryptionUtil.java

This utility class provides methods for encrypting and decrypting data using AES.

Key features:
- Uses AES algorithm for encryption and decryption
- Provides static methods `encrypt` and `decrypt` for easy use throughout the application
- Uses a fixed encryption key (Note: In a production environment, this should be securely managed and not hardcoded)

### passwords.dat

This file stores the encrypted password data. Each entry in the file is encrypted using AES before being written to the file.


## Security Notes

- Passwords are stored locally on the user's device in the passwords.dat file
- The application uses AES encryption for storing passwords
- The encryption key is currently hardcoded in the EncryptionUtil class (This should be improved in a production environment)
- Users should ensure their device is secure to protect the stored passwords and the encryption key
- The passwords.dat file should be protected from unauthorized access

## Future Improvements

- Implement stronger encryption for stored passwords
- Add multi-factor authentication for accessing the password manager
- Implement password sharing functionality for team use
- Add automatic password strength assessment for all saved passwords
- Implement password expiration and rotation reminders
- Implement secure key management for the encryption key, possibly using a key derivation function based on a master password
- Add an option for users to change the encryption key
- Implement secure deletion of the passwords.dat file when the user chooses to reset the password manager

## Contributors

- @CyberSphinxxx
- @davenarchives
- @rhoii
- @BisayangProgrammer
- @HarryGwapo

---

© 2024 Password Sentinel. All rights reserved.
