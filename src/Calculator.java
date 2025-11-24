import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator {
    private JFrame frame;
    private JTextField display;
    private String currentInput = "";
    private String operator = "";
    private double firstNumber = 0;
    private boolean startNewNumber = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Calculator().createAndShowGUI();
            }
        });
    }

    public void createAndShowGUI() {
        // Create JFrame
        frame = new JFrame("Kalkulator GUI - VS Code");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        createComponents();
        setupLayout();
        frame.setVisible(true);
    }

    private void createComponents() {
        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 20));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setText("0");

        String[] buttonLabels = {
            "C", "DEL", "/", "*",
            "7", "8", "9", "-",
            "4", "5", "6", "+",
            "1", "2", "3", "=",
            "0", ".", "±", "="
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.addActionListener(new ButtonClickListener());
            
            if (label.matches("[0-9]")) {
                button.setBackground(Color.WHITE);
            } else if (label.equals("=")) {
                button.setBackground(new Color(70, 130, 180));
                button.setForeground(Color.WHITE);
            } else if (label.matches("[+\\-*/]")) {
                button.setBackground(new Color(255, 165, 0));
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(new Color(240, 240, 240));
            }
            
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        }
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        displayPanel.add(display, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));

        String[][] buttonLayout = {
            {"C", "DEL", "/", "*"},
            {"7", "8", "9", "-"},
            {"4", "5", "6", "+"},
            {"1", "2", "3", "="},
            {"0", ".", "±", "="}
        };

        for (String[] row : buttonLayout) {
            for (String label : row) {
                JButton button = new JButton(label);
                button.setFont(new Font("Arial", Font.BOLD, 16));
                button.addActionListener(new ButtonClickListener());
                styleButton(button, label);
                buttonPanel.add(button);
            }
        }

        mainPanel.add(displayPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        frame.add(mainPanel);
    }

    private void styleButton(JButton button, String label) {
        if (label.matches("[0-9]")) {
            button.setBackground(Color.WHITE);
        } else if (label.equals("=")) {
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.WHITE);
        } else if (label.matches("[+\\-*/]")) {
            button.setBackground(new Color(255, 165, 0));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(240, 240, 240));
        }
        
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String command = source.getText();

            switch (command) {
                case "0": case "1": case "2": case "3": case "4":
                case "5": case "6": case "7": case "8": case "9":
                    handleNumberInput(command);
                    break;
                case ".":
                    handleDecimal();
                    break;
                case "+": case "-": case "*": case "/":
                    handleOperator(command);
                    break;
                case "=":
                    handleEquals();
                    break;
                case "C":
                    handleClear();
                    break;
                case "DEL":
                    handleDelete();
                    break;
                case "±":
                    handleNegate();
                    break;
            }
        }

        private void handleNumberInput(String number) {
            if (startNewNumber) {
                currentInput = number;
                startNewNumber = false;
            } else {
                currentInput += number;
            }
            display.setText(currentInput);
        }

        private void handleDecimal() {
            if (startNewNumber) {
                currentInput = "0.";
                startNewNumber = false;
            } else if (!currentInput.contains(".")) {
                currentInput += ".";
            }
            display.setText(currentInput);
        }

        private void handleOperator(String op) {
            if (!currentInput.isEmpty()) {
                firstNumber = Double.parseDouble(currentInput);
            }
            operator = op;
            startNewNumber = true;
        }

        private void handleEquals() {
            if (!operator.isEmpty() && !currentInput.isEmpty()) {
                double secondNumber = Double.parseDouble(currentInput);
                double result = calculate(firstNumber, secondNumber, operator);

                if (result == (long) result) {
                    display.setText(String.format("%d", (long) result));
                    currentInput = String.valueOf((long) result);
                } else {
                    display.setText(String.format("%.6f", result).replaceAll("0*$", "").replaceAll("\\.$", ""));
                    currentInput = String.valueOf(result);
                }
                operator = "";
                startNewNumber = true;
            }
        }

        private void handleClear() {
            currentInput = "";
            operator = "";
            firstNumber = 0;
            startNewNumber = true;
            display.setText("0");
        }

        private void handleDelete() {
            if (!currentInput.isEmpty() && currentInput.length() > 1) {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
                display.setText(currentInput);
            } else if (currentInput.length() == 1) {
                currentInput = "";
                display.setText("0");
                startNewNumber = true;
            }
        }

        private void handleNegate() {
            if (!currentInput.isEmpty()) {
                if (currentInput.startsWith("-")) {
                    currentInput = currentInput.substring(1);
                } else {
                    currentInput = "-" + currentInput;
                }
                display.setText(currentInput);
            }
        }

        private double calculate(double num1, double num2, String op) {
            switch (op) {
                case "+": return num1 + num2;
                case "-": return num1 - num2;
                case "*": return num1 * num2;
                case "/": 
                    if (num2 != 0) {
                        return num1 / num2;
                    } else {
                        JOptionPane.showMessageDialog(frame, "Error: Pembagian dengan nol!", "Error", JOptionPane.ERROR_MESSAGE);
                        return 0;
                    }
                default: return 0;
            }
        }
    }
}
