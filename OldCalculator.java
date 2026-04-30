import javax.swing.*;
import java.awt.*;

public class OldCalculator extends JFrame {
    private final JTextField display = new JTextField("0");
    private final StringBuilder typed = new StringBuilder();
    private String expr = "";
    private Double left = null;
    private String op = null;
    private boolean error = false, fresh = true;

    public OldCalculator() {
        super("Old Calculator");
        display.setFont(new Font("Monospaced", Font.BOLD, 28));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);

        JPanel p = new JPanel(new GridLayout(4, 4, 5, 5));
        for (String s : new String[]{"1","2","3","+","4","5","6","-","7","8","9","*",".","0","=","/"}) {
            JButton b = new JButton(s);
            b.setFont(new Font("Monospaced", Font.BOLD, 22));
            b.addActionListener(e -> press(s));
            p.add(b);
        }

        setLayout(new BorderLayout(8, 8));
        add(display, BorderLayout.NORTH);
        add(p, BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(320, 420);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void press(String s) {
        if (error) reset();
        if (Character.isDigit(s.charAt(0)) || ".".equals(s)) addDigit(s);
        else if ("=".equals(s)) equal();
        else addOp(s);
    }

    private void addDigit(String s) {
        if (left != null && op == null && fresh) reset();
        if (fresh) {
            typed.setLength(0);
            typed.append(".".equals(s) ? "0." : s);
            fresh = false;
        } else if (".".equals(s)) {
            if (typed.indexOf(".") >= 0) return;
            typed.append('.');
        } else typed.append(s);
        showDisplay();
    }

    private void addOp(String s) {
        if (typed.length() > 0) {
            left = left == null ? Double.parseDouble(typed.toString()) : calc(Double.parseDouble(typed.toString()));
            expr = fmt(left) + " " + s + " ";
            typed.setLength(0);
        } else if (!expr.isEmpty()) expr = expr.substring(0, expr.length() - 2) + s + " ";
        op = s;
        fresh = true;
        showDisplay();
    }

    private void equal() {
        if (op == null || typed.length() == 0) return;
        left = calc(Double.parseDouble(typed.toString()));
        expr = "";
        typed.setLength(0);
        typed.append(fmt(left));
        op = null;
        fresh = true;
        showDisplay();
    }

    private double calc(double right) {
        switch (op) {
            case "+": return left + right;
            case "-": return left - right;
            case "*": return left * right;
            case "/":
                if (right == 0) {
                    display.setText("Error: Division by zero");
                    error = true;
                    left = null;
                    op = null;
                    typed.setLength(0);
                    expr = "";
                    fresh = true;
                    return 0;
                }
                return left / right;
            default: return right;
        }
    }

    private void reset() {
        expr = "";
        typed.setLength(0);
        left = null;
        op = null;
        error = false;
        fresh = true;
        display.setText("0");
    }

    private void showDisplay() {
        display.setText(expr + typed);
    }

    private String fmt(double v) {
        return v == (long) v ? Long.toString((long) v) : Double.toString(v);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OldCalculator().setVisible(true));
    }
}