# File: SavedPasswordsPanel.java

## Overview
`SavedPasswordsPanel` is a custom JPanel that provides a graphical interface for displaying and managing saved passwords in the PasswordSentinel application. It features a searchable table of password entries with options to add, edit, delete, and reveal passwords.

## Dependencies
- `java.awt.*`: For GUI components and layout management.
- `java.awt.event.*`: For handling mouse events.
- `javax.swing.*`: For Swing GUI components.
- `javax.swing.border.*`: For panel borders.
- `javax.swing.event.*`: For document listeners.
- `javax.swing.table.*`: For table models and custom renderers.

## Fields

| Modifier   | Type                            | Name                 | Description                                    |
|------------|--------------------------------|----------------------|------------------------------------------------|
| `private`  | `PasswordManager`              | `passwordManager`    | Manages the collection of password entries.     |
| `private`  | `DefaultTableModel`            | `tableModel`         | Model for the password table.                   |
| `private`  | `JTable`                       | `savedPasswordsTable`| Table displaying saved passwords.               |
| `private`  | `JTextField`                   | `searchField`        | Field for searching passwords.                  |
| `private`  | `TableRowSorter<DefaultTableModel>` | `sorter`        | Sorter for filtering table rows.                |

## Constructor

### `public SavedPasswordsPanel(PasswordManager passwordManager)`
- **Purpose**: Initializes the panel with a given PasswordManager.
- **Parameters**:
  - `passwordManager`: The PasswordManager instance to use.
- **Behavior**: 
  - Sets up the layout and background.
  - Calls `initializeComponents()` to set up the UI.
  - Adds a change listener to the PasswordManager.

## Key Methods

### `private void initializeComponents()`
- **Purpose**: Sets up all UI components of the panel.
- **Components**:
  - Search field
  - Password table
  - Action buttons (Delete, Edit, Add Manual Entry)
- **Behavior**: 
  - Creates and configures the table model and sorter.
  - Sets up custom renderers and editors for table cells.
  - Adds listeners for search functionality and double-click editing.

### `private void filterTable()`
- **Purpose**: Filters the table based on the search field input.
- **Behavior**: Uses regex to filter table rows case-insensitively.

### `private void deletePassword()`
- **Purpose**: Deletes the selected password entry.
- **Behavior**: 
  - Confirms deletion with the user.
  - Removes the entry from the PasswordManager if confirmed.

### `private void editPassword()`
- **Purpose**: Opens the edit dialog for the selected password entry.
- **Behavior**: 
  - Retrieves the selected entry and opens SavePasswordDialog.
  - Handles cases where no entry is selected or the "Reveal" column is clicked.

### `private void addManualEntry()`
- **Purpose**: Opens a dialog to add a new password entry manually.
- **Behavior**: Calls SavePasswordDialog with empty initial values.

### `public void refreshTable()`
- **Purpose**: Updates the table with the current list of password entries.
- **Behavior**: Clears and repopulates the table model with masked passwords.

### `private void togglePasswordVisibility(int viewRow)`
- **Purpose**: Toggles the visibility of a password in the table.
- **Behavior**: Switches between masked and actual password display.

## Custom Classes

### `ButtonRenderer`
- Extends `JButton` and implements `TableCellRenderer`.
- Renders the "Reveal" button in the table.

### `ButtonEditor`
- Extends `DefaultCellEditor`.
- Handles the editing behavior of the "Reveal" button, toggling password visibility.

## Event Handling
- Search field: Updates table filtering in real-time.
- Table: Double-click to edit entries (except "Reveal" column).
- Buttons: Delete, Edit, and Add Manual Entry actions.

## Styling
- Uses theme colors from PasswordSentinel for consistency.
- Custom fonts and dimensions for table and buttons.

## Notes
- Implements a cleanup method to remove listeners when the panel is no longer needed.
- Uses a TableRowSorter for efficient searching and filtering.
- Masks passwords in the table view for security.

## Potential Improvements
1. Implement password strength indicators in the table.
2. Add export/import functionality for password entries.
3. Implement multi-select for batch operations on password entries.
4. Add column sorting functionality.
5. Implement more advanced search options (e.g., by date added, strength).