# File: PasswordStrengthCalculator.java

## Overview
`PasswordStrengthCalculator` is a utility class that provides a static method for calculating the strength of a given password. It evaluates the password based on its length and the presence of various character types.

## Dependencies
- No external dependencies. Uses only Java standard libraries.

## Methods

### `public static int calculateStrength(String password)`
- **Purpose**: Calculates the strength of a given password.
- **Parameters**:
  - `password`: The password string to evaluate.
- **Returns**: An integer from 0 to 5 representing the password strength.
- **Algorithm**:
  1. Initializes strength score to 0.
  2. Checks password length:
     - 8-12 characters: +1 point
     - More than 12 characters: +2 points
  3. Checks for presence of:
     - Uppercase letters: +1 point
     - Lowercase letters: +1 point
     - Numbers: +1 point
     - Special characters: +1 point
  4. Returns the final score, capped at a maximum of 5.

## Strength Scoring

| Score | Interpretation |
|-------|----------------|
| 0-1   | Very Weak      |
| 2     | Weak           |
| 3     | Moderate       |
| 4     | Strong         |
| 5     | Very Strong    |

## Example Usage
```java
String password = "P@ssw0rd";
int strength = PasswordStrengthCalculator.calculateStrength(password);
System.out.println("Password strength: " + strength);
```

```markdown
# File: PasswordStrengthCalculator.java

## Overview
`PasswordStrengthCalculator` is a utility class that provides a static method for calculating the strength of a given password. It evaluates the password based on its length and the presence of various character types.

## Dependencies
- No external dependencies. Uses only Java standard libraries.

## Methods

### `public static int calculateStrength(String password)`
- **Purpose**: Calculates the strength of a given password.
- **Parameters**:
  - `password`: The password string to evaluate.
- **Returns**: An integer from 0 to 5 representing the password strength.
- **Algorithm**:
  1. Initializes strength score to 0.
  2. Checks password length:
     - 8-12 characters: +1 point
     - More than 12 characters: +2 points
  3. Checks for presence of:
     - Uppercase letters: +1 point
     - Lowercase letters: +1 point
     - Numbers: +1 point
     - Special characters: +1 point
  4. Returns the final score, capped at a maximum of 5.

## Strength Scoring

| Score | Interpretation |
|-------|----------------|
| 0-1   | Very Weak      |
| 2     | Weak           |
| 3     | Moderate       |
| 4     | Strong         |
| 5     | Very Strong    |

## Example Usage
```java
String password = "P@ssw0rd";
int strength = PasswordStrengthCalculator.calculateStrength(password);
System.out.println("Password strength: " + strength);
```

## Notes

- The method uses regex patterns to check for the presence of different character types.
- The strength score is capped at 5, even if a password meets all criteria.
- This is a basic strength calculator and does not consider factors like common words or patterns.


## Security Considerations

1. The calculator does not check for dictionary words or common password patterns.
2. It does not consider the positioning or repetition of characters.
3. A password meeting all criteria will always score 5, regardless of its actual complexity beyond the basic checks.


## Potential Improvements

1. Implement more sophisticated strength algorithms (e.g., zxcvbn).
2. Add checks for common words or patterns.
3. Consider character positioning and repetition in the strength calculation.
4. Provide more granular scoring (e.g., 0-100 scale).
5. Add methods to provide specific feedback on how to improve password strength.