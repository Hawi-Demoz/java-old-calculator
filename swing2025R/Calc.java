package swing2025R;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Calc extends JFrame {
    private static final Color BG_TOP = new Color(20, 25, 34);
    private static final Color BG_BOTTOM = new Color(8, 10, 16);
    private static final Color FRAME_EDGE = new Color(74, 84, 104);
    private static final Color PANEL_BG = new Color(23, 28, 40);
    private static final Color DISPLAY_BG = new Color(11, 16, 11);
    private static final Color DISPLAY_FG = new Color(166, 245, 173);
    private static final Color DIGIT_BG = new Color(46, 54, 74);
    private static final Color DIGIT_FG = new Color(244, 247, 255);
    private static final Color OPERATOR_BG = new Color(242, 159, 70);
    private static final Color OPERATOR_FG = new Color(33, 25, 12);
    private static final Color EQUAL_BG = new Color(92, 202, 147);
    private static final Color EQUAL_FG = new Color(14, 27, 19);
    private static final Color CONTROL_BG = new Color(66, 74, 92);
    private static final Color CONTROL_FG = new Color(245, 247, 252);
    private static final Color MEMORY_BG = new Color(106, 112, 132);
    private static final Color MEMORY_FG = new Color(248, 249, 252);
    private static final Color META_FG = new Color(167, 177, 202);

    private final JTextField display = new JTextField();
    private final JLabel statusLabel = new JLabel("READY");
    private final StringBuilder typed = new StringBuilder();
    private double num1 = 0;
    private double lastNum2 = 0;
    private double memory = 0;
    private String operator = "";
    private String lastOperator = "";
    private boolean startNew = true;
    private boolean error = false;

    public Calc() {
        super("Retro Calc");

        setSize(420, 680);
        setMinimumSize(new Dimension(420, 680));
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 14)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM);
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 18));
                for (int y = 0; y < getHeight(); y += 6) {
                    g2.drawLine(0, y, getWidth(), y);
                }
                g2.dispose();
            }
        };
        root.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FRAME_EDGE, 2, true),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        setContentPane(root);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("RETRO CALC");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setForeground(DISPLAY_FG);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel subtitle = new JLabel("Memory keys, keyboard shortcuts, repeat equals");
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        subtitle.setForeground(META_FG);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));

        header.add(title);
        header.add(subtitle);

        JPanel screen = new JPanel(new BorderLayout(0, 4));
        screen.setOpaque(false);

        statusLabel.setForeground(META_FG);
        statusLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));

        display.setEditable(false);
        display.setText("0");
        display.setFont(new Font("Monospaced", Font.BOLD, 34));
        display.setForeground(DISPLAY_FG);
        display.setBackground(DISPLAY_BG);
        display.setCaretColor(DISPLAY_FG);
        display.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(141, 197, 146, 90), 2, true),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setPreferredSize(new Dimension(360, 80));
        display.setOpaque(true);

        screen.add(statusLabel, BorderLayout.NORTH);
        screen.add(display, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(6, 4, 10, 10));
        buttonPanel.setOpaque(false);
        String[] buttons = {
                "MC", "MR", "M+", "M-",
                "C", "CE", "⌫", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "±", "0", ".", "="
        };

        Listener listener = new Listener();
        for (String text : buttons) {
            buttonPanel.add(createButton(text, listener));
        }

        root.add(header, BorderLayout.NORTH);
        root.add(screen, BorderLayout.CENTER);
        root.add(buttonPanel, BorderLayout.SOUTH);

        installKeyboardShortcuts();
        setVisible(true);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBackground(getButtonBackground(text));
        button.setForeground(getButtonForeground(text));
        button.addActionListener(listener);
        button.setPreferredSize(new Dimension(82, 58));
        return button;
    }

    private Color getButtonBackground(String text) {
        if ("=".equals(text)) {
            return EQUAL_BG;
        }
        if (text.matches("[+\\-*/]")) {
            return OPERATOR_BG;
        }
        if ("MC".equals(text) || "MR".equals(text) || "M+".equals(text) || "M-".equals(text)) {
            return MEMORY_BG;
        }
        if ("C".equals(text) || "CE".equals(text) || "⌫".equals(text) || "±".equals(text)) {
            return CONTROL_BG;
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
        if ("MC".equals(text) || "MR".equals(text) || "M+".equals(text) || "M-".equals(text)) {
            return MEMORY_FG;
        }
        if ("C".equals(text) || "CE".equals(text) || "⌫".equals(text) || "±".equals(text)) {
            return CONTROL_FG;
        }
        return DIGIT_FG;
    }

    private void installKeyboardShortcuts() {
        getRootPane().setFocusable(true);
        getRootPane().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();

                if ((keyChar >= '0' && keyChar <= '9') || keyChar == '.') {
                    pressButton(String.valueOf(keyChar));
                    return;
                }

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ADD:
                    case KeyEvent.VK_PLUS:
                        pressButton("+");
                        break;
                    case KeyEvent.VK_SUBTRACT:
                    case KeyEvent.VK_MINUS:
                        pressButton("-");
                        break;
                    case KeyEvent.VK_MULTIPLY:
                        pressButton("*");
                        break;
                    case KeyEvent.VK_DIVIDE:
                    case KeyEvent.VK_SLASH:
                        pressButton("/");
                        break;
                    case KeyEvent.VK_ENTER:
                        pressButton("=");
                        break;
                    case KeyEvent.VK_BACK_SPACE:
                        pressButton("⌫");
                        break;
                    case KeyEvent.VK_ESCAPE:
                        pressButton("C");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void pressButton(String text) {
        resetIfError();
        handleInput(text);
    }

    private void updateStatus() {
        if (error) {
            statusLabel.setText("ERROR");
            return;
        }
        if (memory != 0) {
            statusLabel.setText("MEM " + format(memory));
            return;
        }
        if (!operator.isEmpty()) {
            statusLabel.setText("" + format(num1) + " " + operator);
            return;
        }
        statusLabel.setText(startNew ? "READY" : "TYPING");
    }

    private Double currentEntryValue() {
        if (typed.length() > 0) {
            return Double.parseDouble(typed.toString());
        }
        String visible = display.getText().trim();
        if (visible.isEmpty() || "Error".equalsIgnoreCase(visible)) {
            return null;
        }
        return Double.parseDouble(visible);
    }

    private void setDisplayValue(double value) {
        String formatted = format(value);
        display.setText(formatted);
        typed.setLength(0);
        typed.append(formatted);
        startNew = true;
        updateStatus();
    }

    private void clearAll() {
        display.setText("0");
        typed.setLength(0);
        num1 = 0;
        lastNum2 = 0;
        operator = "";
        lastOperator = "";
        startNew = true;
        error = false;
        updateStatus();
    }

    private void clearEntry() {
        typed.setLength(0);
        display.setText("0");
        startNew = true;
        updateStatus();
    }

    private void backspace() {
        if (startNew) {
            return;
        }
        if (typed.length() > 0) {
            typed.deleteCharAt(typed.length() - 1);
        }
        display.setText(typed.length() == 0 ? "0" : typed.toString());
        if (typed.length() == 0) {
            startNew = true;
        }
        updateStatus();
    }

    private void toggleSign() {
        Double value = currentEntryValue();
        if (value == null) {
            return;
        }
        setDisplayValue(-value);
    }

    private void applyMemoryAdd(double value) {
        memory += value;
        updateStatus();
    }

    private void applyMemorySubtract(double value) {
        memory -= value;
        updateStatus();
    }

    private void recallMemory() {
        setDisplayValue(memory);
    }

    private void handleInput(String text) {
        if (text.matches("[0-9.]")) {
            if (startNew) {
                typed.setLength(0);
                startNew = false;
                if (".".equals(text)) typed.append("0");
            }
            if (".".equals(text) && typed.indexOf(".") >= 0) return;
            if ("0".equals(text) && typed.length() == 1 && typed.charAt(0) == '0') return;
            typed.append(text);
            display.setText(typed.toString());
            updateStatus();
            return;
        }

        switch (text) {
            case "C":
                clearAll();
                return;
            case "CE":
                clearEntry();
                return;
            case "⌫":
                backspace();
                return;
            case "±":
                toggleSign();
                return;
            case "MC":
                memory = 0;
                updateStatus();
                return;
            case "MR":
                recallMemory();
                return;
            case "M+":
            case "M-": {
                Double value = currentEntryValue();
                if (value == null) {
                    return;
                }
                if ("M+".equals(text)) {
                    applyMemoryAdd(value);
                } else {
                    applyMemorySubtract(value);
                }
                return;
            }
        }

        if (text.matches("[+\\-*/]")) {
            try {
                num1 = Double.parseDouble(typed.length() == 0 ? display.getText() : typed.toString());
                operator = text;
                lastOperator = text;
                typed.setLength(0);
                startNew = true;
                display.setText(format(num1) + " " + operator + " ");
                updateStatus();
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
                updateStatus();
            } catch (Exception ex) {
                showError();
            }
        }
    }

    private void resetIfError() {
        if (!error) return;
        display.setText("");
        typed.setLength(0);
        operator = "";
        num1 = 0;
        startNew = true;
        error = false;
        updateStatus();
    }

    private void showError() {
        display.setText("Error");
        typed.setLength(0);
        operator = "";
        startNew = true;
        error = true;
        updateStatus();
    }

    private String format(double value) {
        return value == (long) value ? String.valueOf((long) value) : String.valueOf(value);
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetIfError();
            handleInput(((JButton) e.getSource()).getText());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calc::new);
    }
}