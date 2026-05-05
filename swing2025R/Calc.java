package swing2025R;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calc extends JFrame {
    private static final Color BG_TOP = new Color(18, 22, 33);
    private static final Color BG_BOTTOM = new Color(31, 37, 54);
    private static final Color PANEL_BG = new Color(26, 31, 45);
    private static final Color DISPLAY_BG = new Color(12, 15, 24);
    private static final Color DISPLAY_FG = new Color(244, 247, 255);
    private static final Color DIGIT_BG = new Color(42, 48, 66);
    private static final Color DIGIT_FG = new Color(244, 247, 255);
    private static final Color OPERATOR_BG = new Color(255, 154, 60);
    private static final Color OPERATOR_FG = new Color(25, 25, 25);
    private static final Color EQUAL_BG = new Color(86, 201, 144);
    private static final Color EQUAL_FG = new Color(18, 28, 22);
    private static final Color META_FG = new Color(166, 176, 203);

    private final JTextField display = new JTextField();
    private final StringBuilder typed = new StringBuilder();
    private double num1 = 0;
    private double lastNum2 = 0;
    private String operator = "";
    private String lastOperator = "";
    private boolean startNew = true;
    private boolean error = false;

    public Calc() {
        super("Simple Calculator");

        setSize(380, 560);
        setMinimumSize(new Dimension(380, 560));
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel root = new JPanel(new BorderLayout(0, 16)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM);
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setContentPane(root);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Calculator");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setForeground(DISPLAY_FG);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel subtitle = new JLabel("Repeat = to continue the last operation");
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        subtitle.setForeground(META_FG);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));

        header.add(title);
        header.add(subtitle);

        display.setEditable(false);
        display.setText("0");
        display.setFont(new Font("SansSerif", Font.BOLD, 34));
        display.setForeground(DISPLAY_FG);
        display.setBackground(DISPLAY_BG);
        display.setCaretColor(DISPLAY_FG);
        display.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 28), 1, true),
                BorderFactory.createEmptyBorder(18, 16, 18, 16)
        ));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setPreferredSize(new Dimension(340, 78));
        display.setOpaque(true);

        JPanel displayWrapper = new JPanel(new BorderLayout());
        displayWrapper.setOpaque(false);
        displayWrapper.add(display, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 12, 12));
        buttonPanel.setOpaque(false);
        String[] buttons = {"7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "0", ".", "=", "+"};

        Listener listener = new Listener();
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("SansSerif", Font.BOLD, 21));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(true);
            button.setBackground(getButtonBackground(text));
            button.setForeground(getButtonForeground(text));
            button.addActionListener(listener);
            button.setPreferredSize(new Dimension(72, 60));
            buttonPanel.add(button);
        }

        root.add(header, BorderLayout.NORTH);
        root.add(displayWrapper, BorderLayout.CENTER);
        root.add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private Color getButtonBackground(String text) {
        if ("=".equals(text)) {
            return EQUAL_BG;
        }
        if (text.matches("[+\\-*/]")) {
            return OPERATOR_BG;
        }
        return DIGIT_BG;
    }

    private Color getButtonForeground(String text) {
        if ("=".equals(text)) {
            return EQUAL_FG;
        }
        if (text.matches("[+\\-*/]")) {
            return OPERATOR_FG;
        }
        return DIGIT_FG;
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
                    lastOperator = text;
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
                    double num2;
                    if (operator.isEmpty() && startNew) {
                        if (lastOperator.isEmpty()) {
                            display.setText(display.getText().isEmpty() ? "0" : display.getText());
                            return;
                        }
                        num2 = lastNum2;
                    } else {
                        num2 = Double.parseDouble(typed.toString());
                        lastNum2 = num2;
                    }
                    double result;

                    String activeOperator = operator.isEmpty() ? lastOperator : operator;

                    switch (activeOperator) {
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
                    num1 = result;
                    operator = "";
                    lastOperator = activeOperator;
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