import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JButton {

    private boolean hover = false;
    private boolean pressed = false;

    public Button(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);

        // Hover efekt
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Anti-aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Barva pozadí
        Color bg;
        if (pressed) {
            bg = new Color(140, 0, 140);
        } else if (hover) {
            bg = new Color(180, 0, 180);
        } else {
            bg = new Color(200, 0, 200);
        }
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        // Text
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getAscent();

        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + textHeight) / 2 - 3;

        g2.drawString(getText(), x, y);

        g2.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Custom Button Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 450);
            frame.setLocationRelativeTo(null);
            frame.setLayout(null);

            Button button = new Button("Klikni");
            button.setBounds(50, 50, 150, 50);

            frame.add(button);
            frame.setVisible(true);
        });
    }
}

