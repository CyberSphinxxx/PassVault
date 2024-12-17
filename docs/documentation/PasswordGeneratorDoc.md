# File: PasswordGenerator.java

## Overview
`PasswordGenerator` is a utility class that provides static methods for generating secure passwords. It allows customization of password length and character types to include (uppercase, lowercase, numbers, and symbols).

## Dependencies
- `java.util.Random`: For generating random characters.

## Constants

| Modifier        | Type     | Name         | Value                                   |
|-----------------|----------|--------------|---------------------------------------- |
| `private static`| `String` | `UPPERCASE`  | `"ABCDEFGHIJKLMNOPQRSTUVWXYZ"`          |
| `private static`| `String` | `LOWERCASE`  | `"abcdefghijklmnopqrstuvwxyz"`          |
| `private static`| `String` | `NUMBERS`    | `"0123456789"`                          |
| `private static`| `String` | `SYMBOLS`    | `"!@#$%^&*()_+-=[]{}|;:,.<>?"`          |

## Methods

### `public static String generatePassword(int length, boolean includeUppercase, boolean includeLowercase, boolean includeNumbers, boolean includeSymbols)`
- **Purpose**: Generates a random password based on specified criteria.
- **Parameters**:
  - `length`: The desired length of the password.
  - `includeUppercase`: Whether to include uppercase letters.
  - `includeLowercase`: Whether to include lowercase letters.
  - `includeNumbers`: Whether to include numbers.
  - `includeSymbols`: Whether to include symbols.
- **Returns**: A string containing the generated password.
- **Throws**: `IllegalArgumentException` if no character type is selected.
- **Algorithm**:
  1. Builds a character set based on the inclusion flags.
  2. Validates that at least one character type is selected.
  3. Randomly selects characters from the built set to form the password.

## Example Usage
```java
// Generate a 12-character password with all character types
String password = PasswordGenerator.generatePassword(12, true, true, true, true);

// Generate a 16-character password with only uppercase letters and numbers
String password = PasswordGenerator.generatePassword(16, true, false, true, false);

## Notes

- The class uses `java.util.Random` for generating random characters, which is not cryptographically secure.
- All methods in this class are static, making it a utility class.
- The class does not maintain any state between method calls.

## Security Considerations

1. The `Random` class is used for generating random numbers. For cryptographic purposes, consider using `SecureRandom` instead.
2. The method does not guarantee the inclusion of all selected character types in the generated password. For very short passwords, this could potentially result in a password not containing all requested character types.
3. The symbols used are limited to a predefined set. Consider allowing customization of the symbol set for increased flexibility.
4. There's no check for password strength beyond the inclusion of character types and length. Consider implementing additional strength checks.

```markdown
# File: PasswordGenerator.java

## Overview
`PasswordGenerator` is a utility class that provides static methods for generating secure passwords. It allows customization of password length and character types to include (uppercase, lowercase, numbers, and symbols).

## Dependencies
- `java.util.Random`: For generating random characters.

## Constants

| Modifier        | Type     | Name         | Value                                   |
|-----------------|----------|--------------|---------------------------------------- |
| `private static`| `String` | `UPPERCASE`  | `"ABCDEFGHIJKLMNOPQRSTUVWXYZ"`          |
| `private static`| `String` | `LOWERCASE`  | `"abcdefghijklmnopqrstuvwxyz"`          |
| `private static`| `String` | `NUMBERS`    | `"0123456789"`                          |
| `private static`| `String` | `SYMBOLS`    | `"!@#$%^&*()_+-=[]{}|;:,.<>?"`          |

## Methods

### `public static String generatePassword(int length, boolean includeUppercase, boolean includeLowercase, boolean includeNumbers, boolean includeSymbols)`
- **Purpose**: Generates a random password based on specified criteria.
- **Parameters**:
  - `length`: The desired length of the password.
  - `includeUppercase`: Whether to include uppercase letters.
  - `includeLowercase`: Whether to include lowercase letters.
  - `includeNumbers`: Whether to include numbers.
  - `includeSymbols`: Whether to include symbols.
- **Returns**: A string containing the generated password.
- **Throws**: `IllegalArgumentException` if no character type is selected.
- **Algorithm**:
  1. Builds a character set based on the inclusion flags.
  2. Validates that at least one character type is selected.
  3. Randomly selects characters from the built set to form the password.

## Example Usage
```java
// Generate a 12-character password with all character types
String password = PasswordGenerator.generatePassword(12, true, true, true, true);

// Generate a 16-character password with only uppercase letters and numbers
String password = PasswordGenerator.generatePassword(16, true, false, true, false);
```

## Notes

- The class uses `java.util.Random` for generating random characters, which is not cryptographically secure.
- All methods in this class are static, making it a utility class.
- The class does not maintain any state between method calls.


## Security Considerations

1. The `Random` class is used for generating random numbers. For cryptographic purposes, consider using `SecureRandom` instead.
2. The method does not guarantee the inclusion of all selected character types in the generated password. For very short passwords, this could potentially result in a password not containing all requested character types.
3. The symbols used are limited to a predefined set. Consider allowing customization of the symbol set for increased flexibility.
4. There's no check for password strength beyond the inclusion of character types and length. Consider implementing additional strength checks.


## Potential Improvements

1. Use `SecureRandom` instead of `Random` for improved security.
2. Add a method to ensure all selected character types are present in the generated password.
3. Allow customization of the character sets (e.g., custom symbol sets).
4. Implement additional password strength checks (e.g., avoiding common patterns or dictionary words).
5. Add options for generating passphrases or other alternative password formats.