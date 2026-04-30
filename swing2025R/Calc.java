package swing2025R;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calc extends JFrame {
    private final JTextField display = new JTextField();
    private final StringBuilder typed = new StringBuilder();
    private double num1 = 0;
    private String operator = "";
    private boolean startNew = true;
    private boolean error = false;

    public Calc() {
        super("Simple Calculator");

        setSize(300, 300);
        setLocation(200, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        display.setEditable(false);
        display.setFont(new Font("Monospaced", Font.BOLD, 28));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setPreferredSize(new Dimension(300, 50));
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 0, 0));
        String[] buttons = {"7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "0", ".", "=", "+"};

        Listener listener = new Listener();
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Monospaced", Font.BOLD, 20));
            button.addActionListener(listener);
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void resetIfError() {
        if (!error) return;
        display.setText("");
        typed.setLength(0);
        operator = "";
        num1 = 0;
        startNew = true;
        error = false;
    }

    private void showError() {
        display.setText("Error");
        typed.setLength(0);
        operator = "";
        startNew = true;
        error = true;
    }

    private String format(double value) {
        return value == (long) value ? String.valueOf((long) value) : String.valueOf(value);
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetIfError();

            String text = ((JButton) e.getSource()).getText();

            if (text.matches("[0-9.]")) {
                if (startNew) {
                    typed.setLength(0);
                    startNew = false;
                    if (".".equals(text)) typed.append("0");
                }
                if (".".equals(text) && typed.indexOf(".") >= 0) return;
                typed.append(text);
                display.setText(typed.toString());
                return;
            }

            if (text.matches("[+\\-*/]")) {
                try {
                    num1 = Double.parseDouble(typed.length() == 0 ? display.getText() : typed.toString());
                    operator = text;
                    typed.setLength(0);
                    startNew = true;
                    display.setText(format(num1) + " " + operator + " ");
                } catch (Exception ex) {
                    showError();
                }
                return;
            }

            if ("=".equals(text)) {
                try {
                    double num2 = Double.parseDouble(typed.toString());
                    double result;

                    switch (operator) {
                        case "+": result = num1 + num2; break;
                        case "-": result = num1 - num2; break;
                        case "*": result = num1 * num2; break;
                        case "/":
                            if (num2 == 0) {
                                showError();
                                return;
                            }
                            result = num1 / num2;
                            break;
                        default:
                            display.setText(typed.toString());
                            return;
                    }

                    display.setText(format(result));
                    typed.setLength(0);
                    typed.append(format(result));
                    num1 = result;
                    operator = "";
                    startNew = true;
                } catch (Exception ex) {
                    showError();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calc::new);
    }
}