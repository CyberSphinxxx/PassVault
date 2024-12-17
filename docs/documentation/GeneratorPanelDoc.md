
# File: GeneratorPanel.java

## Overview
`GeneratorPanel` is a Java class extending `JPanel`. It serves as the UI panel for generating secure passwords, copying them to the clipboard, and saving them. The class interacts with `PasswordManager` and uses auxiliary tools like `PasswordGenerator` and `PasswordStrengthCalculator`.

## Dependencies
- `javax.swing.*`: For UI components (JTextField, JCheckBox, JButton, etc.).
- `java.awt.*`: For layout management and styling.
- `java.awt.datatransfer.StringSelection`: For clipboard functionality.
- `javax.swing.event.ChangeEvent`, `ChangeListener`: For slider events.
- `javax.swing.border.*`: For component styling.

## Fields

| Modifier       | Type                   | Name                 | Description                          |
|----------------|------------------------|-----------------------|--------------------------------------|
| `private`      | `JTextField`           | `passwordField`       | Displays the generated password.     |
| `private`      | `JSlider`              | `lengthSlider`        | Controls the password length.        |
| `private`      | `JLabel`               | `lengthValueLabel`    | Displays the current slider value.   |
| `private`      | `JCheckBox`            | `includeUppercase`    | Toggle to include uppercase letters. |
| `private`      | `JCheckBox`            | `includeLowercase`    | Toggle to include lowercase letters. |
| `private`      | `JCheckBox`            | `includeNumbers`      | Toggle to include numbers.           |
| `private`      | `JCheckBox`            | `includeSymbols`      | Toggle to include symbols.           |
| `private`      | `JProgressBar`         | `strengthIndicator`   | Indicates the strength of the password.|
| `private`      | `PasswordManager`      | `passwordManager`     | Manages password storage.            |
| `private`      | `Color`                | `successColor`        | Color for strong passwords.          |
| `private`      | `Color`                | `warningColor`        | Color for medium passwords.          |
| `private`      | `Color`                | `errorColor`          | Color for weak passwords.            |
| `private`      | `Color`                | `textColor`           | Default text color.                  |

## Methods

### `GeneratorPanel(PasswordManager passwordManager)`
- **Purpose**: Constructor for the `GeneratorPanel`.
- **Parameters**: 
  - `passwordManager`: Instance of `PasswordManager` for saving passwords.
- **Description**: Initializes the panel, creates UI components, and sets up listeners.

### `private void initializeComponents()`
- **Purpose**: Sets up the UI layout, components, and event listeners.
- **Description**:
  - Creates a card-like styled UI for password generation.
  - Adds a password display field, length slider, and checkboxes for customization.
  - Provides buttons to generate, copy, and save passwords.

### `private void setupListeners()`
- **Purpose**: Adds event listeners for the length slider and checkboxes.
- **Listeners**:
  - Updates the password field dynamically when sliders or checkboxes change.

### `private void generatePassword()`
- **Purpose**: Generates a password based on user settings.
- **Dependencies**: Uses `PasswordGenerator.generatePassword` to create a password.
- **Steps**:
  1. Retrieves user-selected options (length, character types).
  2. Updates `passwordField` with the new password.
  3. Calculates and updates the password strength using `PasswordStrengthCalculator`.

### `private void copyPassword()`
- **Purpose**: Copies the generated password to the clipboard.
- **Behavior**:
  - Retrieves the password from `passwordField`.
  - Copies it to the system clipboard.
  - Displays a confirmation dialog.

### `private void savePassword()`
- **Purpose**: Saves the generated password using the `SavePasswordDialog`.
- **Behavior**:
  - Prompts the user with a dialog to save the password.
  - Validates if a password exists before saving.

### `private JButton createStyledButton(String text)`
- **Purpose**: Creates a styled button for the UI.
- **Parameters**:
  - `text`: Label for the button.
- **Returns**: A button with predefined colors, font, and size.

### `private JCheckBox createStyledCheckBox(String text, boolean selected)`
- **Purpose**: Creates a styled checkbox.
- **Parameters**:
  - `text`: Label for the checkbox.
  - `selected`: Default selection state.

### `private void updateStrengthMeter(JProgressBar meter, int strength)`
- **Purpose**: Updates the strength indicator based on the password's strength score.
- **Behavior**:
  - Sets the value, string description, and color of the progress bar.
- **Strength Ranges**:
  - `0-2`: Weak (Red color).
  - `3-4`: Medium (Yellow color).
  - `5`: Strong (Green color).

## Event Listeners
- **Slider**: Updates the password length dynamically.
- **Checkboxes**: Triggers password regeneration when toggled.
- **Buttons**:
  - **Generate**: Generates a new password.
  - **Copy**: Copies the password to the clipboard.
  - **Save**: Saves the password via `SavePasswordDialog`.

## UI Components Layout
1. **Password Display Panel**:
   - Displays generated passwords.
   - Includes a "Copy" button.
2. **Password Length Section**:
   - Slider for password length with a dynamic value display.
3. **Character Types Section**:
   - Four checkboxes: Uppercase, Lowercase, Numbers, Symbols.
4. **Password Strength Section**:
   - Progress bar to indicate strength visually and textually.
5. **Buttons**:
   - "Generate Password" and "Save Password".

## Color Customization
- **Success (Green)**: For strong passwords.
- **Warning (Yellow)**: For medium passwords.
- **Error (Red)**: For weak passwords.
- **Default Text Color (Gray)**: Used for labels and checkboxes.

## Notes
- `PasswordGenerator` and `PasswordStrengthCalculator` are required utilities for this class.
- This class focuses solely on UI and event-driven logic; generation and strength calculation are handled externally.

## Example Usage
```java
PasswordManager passwordManager = new PasswordManager();
GeneratorPanel generatorPanel = new GeneratorPanel(passwordManager);
```
