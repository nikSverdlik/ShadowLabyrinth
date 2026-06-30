import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class GamePanel extends JPanel {
    Player player;
    Color[][] tileColors = new Color[20][20];
    List<Wall> walls;
    BufferedImage floorImage;
    Random random = new Random();
    float darkness = 0.0f;
    float targetDarkness = 0.0f;
    BufferedImage lightMask;
    boolean lightsOn = true;
    Move input;
    Timer lightTimer;
    Timer gameTimer;
    List<Keys> keys;
    int keysCollected = 0;
    int totalKeys = 5;
    Exit exit;
    boolean gameWon = false;
    List<Enemy> enemies;
    boolean gameLost = false;
    UI ui;
    boolean gameEnded = false;
    int level = 1;
    long startTime;

    public GamePanel(UI ui) {
        this.ui = ui;
        player = new Player(400, 450);
        setBackground(Color.BLACK);
        setFocusable(true);
        input = new Move();

        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                int shade = random.nextInt(60) + 60;
                tileColors[row][col] = new Color(shade, shade, shade);
            }
        }

        walls = Wall.createMaze(1000, 1000);
        enemies = new ArrayList<>();
        int enemyCount = 4;

        while (enemies.size() < enemyCount ) {
            int ex = 30 + random.nextInt(940);
            int ey = 30 + random.nextInt(940);

            Rectangle enemyBounds = new Rectangle(ex - 15, ey - 15, 30, 30);
            boolean blocked = false;

            for (Wall wall : walls) {
                if (enemyBounds.intersects(wall.getBounds())) {
                    blocked = true;
                    break;
                }
            }

            if (!blocked) {
                enemies.add(new Enemy(ex, ey));
            }
        }
        keys = Keys.generateKeys(walls, totalKeys);
        exit = new Exit(976, 400);

        stoneFloor();

        addKeyListener(input);

    }

    /**
     * Обрабатывает движение игрока, сбор ключей, столкновения с врагами
     * и взаимодействие с выходом. Вызывается каждый кадр.
     */
    void movePlayer() {
        if (gameEnded) return;

        player.movePlayer(input.up, input.down, input.left, input.right, getHeight(), getWidth(), walls);

        Rectangle playerBounds = new Rectangle(player.x - player.radius, player.y - player.radius,
                player.radius * 2, player.radius * 2);
        for (Keys key : keys) {
            if (!key.collected && playerBounds.intersects(key.getBounds())) {
                key.collected = true;
                keysCollected++;
            }
        }

        if (keysCollected == totalKeys) {
            exit.visible = true;
        }

        if (!gameWon && !gameLost) {
            for (Enemy enemy : enemies) {
                enemy.move(walls, getWidth(), getHeight());

                if (playerBounds.intersects(enemy.getBounds())) {
                    gameLost = true;
                    gameTimer.stop();

                    if (!gameEnded) {
                        gameEnded = true;
                        long finalTime = System.currentTimeMillis() - startTime;
                        int min = (int)(finalTime / 60000);
                        int sec = (int)((finalTime / 1000) % 60);
                        int ms = (int)((finalTime % 1000) / 10);
                        String time = String.format("%d:%02d:%02d", min, sec, ms);

                        input.up = false;
                        input.down = false;
                        input.left = false;
                        input.right = false;

                        repaint();
                        ui.showMenu(false, level, time, "");
                    }
                    return;
                }
            }
        }

        if (exit.visible) {
            if (playerBounds.intersects(exit.getBounds())) {
                exit.open = true;
                gameWon = true;
                gameTimer.stop();

                long levelTime = System.currentTimeMillis() - startTime;
                SaveData.saveBestTime(level, levelTime);

                if (!gameEnded) {
                    gameEnded = true;

                    int min = (int)(levelTime / 60000);
                    int sec = (int)((levelTime / 1000) % 60);
                    int ms = (int)((levelTime % 1000) / 10);
                    String time = String.format("%d:%02d:%02d", min, sec, ms);

                    String recordForThisLevel = SaveData.getBestTimeForLevel(level);

                    input.up = false;
                    input.down = false;
                    input.left = false;
                    input.right = false;

                    repaint();
                    ui.showMenu(true, level, time, recordForThisLevel);
                }
                return;
            }
        }
    }

    /**
     * Запускает основной игровой цикл. Таймер срабатывает каждые 16 мс (60 FPS).
     * Управляет темнотой, движением игрока и перерисовкой.
     */
    void startGameLoop() {
        gameTimer = new Timer(16, e -> {
            // обновляем темноту
            if (darkness < targetDarkness) {
                darkness += 0.005f;
                if (darkness > 1.0f) {
                    darkness = 1.0f;
                }
            }
            movePlayer();
            repaint();
        });
        gameTimer.start();
    }

    /**
     * Запускает таймер для затемнения экрана. Через 25 мс свет гаснет.
     */
    void setupLightTimer() {
        lightTimer = new Timer(25, e -> {
            lightsOn = false;
            targetDarkness = 1.0f;
            lightTimer.stop();
        });
        lightTimer.setRepeats(false);
        lightTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(floorImage, 0, 0, null);

        for (Wall wall : walls) {
            wall.draw(g);
        }

        if (keys != null) {
            for (Keys key : keys) {
                key.draw(g);
            }
        }

        player.draw(g);

        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        exit.draw(g);

        if (darkness > 0.0f) {
            createLightMask();
            g.drawImage(lightMask, 0, 0, null);
        }

        if (!gameLost && !gameWon) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Ключи: " + keysCollected + "/" + totalKeys, 850, 30);

            long elapsed = System.currentTimeMillis() - startTime;
            int minutes = (int) (elapsed / 60000);
            int seconds = (int) ((elapsed / 1000) % 60);
            int millis = (int) ((elapsed % 1000) / 10);

            String timeText = String.format("%d:%02d:%02d", minutes, seconds, millis);
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRoundRect(10, 10, 130, 35, 10, 10);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.BOLD, 22));
            g.drawString(timeText, 20, 35);
        }

    }

    /**
     * Создает маску освещения: черный фон с прозрачным кругом вокруг игрока.
     * Используется RadialGradientPaint для плавного перехода от темноты к свету.
     */
    void createLightMask() {
        // создаём картинку один раз
        if (lightMask == null) {
            lightMask = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D g2d = lightMask.createGraphics();

        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, 1000, 1000);

        // заливаем чёрным
        g2d.setComposite(AlphaComposite.SrcOver);
        int alpha = Math.min(255, (int)(255 * darkness));
        g2d.setColor(new Color(0, 0, 0, alpha));
        g2d.fillRect(0, 0, 1000, 1000);

        //круг света вокруг игрока
        g2d.setComposite(AlphaComposite.DstOut);

        Point2D center = new Point2D.Float(player.x, player.y);

        float[] dist = {0.0f, 0.4f, 0.7f, 1.0f};
        Color[] colors = {
                new Color(0, 0, 0, Math.min(255, (int)(255 * darkness))),
                new Color(0, 0, 0, Math.min(255, (int)(200 * darkness))),
                new Color(0, 0, 0, Math.min(255, (int)(100 * darkness))),
                new Color(0, 0, 0, 0)
        };

        RadialGradientPaint gradient = new RadialGradientPaint(center, 100, dist, colors);

        g2d.setPaint(gradient);
        g2d.fillOval(player.x - 100,  player.y - 100, 200, 200);

        g2d.dispose();
    }

    /**
     * Генерирует текстуру каменного пола из плиток с трещинами.
     */
    void stoneFloor() {
        floorImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = floorImage.createGraphics();

        int tileSize = 50;

        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                int x = col * tileSize;
                int y = row * tileSize;

                g2d.setColor(tileColors[row][col]);
                g2d.fillRect(x, y, tileSize, tileSize);

                g2d.setColor(new Color(
                        tileColors[row][col].getRed() - 20,
                        tileColors[row][col].getGreen() - 20,
                        tileColors[row][col].getBlue() - 20
                ));

                for (int i = 0; i < 3; i++) {
                    int spotX = x + random.nextInt(40) + 5;
                    int spotY = y + random.nextInt(40) + 5;
                    int spotSize = random.nextInt(8) + 3;
                    g2d.fillOval(spotX, spotY, spotSize, spotSize);
                }

                g2d.setColor(new Color(40, 40, 40));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRect(x, y, tileSize, tileSize);

                // трещина на некоторых плитках
                if (random.nextInt(100) < 30) {
                    g2d.setColor(new Color(30, 30, 30));
                    int crackX = x + random.nextInt(30) + 10;
                    int crackY = y + random.nextInt(30) + 10;
                    g2d.drawLine(crackX, crackY,
                            crackX + random.nextInt(20) - 10,
                            crackY + random.nextInt(20) - 10);
                }
            }
        }
        g2d.dispose();
    }

    public void nextLevel() {
        level++;
        totalKeys += 2;
        startTime = System.currentTimeMillis();
        resetGame();
    }

    public void restartGame() {
        resetGame();
    }

    /**
     * Сбрасывает состояние игры: заново генерирует карту, ключи, врагов.
     */
    void resetGame() {
        startTime = System.currentTimeMillis();
        gameWon = false;
        gameLost = false;
        gameEnded = false;
        keysCollected = 0;
        darkness = 0;
        targetDarkness = 0;
        lightsOn = true;

        walls = Wall.createMaze(1000, 1000);
        keys = Keys.generateKeys(walls, totalKeys);

        enemies.clear();
        int enemyCount = 3 + level;
        int att = 0;
        while (enemies.size() < enemyCount) {
            int ex = 30 + random.nextInt(940);
            int ey = 30 + random.nextInt(940);
            Rectangle eb = new Rectangle(ex - 15, ey - 15, 30, 30);
            boolean blocked = false;
            for (Wall w : walls) {
                if (eb.intersects(w.getBounds())) { blocked = true; break; }
            }
            if (!blocked) enemies.add(new Enemy(ex, ey));
        }

        exit = new Exit(976, 400);
        player.x = 400;
        player.y = 450;

        stoneFloor();
        if (gameTimer != null) gameTimer.stop();
        if (lightTimer != null) lightTimer.stop();
        setupLightTimer();
        startGameLoop();
        repaint();
    }

    public void startGame() {
        startTime = System.currentTimeMillis();
        setupLightTimer();
        startGameLoop();
        requestFocusInWindow();
    }

}