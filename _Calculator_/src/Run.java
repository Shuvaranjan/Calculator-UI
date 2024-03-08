import javax.swing.SwingUtilities;

import GUI.CalculatorGui;

public class Run {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CalculatorGui().setVisible(true);
            }
        });
    }
}
