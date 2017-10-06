import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends JPanel {

    JFrame frame = new JFrame();

    public static float time = 0;

    public Main () {
        frame.add(this);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 0.01f));
        frame.getContentPane().setPreferredSize(new Dimension(800, 800));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.repaint();

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
        });

        javax.swing.Timer timer = new javax.swing.Timer (10, al -> {
            time+=0.01;
            frame.repaint();
        });
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2f));
        g2d.setColor(new Color(255, 2, 2, 255));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        drawCircle (g2d, 400, 400, 400);
        draw (g2d, 400, 400, 400);
    }

    private void draw(Graphics2D g, float x, float y, float d) {
        if(d > 50) {
            draw(g, x + d / 2, y + d / 2, d / 4);
            draw(g, x - d / 2, y - d / 2, d / 4);
            draw(g, x - d / 2, y + d / 2, d / 4);
            draw(g, x + d / 2, y - d / 2, d / 4);
        }
        if(d > 2) {
            drawCircle(g, x + d / 2, y + d / 2, d / 4);
            drawCircle(g, x - d / 2, y - d / 2, d / 4);
            drawCircle(g, x - d / 2, y + d / 2, d / 4);
            drawCircle(g, x + d / 2, y - d / 2, d / 4);
        }

        g.setColor(new Color(1f, 0, 0, clamp(1f - d / 800f, 0, 1)));
        g.fillOval((int) (x - d / 2), (int) (y - d / 2), (int) d, (int) d);
        g.setColor(new Color(0f, 0f, 0f, 1f));
        g.drawOval((int) (x - d / 2), (int) (y - d / 2), (int) d, (int) d);
}

    private void drawCircle (Graphics2D g, float x, float y, float d) {
        if(d > 2) {
            if(Math.sin(time) < 0) {
                drawCircle(g, x + d / 2, y, d / 2 * (float) Math.abs(Math.sin(time)) + 0.1f);
            } else {
                drawCircle(g, x - d / 2, y, d / 2 * (float) Math.abs(Math.sin(time)) + 0.1f);
            }
            if(Math.cos(time) < 0) {
                drawCircle(g, x, y - d / 2, d / 2 * (float) Math.abs(Math.cos(time)) + 0.1f);
            } else {
                drawCircle(g, x, y + d / 2, d / 2 * (float) Math.abs(Math.cos(time)) + 0.1f);
            }
        }

        g.setColor(new Color(1f, 0, 0, clamp(1f - d / 800f, 0, 1)));
        g.fillOval((int) (x - d / 2), (int) (y - d / 2), (int) d, (int) d);
        g.setColor(new Color(0f, 0f, 0f, 1f));
        g.drawOval((int) (x - d / 2), (int) (y - d / 2), (int) d, (int) d);
    }

    private static float clamp (float value, float min, float max) {
        return value < min ? min : value > max ? max : value;
    }

    public static void main(String[] args) {
        new Main();
    }

}
