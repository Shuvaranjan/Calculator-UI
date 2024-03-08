package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import Constants.CommonConstants;

public class CalculatorGui extends JFrame implements ActionListener {
    private final SpringLayout springLayout = new SpringLayout();
    private CalculatorService calculatorService;

    // display field
    private JTextField displayField;

    // buttons
    private JButton[] buttons;

    // flags
    private boolean pressedOperator = false;
    private boolean pressedEquals = false;

    public CalculatorGui() {
        super(CommonConstants.App_Name);
        setSize(CommonConstants.App_Size[0], CommonConstants.App_Size[1]);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(springLayout);
        getContentPane().setBackground(CommonConstants.BACKGROUND_COLOR);

        ImageIcon icon = new ImageIcon("_Calculator_\\src\\--MEDIA--\\icons8-calculator-80.png");
        this.setIconImage(icon.getImage());

        calculatorService = new CalculatorService();

        addGuiComponents();
    }

    public void addGuiComponents() {
        // Add Display Components
        addDisplayFieldComponents();

        // Add Button Components
        addButtonComponents();
    }

    public void addDisplayFieldComponents() {
        JPanel displayfieldpanel = new JPanel();
        displayfieldpanel.setBackground(CommonConstants.BACKGROUND_COLOR);
        displayField = new JTextField(CommonConstants.TEXTFIELD_LENGTH);
        displayField.setFont(new Font("Dialog", Font.BOLD, CommonConstants.TEXTFIELD_FONTSIZE));
        displayField.setText("0");
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);
        displayField.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        displayField.setBackground(Color.decode("#26C9CD"));
        displayField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        displayField.setEditable(false);

        displayfieldpanel.add(displayField);
        this.getContentPane().add(displayfieldpanel);

        springLayout.putConstraint(SpringLayout.NORTH, displayfieldpanel,
                CommonConstants.TEXTFIELD_SPRINGLAYOUT_NORTHPAD, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, displayfieldpanel, CommonConstants.TEXTFIELD_SPRINGLAYOUT_WESTPAD,
                SpringLayout.WEST, this);
    }

    public void addButtonComponents() {

        GridLayout gridLayout = new GridLayout(CommonConstants.BOTTOM_ROWCOUNT, CommonConstants.BOTTOM_COLCOUNT);

        JPanel buttonPanel = new JPanel();
        buttons = new JButton[CommonConstants.BOTTOM_COUNT];
        buttonPanel.setBackground(CommonConstants.BACKGROUND_COLOR);
        buttonPanel.setLayout(gridLayout);
        for (int i = 0; i < CommonConstants.BOTTOM_COUNT; i++) {
            JButton button = new JButton(getButtonLabel(i));
            button.setFont(new Font("Times of New Roman", Font.PLAIN, CommonConstants.BOTTOM_FONTSIZE));
            button.addActionListener(this);
            button.setBackground(Color.decode("#212121"));
            button.setForeground(CommonConstants.FOREGROUND_COLOR);
            button.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(Color.decode("#FF0101"));

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(Color.decode("#212121"));

                }

            });

            buttonPanel.add(button);

        }

        gridLayout.setHgap(CommonConstants.BUTTON_HGAP);
        gridLayout.setVgap(CommonConstants.BUTTON_VGAP);
        this.getContentPane().add(buttonPanel);

        springLayout.putConstraint(SpringLayout.NORTH, buttonPanel, CommonConstants.BUTTON_SPRINGLAYOUT_NORTHPAD,
                SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, buttonPanel, CommonConstants.BUTTON_SPRINGLAYOUT_WESTPAD,
                SpringLayout.WEST, this);

    }

    public String getButtonLabel(int buttonIndex) {
        switch (buttonIndex) {
            case 0:

                return "C";

            case 1:

                return "⌫";

            case 2:

                return "%";

            case 3:

                return "÷";

            case 4:

                return "7";

            case 5:

                return "8";

            case 6:

                return "9";

            case 7:

                return "x";

            case 8:

                return "4";

            case 9:

                return "5";

            case 10:

                return "6";

            case 11:

                return "-";

            case 12:

                return "1";

            case 13:

                return "2";

            case 14:

                return "3";

            case 15:

                return "+";

            case 16:

                return "0";

            case 17:

                return ".";

            case 18:

                return "=";

        }
        return "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonCommand = e.getActionCommand();
        if (buttonCommand.matches("[0-9]")) {
            if (pressedEquals || pressedOperator || displayField.getText().equals("0"))
                displayField.setText(buttonCommand);
            else
                displayField.setText(displayField.getText() + buttonCommand);

            // update flags
            pressedOperator = false;
            pressedEquals = false;
        } else if (buttonCommand.equals("C")) {
            if (!displayField.getText().contains("C")) {
                displayField.setText("0");
            }
        } else if (buttonCommand.equals("⌫")) {
            if (Double.parseDouble(displayField.getText()) != 0) {
                displayField.setText(displayField.getText().substring(0, displayField.getText().length() - 1));

            }

        }

        else if (buttonCommand.equals("=")) {
            // calculate
            calculatorService.setNum2(Double.parseDouble(displayField.getText()));

            double result = 0;
            switch (calculatorService.getMathSymbol()) {
                case '+':
                    result = calculatorService.add();
                    break;
                case '-':
                    result = calculatorService.subtract();
                    break;
                case '÷':
                    result = calculatorService.divide();
                    break;
                case 'x':
                    result = calculatorService.multiply();
                    break;
                case '%':
                    result = calculatorService.modulus();
            }

            // update the display field
            displayField.setText(Double.toString(result));

            // update flags
            pressedEquals = true;
            pressedOperator = false;

        } else if (buttonCommand.equals(".")) {
            if (!displayField.getText().contains(".")) {
                displayField.setText(displayField.getText() + buttonCommand);
            }
        } else { // operator
            if (!pressedOperator)
                calculatorService.setNum1(Double.parseDouble(displayField.getText()));

            calculatorService.setMathSymbol(buttonCommand.charAt(0));

            // update flags
            pressedOperator = true;
            pressedEquals = false;
        }
    }
}
