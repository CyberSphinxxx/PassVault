# File: PasswordEntry.java

## Overview
`PasswordEntry` is a Java class that implements `Serializable`. It serves as a data model for storing password entries in the PasswordSentinel application. Each entry contains a label, username, and password, with corresponding getter and setter methods.

## Dependencies
- `java.io.Serializable`: For object serialization support.

## Fields

| Modifier   | Type     | Name       | Description                               |
|------------|----------|------------|-------------------------------------------|
| `private`  | `String` | `label`    | Identifier/name for the password entry.   |
| `private`  | `String` | `username` | Username associated with the password.    |
| `private`  | `String` | `password` | The actual password being stored.         |

## Constructor

### `public PasswordEntry(String label, String username, String password)`
- **Purpose**: Creates a new password entry with the specified details.
- **Parameters**:
  - `label`: Identifier for the password entry.
  - `username`: Associated username.
  - `password`: The password to store.

## Methods

### Getters

#### `public String getLabel()`
- **Purpose**: Retrieves the entry's label.
- **Returns**: The label string.

#### `public String getUsername()`
- **Purpose**: Retrieves the stored username.
- **Returns**: The username string.

#### `public String getPassword()`
- **Purpose**: Retrieves the stored password.
- **Returns**: The password string.

### Setters

#### `public void setLabel(String label)`
- **Purpose**: Updates the entry's label.
- **Parameters**:
  - `label`: New label value.

#### `public void setUsername(String username)`
- **Purpose**: Updates the stored username.
- **Parameters**:
  - `username`: New username value.

#### `public void setPassword(String password)`
- **Purpose**: Updates the stored password.
- **Parameters**:
  - `password`: New password value.

## Serialization
- Implements `Serializable` to support saving/loading password entries to/from files.
- No custom serialization methods are implemented (uses default Java serialization).

## Example Usage
```java
// Create a new password entry
PasswordEntry entry = new PasswordEntry("Gmail", "user@gmail.com", "securePassword123");

// Get stored values
String label = entry.getLabel();      // Returns "Gmail"
String username = entry.getUsername(); // Returns "user@gmail.com"
String password = entry.getPassword(); // Returns "securePassword123"

// Update values
entry.setLabel("Google Account");
entry.setUsername("newuser@gmail.com");
entry.setPassword("newSecurePassword456");
```

## Notes

- This class is used throughout the application for storing and managing password entries.
- Being `Serializable` allows the entries to be saved to disk and loaded back.
- No validation is performed on the input values; this should be handled by the calling code.
- Consider adding validation and encryption for sensitive data in production environments.