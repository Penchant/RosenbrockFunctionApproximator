import javax.swing.*;
import java.awt.*;

public class Main extends JPanel {

    Network network;

    public Main () {
        JFrame frame = createWindow("Rosenbrock Function Approximation", 800, 800);
        addContentToWindow(frame);

        network = new Network(0, 0, 0, false);
    }

    private JFrame createWindow (String title, int width, int height) {
        JFrame frame = new JFrame();

        frame.setTitle(title);
        frame.getContentPane().setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        return frame;
    }

    private void addContentToWindow(JFrame frame) {
        frame.add(this);
    }

    public static void main(String[] args) {
        new Main();
    }

}
