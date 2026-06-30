import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {
    GamePanel gamePanel;
    JPanel mainPanel;
    CardLayout cardLayout;

    UI(){
        setBounds(100,100, 1016, 1039);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel menuPanel = createMenuPanel();
        mainPanel.add(menuPanel, "menu");

        gamePanel = new GamePanel(this);
        mainPanel.add(gamePanel, "game");

        add(mainPanel);
        cardLayout.show(mainPanel, "menu");
        setVisible(true);
    }

    JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(20, 20, 40));
        panel.setLayout(null);


        JLabel subtitle = new JLabel("Найди ключи и выход, пока не погас свет", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.BOLD, 25));
        subtitle.setForeground(Color.YELLOW);
        subtitle.setBounds(200, 230, 600, 30);
        panel.add(subtitle);


        JButton playBtn = new JButton("ИГРАТЬ");
        playBtn.setBounds(350, 350, 300, 60);
        playBtn.setFont(new Font("Arial", Font.BOLD, 24));
        playBtn.setBackground(new Color(0, 140, 0));
        playBtn.setForeground(Color.WHITE);
        playBtn.setFocusPainted(false);
        playBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "game");
            gamePanel.startGame();
        });
        panel.add(playBtn);


        JButton exitBtn = new JButton("ВЫХОД");
        exitBtn.setBounds(350, 430, 300, 60);
        exitBtn.setFont(new Font("Arial", Font.BOLD, 24));
        exitBtn.setBackground(new Color(140, 0, 0));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        exitBtn.addActionListener(e -> System.exit(0));
        panel.add(exitBtn);

        JLabel controls = new JLabel("WASD — движение", SwingConstants.CENTER);
        controls.setFont(new Font("Arial", Font.PLAIN, 16));
        controls.setForeground(Color.GRAY);
        controls.setBounds(300, 600, 400, 30);
        panel.add(controls);

        return panel;
    }

    void showMenu(boolean won, int level, String time, String record) {
        JDialog dialog = new JDialog(this, "", true);
        dialog.setSize(400, won ? 450 : 330);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(new Color(20, 20, 40));

        JLabel title = new JLabel(won ? "ПОБЕДА!" : "ПОРАЖЕНИЕ", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(won ? new Color(0, 255, 100) : new Color(255, 80, 80));
        title.setBounds(0, 25, 400, 50);
        dialog.add(title);

        JLabel levelLabel = new JLabel("Уровень " + level, SwingConstants.CENTER);
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        levelLabel.setForeground(Color.LIGHT_GRAY);
        levelLabel.setBounds(0, 80, 400, 30);
        dialog.add(levelLabel);

        JLabel timeLabel = new JLabel("Время: " + time, SwingConstants.CENTER);
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 28));
        timeLabel.setForeground(Color.YELLOW);
        timeLabel.setBounds(0, 115, 400, 40);
        dialog.add(timeLabel);

        if (won) {
            JLabel bestLabel = new JLabel("Рекорд уровня " + level + ": " + record, SwingConstants.CENTER);
            bestLabel.setFont(new Font("Arial", Font.BOLD, 16));
            bestLabel.setForeground(new Color(255, 215, 0));
            bestLabel.setBounds(0, 165, 400, 30);
            dialog.add(bestLabel);

            JButton nextBtn = new JButton("Следующий уровень");
            nextBtn.setBounds(70, 200, 260, 45);
            nextBtn.setFont(new Font("Arial", Font.BOLD, 16));
            nextBtn.setBackground(new Color(0, 140, 0));
            nextBtn.setForeground(Color.WHITE);
            nextBtn.setFocusPainted(false);
            nextBtn.addActionListener(e -> {
                dialog.dispose();
                gamePanel.nextLevel();
            });
            dialog.add(nextBtn);

            JButton retryBtn = new JButton("Попробовать снова");
            retryBtn.setBounds(70, 250, 260, 45);
            retryBtn.setFont(new Font("Arial", Font.BOLD, 16));
            retryBtn.setBackground(new Color(0, 90, 180));
            retryBtn.setForeground(Color.WHITE);
            retryBtn.setFocusPainted(false);
            retryBtn.addActionListener(e -> {
                dialog.dispose();
                gamePanel.restartGame();
            });
            dialog.add(retryBtn);

            JButton exitBtn = new JButton("Выход");
            exitBtn.setBounds(70, 300, 260, 45);
            exitBtn.setFont(new Font("Arial", Font.BOLD, 16));
            exitBtn.setBackground(new Color(140, 0, 0));
            exitBtn.setForeground(Color.WHITE);
            exitBtn.setFocusPainted(false);
            exitBtn.addActionListener(e -> {
                dialog.dispose();
                System.exit(0);
            });
            dialog.add(exitBtn);
        } else {

            JButton retryBtn = new JButton("Попробовать снова");
            retryBtn.setBounds(70, 170, 260, 45);
            retryBtn.setFont(new Font("Arial", Font.BOLD, 16));
            retryBtn.setBackground(new Color(0, 90, 180));
            retryBtn.setForeground(Color.WHITE);
            retryBtn.setFocusPainted(false);
            retryBtn.addActionListener(e -> {
                dialog.dispose();
                gamePanel.restartGame();
            });
            dialog.add(retryBtn);

            JButton exitBtn = new JButton("Выход");
            exitBtn.setBounds(70, 225, 260, 45);
            exitBtn.setFont(new Font("Arial", Font.BOLD, 16));
            exitBtn.setBackground(new Color(140, 0, 0));
            exitBtn.setForeground(Color.WHITE);
            exitBtn.setFocusPainted(false);
            exitBtn.addActionListener(e -> {
                dialog.dispose();
                System.exit(0);
            });
            dialog.add(exitBtn);
        }

        dialog.setVisible(true);
        gamePanel.requestFocusInWindow();
    }
}
