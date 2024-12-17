# File: LoginDialog.java

## Overview
`LoginDialog` is a Java class extending `JDialog`. It provides a static method to create and display a login dialog for user authentication in the PasswordSentinel application. The dialog includes username and password fields, along with login functionality.

## Dependencies
- `javax.swing.*`: For UI components (JDialog, JTextField, JPasswordField, JButton, etc.).
- `javax.swing.border.EmptyBorder`: For component padding.
- `java.awt.*`: For layout management and styling.
- `java.awt.event.ActionEvent`, `ActionListener`: For button click events.

## Constants

| Modifier        | Type    | Name               | Value                    |
|-----------------|---------|--------------------| ------------------------ |
| `private static`| `Color` | `BACKGROUND_COLOR` | `new Color(255, 255, 255)`|
| `private static`| `Color` | `PRIMARY_COLOR`    | `new Color(59, 130, 246)`|
| `private static`| `Color` | `TEXT_COLOR`       | `new Color(31, 41, 55)` |
| `private static`| `Color` | `BORDER_COLOR`     | `new Color(209, 213, 219)`|

## Methods

### `public static void showLoginDialog()`
- **Purpose**: Creates and displays the login dialog.
- **Description**:
  - Sets up the dialog properties (size, layout, position).
  - Creates and adds UI components (title, username field, password field, login button).
  - Implements login validation logic.

### `private static JTextField createStyledTextField(String placeholder)`
- **Purpose**: Creates a styled text field with placeholder functionality.
- **Parameters**: 
  - `placeholder`: The placeholder text to display.
- **Returns**: A styled `JTextField` with focus listeners for placeholder behavior.

### `private static JPasswordField createStyledPasswordField(String placeholder)`
- **Purpose**: Creates a styled password field with placeholder functionality.
- **Parameters**: 
  - `placeholder`: The placeholder text to display.
- **Returns**: A styled `JPasswordField` with focus listeners for placeholder and masking behavior.

### `private static JButton createStyledButton(String text)`
- **Purpose**: Creates a styled button for the UI.
- **Parameters**:
  - `text`: Label for the button.
- **Returns**: A button with predefined colors, font, and size.

### `private static boolean validateLogin(String username, String password)`
- **Purpose**: Validates the entered credentials.
- **Parameters**:
  - `username`: The entered username.
  - `password`: The entered password.
- **Returns**: `true` if credentials are valid, `false` otherwise.
- **Note**: Currently set to accept "admin" as both username and password.

## UI Components Layout
1. **Title Label**: "Password Sentinel"
2. **Username Field**: Text field with "Username" placeholder
3. **Password Field**: Password field with "Password" placeholder
4. **Login Button**: "Sign In" button
5. **Error Label**: Displays error messages for invalid login attempts

## Event Listeners
- **Login Button**: Triggers login validation on click.
- **Text Fields**: Focus listeners for placeholder behavior.

## Login Process
1. User enters username and password.
2. User clicks "Sign In" button.
3. `validateLogin()` is called to check credentials.
4. If valid, the dialog closes and `PasswordSentinel` main window opens.
5. If invalid, an error message is displayed.

## Styling Details
- **Dialog Size**: 350x400 pixels
- **Background Color**: White
- **Primary Color** (for title and button): Blue (59, 130, 246)
- **Text Color**: Dark gray (31, 41, 55)
- **Border Color**: Light gray (209, 213, 219)
- **Font**: Arial, various sizes and styles

## Notes
- The dialog is non-resizable and centered on the screen.
- Placeholder text is implemented for both username and password fields.
- The current login validation is a placeholder and should be replaced with actual authentication logic.

## Example Usage
```java
SwingUtilities.invokeLater(LoginDialog::showLoginDialog);
```