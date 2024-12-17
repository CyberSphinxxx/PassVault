# PasswordSentinel.java

## Overview
`PasswordSentinel` is the main class for the **Password Sentinel** application. It serves as the entry point for the program and builds the primary graphical user interface (GUI) using Java Swing components.

---

## Features
1. **Theming**:
   - Custom theme colors for a consistent user experience.
   - Text and accent colors defined as constants for easy maintenance.

2. **Tabbed Interface**:
   - Provides three main tabs:
     - **Generator**: For generating secure passwords.
     - **Saved Passwords**: For displaying and managing stored passwords.
     - **Password Strength Checker**: For evaluating the strength of user-provided passwords.

3. **Header and Footer**:
   - A styled header displays the application title.
   - A footer includes a copyright notice and credits.

---

## Code Structure

### Class Constants
```java
public static final Color THEME_COLOR = new Color(240, 240, 250);
public static final Color ACCENT_COLOR = new Color(100, 100, 200);
public static final Color TEXT_COLOR = new Color(50, 50, 50);
```
- Defines the application's primary color scheme.

---

### Constructor: `PasswordSentinel()`
- Initializes the JFrame window.
- Loads previously saved passwords using the `PasswordManager` class.
- Calls `initializeUI()` to set up the interface.
- Displays the main window.

```java
setTitle("Password Sentinel");
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setSize(1000, 800);
```

---

### Method: `initializeUI()`
- Configures the main interface with a **JTabbedPane**.
- Adds tabs for:
  - **Generator** (via `GeneratorPanel`).
  - **Saved Passwords** (via `SavedPasswordsPanel`).
  - **Password Strength Checker** (via `PasswordStrengthCheckerPanel`).

---

### Method: `createHeaderPanel()`
- Builds a header panel with:
  - Custom accent background color.
  - Application title styled in bold white text.

```java
JLabel titleLabel = new JLabel("Password Sentinel");
titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
```

---

### Method: `createFooterPanel()`
- Builds a footer panel with:
  - Copyright notice.
  - List of contributors.

---

### `main` Method
- Application entry point.
- Launches the login dialog via `LoginDialog::showLoginDialog`.

```java
SwingUtilities.invokeLater(LoginDialog::showLoginDialog);
```

---

## Dependencies
- **PasswordManager**: Handles password data management.
- **GeneratorPanel**: UI for password generation.
- **SavedPasswordsPanel**: UI for managing saved passwords.
- **PasswordStrengthCheckerPanel**: UI for password strength analysis.
- **LoginDialog**: Initial login interface.

---

## Contributors
- `@CyberSphinxxx`
- `@davenarchives`
- `@rhoii`
- `@BisayangProgrammer`
- `@HarryGwapo`

---

## Notes
- This class uses **Java Swing** for building the GUI.
- The application ensures a clean, user-friendly interface with modern styling.
