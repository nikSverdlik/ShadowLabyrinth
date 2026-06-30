import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Keys {
    int x, y;
    boolean collected = false;

    private static final int SIZE = 14;

    public Keys(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        if (collected) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // круглая головка
        g2.setColor(new Color(255, 215, 0));
        g2.fillOval(x - 8, y - 8, 16, 16);
        g2.setColor(new Color(180, 150, 0));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x - 8, y - 8, 16, 16);

        // дырка
        g2.setColor(Color.BLACK);
        g2.fillOval(x - 4, y - 4, 8, 8);

        // стержень
        g2.setColor(new Color(255, 215, 0));
        g2.fillRect(x + 6, y - 2, 10, 4);
        g2.setColor(new Color(180, 150, 0));
        g2.drawRect(x + 6, y - 2, 10, 4);

        // зубчики
        g2.setColor(new Color(255, 215, 0));
        g2.fillRect(x + 10, y + 2, 3, 4);
        g2.fillRect(x + 14, y + 2, 3, 4);
        g2.setColor(new Color(180, 150, 0));
        g2.drawRect(x + 10, y + 2, 3, 4);
        g2.drawRect(x + 14, y + 2, 3, 4);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - 8, y - 8, 25, 18);
    }

    public static List<Keys> generateKeys(List<Wall> walls, int count) {
        List<Keys> keys = new ArrayList<>();
        Random r = new Random();
        int attempts = 0;

        while (keys.size() < count && attempts++ < 1000) {
            int kx = 30 + r.nextInt(940);
            int ky = 30 + r.nextInt(940);
            Rectangle kb = new Rectangle(kx - 14, ky - 10, 28, 20);

            boolean ok = true;
            for (Wall w : walls) {
                if (kb.intersects(w.getBounds())) { ok = false; break; }
            }
            if (ok) keys.add(new Keys(kx, ky));
        }
        return keys;
    }
}
