import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SaveData implements Serializable{
    private static final String SAVE_FILE = "best_time.txt";
    private Map<Integer, Long> bestTimes;

    public SaveData() {
        this.bestTimes = new HashMap<>();
    }

    /**
     * Сохраняет время для уровня, если оно лучше предыдущего рекорда.
     *
     * @param level номер уровня
     * @param timeMillis время в миллисекундах
     */
    public static void saveBestTime(int level, long timeMillis) {
        SaveData data = load();

        Long currentBest = data.bestTimes.get(level);

        if (currentBest == null || timeMillis < currentBest) {
            data.bestTimes.put(level, timeMillis);
            saveToFile(data);
            System.out.println("Новый рекорд для уровня " + level + ": " + formatTime(timeMillis));
        }
    }

    /**
     * Возвращает лучший результат для уровня в виде строки.
     *
     * @param level номер уровня
     * @return время в формате "м:сс:мс" или "-:-:-", если рекорда нет
     */
    public static String getBestTimeForLevel(int level) {
        SaveData data = load();
        Long time = data.bestTimes.get(level);
        if (time == null) {
            return "-:-:-";
        }
        return formatTime(time);
    }

    private static String formatTime(long millis) {
        int minutes = (int) (millis / 60000);
        int seconds = (int) ((millis / 1000) % 60);
        int millisec = (int) ((millis % 1000) / 10);
        return String.format("%d:%02d:%02d", minutes, seconds, millisec);
    }

    private static void saveToFile(SaveData data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.out.println("Не удалось сохранить рекорд: " + e.getMessage());
        }
    }

    private static SaveData load() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return new SaveData();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (SaveData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Не удалось загрузить рекорд: " + e.getMessage());
            return new SaveData();
        }
    }
}
