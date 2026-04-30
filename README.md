# Old Calculator

A simple old-style calculator built with Java Swing.

## Features

- Basic calculator layout with a 4x4 button grid
- Supports addition, subtraction, multiplication, and division
- Handles decimal numbers
- Shows results in a read-only display
- Displays an error message for division by zero

## Compile

Open a terminal in this folder and run:

```bash
javac OldCalculator.java
```

## Run

After compilation, launch the app with:

```bash
java OldCalculator
```

## Notes

- The calculator clears itself after an error when you press a number or decimal point.
- Multiple operators in a row replace the previous operator instead of creating invalid input.