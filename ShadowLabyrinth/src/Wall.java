import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wall {
    int x, y, width, height;

    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    void draw(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    static List<Wall> createMaze(int panelWidth, int panelHeight) {
        Random rand = new Random();
        boolean flipX = rand.nextBoolean();
        boolean flipY = rand.nextBoolean();
        boolean rotate = rand.nextBoolean();

        return buildMaze(flipX, flipY, rotate);
    }

    private static List<Wall> buildMaze(boolean flipX, boolean flipY, boolean rotate) {
        List<Wall> walls = new ArrayList<>();
        int t = 8;
        int[][] raw = {
                {0, 0, 1000, t},
                {0, 992, 1000, t},
                {0, 0, t, 1000},
                {992, 0, t, 1000},
                {250, t, t, 200},
                {250, 300, t, 50},
                {t, 250, 250, t},
                {750, t, t, 300},
                {650, 300, 100, t},
                {250, 700, t, 200},
                {t, 700, 250, t},
                {750, 700, t, 292},
                {650, 700, 100, t},
                {0, 500, 250, t},
                {350, 500, 300, t},
                {750, 500, 250, t},
                {500, 0, t, 250},
                {500, 350, t, 150},
                {500, 600, t, 392},
        };

        for (int[] w : raw) {
            int x = w[0], y = w[1], wd = w[2], ht = w[3];

            if (rotate) {
                int temp = x;
                x = y;
                y = 992 - temp - wd;
                temp = wd;
                wd = ht;
                ht = temp;
            }

            if (flipX) x = 992 - x - wd;
            if (flipY) y = 992 - y - ht;

            walls.add(new Wall(x, y, wd, ht));
        }

        return walls;
    }
}

