## PassVault Project Overview

**Project Name:** PassVault

**Objective:**
The objective of this project is to create a secure, user-friendly application for generating, managing, and storing passwords. The application features a password generator, strength indicator, and a password manager that securely saves and retrieves passwords.

**Technologies Used:**
- Java Swing for GUI
- Java I/O for file operations
- Java Collections for data management

### Key Components:

1. **Password Generation:**
   - **Character Sets:** Includes uppercase letters, lowercase letters, numbers, and symbols.
   - **Length Control:** Allows users to set password length between 6 to 30 characters.
   - **Options:** Users can choose which character sets to include in the generated password.

2. **Password Strength Indicator:**
   - **Criteria:** Evaluates password strength based on length, character variety (uppercase, lowercase, numbers, symbols).
   - **Visualization:** Uses a `JProgressBar` to show password strength, with labels "Weak", "Medium", and "Strong".

3. **Password Management:**
   - **Saving Passwords:** Allows users to save generated passwords with labels and optional usernames.
   - **Retrieving Passwords:** Stored passwords can be revealed or hidden, and managed through a table interface.
   - **Editing and Deleting:** Users can edit or delete saved passwords.
   - **Manual Entry:** Supports manual addition of passwords.

4. **Encryption and Security:**
   - **Storage:** Passwords are saved in an encrypted format in a file named `passwords.dat`.
   - **Encryption Utility:** Placeholder for encryption and decryption methods (to be implemented).

5. **User Interface:**
   - **Tabbed Pane:** Divides functionality into two main tabs - "Generator" and "Saved Passwords".
   - **Stylized Components:** Uses custom colors and fonts to enhance UI aesthetics.

### UI Components:

- **Generator Panel:**
  - **Password Field:** Displays generated passwords.
  - **Controls:** Sliders and checkboxes for password length and character set selection.
  - **Buttons:** Includes "Generate Password" and "Copy".

- **Saved Passwords Panel:**
  - **Table:** Displays saved passwords with actions to reveal, edit, or delete.
  - **Buttons:** Includes options to save generated passwords, delete selected passwords, edit passwords, and add manual entries.

### Class Descriptions:

- **PassVault:** Main class extending `JFrame` that sets up the UI and handles the core functionality.
- **PasswordEntry:** Represents a password entry with label, username, and password.
- **EncryptionUtil:** Placeholder class for encryption and decryption logic.
- **ButtonRenderer & ButtonEditor:** Custom renderer and editor for table cell buttons.

### Future Enhancements:

- **Implement Encryption:** Replace placeholder methods in `EncryptionUtil` with actual encryption algorithms.
- **Validation Logic:** Enhance login validation with a secure authentication mechanism.
- **Backup and Sync:** Add features for password backup and synchronization across devices.
