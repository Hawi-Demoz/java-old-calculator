# Simple Swing Calculator

A simple old-style calculator built with Java Swing.

## Features

- Basic calculator layout with a 4x4 button grid
- Supports addition, subtraction, multiplication, and division
- Handles decimal numbers
- Shows the expression and the result in the display
- Displays an error message for division by zero

## Compile

Open a terminal in this folder and run:

```bash
javac -d . swing2025R/Calc.java
```

## Run

After compilation, launch the app with:

```bash
java swing2025R.Calc
```

## Troubleshooting

If you get a `class not found` error on another device, check these points:

- Run the commands from the project root folder, not from inside `swing2025R`.
- Keep the file in this exact path: `swing2025R/Calc.java`.
- Compile with `javac -d . swing2025R/Calc.java` so Java creates the package folder correctly.
- Run with `java swing2025R.Calc`, not `java Calc` and not `java OldCalculator`.
- Make sure the other device has a JDK installed, not just a JRE.
- Copy the whole project folder, including the `swing2025R` directory, not only the `.java` file.

## Notes

- The calculator clears itself after an error when you press a new button.
- The display shows the current expression while you type.