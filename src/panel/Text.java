package panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Text {
    private String text;
    private int x;
    private int y;
    private boolean centered;

    public Text(String text, int x, int y) {
        this(text, x, y, true);
    }

    public Text(String text, int x, int y, boolean centered) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.centered = centered;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isCentered() {
        return centered;
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
    }

    public void draw(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.PLAIN, 48));
        g2.setColor(Color.BLACK);
        String[] lines = text.split("\n");
        int lineHeight = g2.getFontMetrics().getHeight();

        if (centered) {
            int totalHeight = lines.length * lineHeight;
            int startY = y - (totalHeight / 2);
            for (int i = 0; i < lines.length; i++) {
                int lineWidth = g2.getFontMetrics().stringWidth(lines[i]);
                int startX = x - (lineWidth / 2);
                g2.drawString(lines[i], startX, startY + (i * lineHeight) + g2.getFontMetrics().getAscent());
            }
        } else {
            for (int i = 0; i < lines.length; i++) {
                g2.drawString(lines[i], x, y + (i * lineHeight));
            }
        }
    }
}
