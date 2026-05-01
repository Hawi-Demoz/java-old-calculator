import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleCalculator extends JFrame {

    private JTextField display;
    private JButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private JButton dot, add, sub, mul, div, equal;
    private double num1 = 0;
    private String operator = "";

    public SimpleCalculator() {
        super("Simple Calculator");
        setSize(200, 300);
        setLocation(200, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        display = new JTextField();
        display.setEditable(false);
        display.setPreferredSize(new Dimension(300, 100));
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 0, 0));

        button0 = new JButton("0");
        button1 = new JButton("1");
        button2 = new JButton("2");
        button3 = new JButton("3");
        button4 = new JButton("4");
        button5 = new JButton("5");
        button6 = new JButton("6");
        button7 = new JButton("7");
        button8 = new JButton("8");
        button9 = new JButton("9");
        dot = new JButton(".");
        add = new JButton("+");
        sub = new JButton("-");
        mul = new JButton("*");
        div = new JButton("/");
        equal = new JButton("=");

        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(add);
        buttonPanel.add(button4);
        buttonPanel.add(button5);
        buttonPanel.add(button6);
        buttonPanel.add(sub);
        buttonPanel.add(button7);
        buttonPanel.add(button8);
        buttonPanel.add(button9);
        buttonPanel.add(mul);
        buttonPanel.add(dot);
        buttonPanel.add(button0);
        buttonPanel.add(equal);
        buttonPanel.add(div);

        add(buttonPanel, BorderLayout.CENTER);

        Listener l = new Listener();
        button1.addActionListener(l);
        button2.addActionListener(l);
        button3.addActionListener(l);
        add.addActionListener(l);
        button4.addActionListener(l);
        button5.addActionListener(l);
        button6.addActionListener(l);
        sub.addActionListener(l);
        button7.addActionListener(l);
        button8.addActionListener(l);
        button9.addActionListener(l);
        mul.addActionListener(l);
        dot.addActionListener(l);
        button0.addActionListener(l);
        equal.addActionListener(l);
        div.addActionListener(l);

        setVisible(true);
    }

    private class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            String text = btn.getText();

            if (text.matches("[0-9.]")) {
                display.setText(display.getText() + text);

            } else if (text.matches("[+\\-*/]")) {
                try {
                    num1 = Double.parseDouble(display.getText());
                    operator = text;
                    display.setText("");
                } catch (Exception ex) {
                    display.setText("Error");
                }

            } else if (text.equals("=")) {
                try {
                    double num2 = Double.parseDouble(display.getText());
                    double result = 0;

                    switch (operator) {
                        case "+":
                            result = num1 + num2;
                            break;
                        case "-":
                            result = num1 - num2;
                            break;
                        case "*":
                            result = num1 * num2;
                            break;
                        case "/":
                            if (num2 == 0) {
                                display.setText("Error");
                                return;
                            }
                            result = num1 / num2;
                            break;
                    }

                    display.setText(String.valueOf(result));
                } catch (Exception ex) {
                    display.setText("Error");
                }
            }
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SimpleCalculator();
            }
        });
    }
}
