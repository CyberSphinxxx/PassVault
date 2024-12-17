# **PasswordManager**

### **Overview**  
`PasswordManager` is responsible for managing password entries, including saving, loading, editing, and deleting passwords. It uses **serialization** to store passwords locally in an encrypted format (`passwords.dat`). This ensures password data remains secure and persists between sessions.

---

## **Class Responsibilities**
1. **Password Management**  
   - Add, delete, and edit password entries.  
   - Load and save passwords from/to a local file.  
2. **Data Encryption**  
   - Encrypts passwords before saving.  
   - Decrypts passwords upon loading.  
3. **Change Listeners**  
   - Notifies UI components of changes to the password list.

---

## **Fields**

| **Field**                     | **Type**                | **Description**                                                                 |
|-------------------------------|-------------------------|-------------------------------------------------------------------------------|
| `STORAGE_FILE`                | `File`                  | File object pointing to `passwords.dat`, where password entries are saved.    |
| `savedPasswords`              | `List<PasswordEntry>`   | A list that stores password entries in memory.                                |
| `changeListeners`             | `List<Runnable>`        | A list of listeners to notify UI components about password list updates.      |

---

## **Methods**

| **Method**                     | **Description**                                                                                              |
|--------------------------------|-------------------------------------------------------------------------------------------------------------|
| `PasswordManager()`            | Constructor that initializes the `savedPasswords` list.                                                     |
| `addChangeListener(Runnable)`  | Adds a listener to be notified when the password list changes.                                              |
| `removeChangeListener(Runnable)` | Removes a change listener.                                                                                 |
| `notifyListeners()`            | Notifies all registered listeners about changes to the password list.                                       |
| `addPassword(PasswordEntry)`   | Adds a new password entry, saves it to the file, and notifies listeners.                                    |
| `deletePassword(int index)`    | Deletes a password entry at a given index, saves changes, and notifies listeners.                           |
| `editPassword(int index, PasswordEntry)` | Replaces a password entry at a given index with a new entry, saves changes, and notifies listeners. |
| `getSavedPasswords()`          | Returns the list of saved password entries.                                                                 |
| `getPasswordEntry(int index)`  | Retrieves a specific password entry at the given index.                                                     |
| `savePasswords()`              | Encrypts all password entries and writes them to the `passwords.dat` file.                                  |
| `loadPasswords()`              | Reads the `passwords.dat` file, decrypts the entries, and loads them into memory.                           |

---

## **Details of Key Methods**

### **1. `savePasswords()`**  
- **Purpose**:  
   Encrypts each `PasswordEntry` and serializes the list to `passwords.dat`.  
- **Process**:  
   - Iterates through all password entries.  
   - Encrypts each entry using the `EncryptionUtil.encrypt()` method.  
   - Writes the encrypted list to the file using `ObjectOutputStream`.  
- **Exception Handling**:  
   - Displays an error dialog if saving fails.  

---

### **2. `loadPasswords()`**  
- **Purpose**:  
   Reads the encrypted password file (`passwords.dat`), decrypts the entries, and loads them into memory.  
- **Process**:  
   - Checks if the file exists; if not, returns early.  
   - Reads the encrypted list using `ObjectInputStream`.  
   - Decrypts each password entry using `EncryptionUtil.decrypt()`.  
   - Splits the decrypted data into label, username, and password.  
- **Exception Handling**:  
   - Displays an error dialog for file read/decryption issues.  

---

## **Usage Example**

Here’s an example of how the `PasswordManager` class might be used:

```java
PasswordManager manager = new PasswordManager();
manager.loadPasswords(); // Load saved passwords

// Add a new password entry
PasswordEntry entry = new PasswordEntry("Email", "user@example.com", "P@ssw0rd123");
manager.addPassword(entry);

// Delete a password entry
manager.deletePassword(0); // Deletes the first entry

// Edit a password entry
PasswordEntry updatedEntry = new PasswordEntry("Email", "user@example.com", "NewP@ss123");
manager.editPassword(0, updatedEntry);

// Retrieve all saved passwords
List<PasswordEntry> passwords = manager.getSavedPasswords();
for (PasswordEntry p : passwords) {
    System.out.println("Label: " + p.getLabel() + ", Username: " + p.getUsername());
}
```

---

## **Dependencies**  
- **PasswordEntry**: Represents an individual password record with a label, username, and password.  
- **EncryptionUtil**: Handles encryption and decryption of password data.  
- **Swing JOptionPane**: Used for displaying error messages.

---

## **Error Handling**  
The class ensures robustness by:  
- Displaying user-friendly error dialogs for file I/O or decryption issues.  
- Ignoring invalid entries during the decryption process while continuing to load other valid entries.

---

## **Notes**  
- The encryption and decryption are handled via `EncryptionUtil`. Ensure this class implements secure AES encryption.  
- Passwords are saved in serialized and encrypted form (`passwords.dat`) to prevent unauthorized access.  

---
