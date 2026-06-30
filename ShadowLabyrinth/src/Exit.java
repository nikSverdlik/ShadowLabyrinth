import java.awt.*;

public class Exit {
    int x, y;
    boolean visible = false;
    boolean open = false;

    public Exit(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!open) {
            // закрытая дверь
            g2.setColor(new Color(60, 30, 10));
            g2.fillRoundRect(x, y, 16, 70, 4, 4);
            g2.setColor(new Color(100, 60, 30));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, 16, 70, 4, 4);

            // доски на двери
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(x + 8, y + 5, x + 8, y + 65);
            g2.drawLine(x, y + 25, x + 16, y + 25);
            g2.drawLine(x, y + 45, x + 16, y + 45);

            // замок
            g2.setColor(Color.RED);
            g2.fillOval(x + 4, y + 30, 8, 8);
            g2.setColor(Color.BLACK);
            g2.fillRect(x + 7, y + 38, 2, 4);

            if (visible) {
                g2.setColor(new Color(255, 255, 0, 80));
                g2.fillRoundRect(x - 4, y - 4, 24, 78, 8, 8);
            }
        } else {
            // открытая дверь
            g2.setColor(new Color(255, 255, 200));
            g2.fillRoundRect(x, y, 16, 70, 4, 4);
            g2.setColor(new Color(200, 200, 150));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, 16, 70, 4, 4);

            g2.setColor(new Color(255, 255, 200, 100));
            g2.fillOval(x - 12, y - 5, 40, 80);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x - 10, y, 36, 70);
    }
}
