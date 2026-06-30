import java.awt.*;
import java.util.List;

public class Player {
    int x;
    int y;
    int speed = 3;
    int radius = 20;
    int direction = 0;      // 0=вниз, 1=влево, 2=вверх, 3=вправо
    boolean moving = false;
    int animFrame = 0;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(20, 20, 20));
        int legOffset = moving ? (animFrame % 10 < 5 ? 4 : -4) : 0;

        g2d.fillRect(x - 8, y + 6, 6, 10 + legOffset);
        g2d.fillRect(x + 2, y + 6, 6, 10 - legOffset);

        g2d.setColor(new Color(60, 30, 10));
        g2d.fillRect(x - 9, y + 14 + legOffset, 8, 4);
        g2d.fillRect(x + 1, y + 14 - legOffset, 8, 4);

        g2d.setColor(new Color(30, 80, 160));
        g2d.fillRoundRect(x - 10, y - 10, 20, 20, 6, 6);

        int armOffset = moving ? (animFrame % 10 < 5 ? -3 : 3) : 0;
        g2d.setColor(new Color(30, 80, 160));
        g2d.fillRect(x - 15, y - 8 + armOffset, 5, 12);
        g2d.fillRect(x + 10, y - 8 - armOffset, 5, 12);

        g2d.setColor(new Color(255, 220, 180));
        g2d.fillOval(x - 16, y + 2 + armOffset, 7, 6);
        g2d.fillOval(x + 9, y + 2 - armOffset, 7, 6);

        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillOval(x - 8, y - 5, 16, 16);

        g2d.setColor(new Color(255, 220, 180));
        g2d.fillOval(x - 9, y - 23, 18, 18);

        int eyeX1 = x - 4, eyeX2 = x + 1;
        int eyeY = y - 19;

        switch (direction) {
            case 0: eyeY = y - 18; break;
            case 2: eyeY = y - 20; break;
            case 1: eyeX1 = x - 5; eyeX2 = x - 1; break;
            case 3: eyeX1 = x - 2; eyeX2 = x + 2; break;
        }

        g2d.setColor(Color.WHITE);
        g2d.fillOval(eyeX1 - 1, eyeY - 1, 6, 6);
        g2d.fillOval(eyeX2 - 1, eyeY - 1, 6, 6);

        g2d.setColor(Color.BLACK);
        g2d.fillOval(eyeX1 + 1, eyeY + 1, 4, 4);
        g2d.fillOval(eyeX2 + 1, eyeY + 1, 4, 4);

        g2d.setColor(new Color(80, 50, 20));
        g2d.fillOval(x - 9, y - 26, 18, 10);

        g2d.fillRect(x - 5, y - 28, 10, 4);
    }

    void movePlayer(boolean up, boolean down, boolean left, boolean right,
                    int height, int width, List<Wall> walls) {

        moving = up || down || left || right;

        if (up) {
            direction = 2;
            int newY = y - speed;
            if (newY >= radius) {
                Rectangle bounds = new Rectangle(x - radius, newY - radius, radius * 2, radius * 2);
                if (!collidesWithWall(bounds, walls)) {
                    y = newY;
                }
            }
        }

        if (down) {
            direction = 0;
            int newY = y + speed;
            if (newY <= height - radius) {
                Rectangle bounds = new Rectangle(x - radius, newY - radius, radius * 2, radius * 2);
                if (!collidesWithWall(bounds, walls)) {
                    y = newY;
                }
            }
        }

        if (left) {
            direction = 1;
            int newX = x - speed;
            if (newX >= radius) {
                Rectangle bounds = new Rectangle(newX - radius, y - radius, radius * 2, radius * 2);
                if (!collidesWithWall(bounds, walls)) {
                    x = newX;
                }
            }
        }

        if (right) {
            direction = 3;
            int newX = x + speed;
            if (newX <= width - radius) {
                Rectangle bounds = new Rectangle(newX - radius, y - radius, radius * 2, radius * 2);
                if (!collidesWithWall(bounds, walls)) {
                    x = newX;
                }
            }
        }

        if (moving) {
            animFrame++;
        }
    }

    boolean collidesWithWall(Rectangle bounds, List<Wall> walls) {
        for (Wall wall : walls) {
            if (bounds.intersects(wall.getBounds())) {
                return true;
            }
        }
        return false;
    }
}
