import java.awt.*;
import java.util.List;
import java.util.Random;

public class Enemy {
    int x, y;
    int speed = 1;
    int dx, dy;
    Random rand = new Random();

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        changeDirection();
    }

    /**
     * Меняет направление движения на случайное.
     */
    void changeDirection() {
        int[][] dirs = {{0, speed}, {0, -speed}, {speed, 0}, {-speed, 0}};
        int[] d = dirs[rand.nextInt(4)];
        dx = d[0];
        dy = d[1];
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // тело призрака
        g2.setColor(new Color(255, 80, 80, 200));
        g2.fillOval(x - 12, y - 15, 24, 24);

        // хвост призрака
        g2.fillOval(x - 10, y, 8, 10);
        g2.fillOval(x - 4, y + 2, 8, 10);
        g2.fillOval(x + 2, y, 8, 10);

        // глаза
        g2.setColor(Color.WHITE);
        g2.fillOval(x - 6, y - 10, 8, 8);
        g2.fillOval(x + 2, y - 10, 8, 8);

        // зрачки
        g2.setColor(Color.BLACK);
        g2.fillOval(x - 3, y - 8, 4, 4);
        g2.fillOval(x + 5, y - 8, 4, 4);
    }

    /**
     * Двигает врага в текущем направлении.
     * Если враг упирается в стену или границу экрана, меняет направление.
     *
     * @param walls стены для проверки столкновений
     * @param w ширина экрана
     * @param h высота экрана
     */
    public void move(List<Wall> walls, int w, int h) {
        int nx = x + dx;
        int ny = y + dy;

        if (nx < 20 || nx > w - 20 || ny < 20 || ny > h - 20) {
            changeDirection();
            return;
        }

        Rectangle b = new Rectangle(nx - 10, ny - 10, 20, 20);
        for (Wall wall : walls) {
            if (b.intersects(wall.getBounds())) {
                changeDirection();
                return;
            }
        }

        x = nx;
        y = ny;

        if (rand.nextInt(100) < 1) {
            changeDirection();
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x - 10, y - 10, 20, 20);
    }
}
