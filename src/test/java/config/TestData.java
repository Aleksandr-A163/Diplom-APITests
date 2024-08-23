package config;

import java.util.Random;

public class TestData {

    // Массив значений ISBN
    private static final String[] ISBN_VALUES = {
        "9781593275846",
        "9781449337711",
        "9781593277574",
        "9781449325862",
        "9781449331818",
    };

    // Метод для случайного выбора ISBN
    public static String getRandomIsbn() {
        Random random = new Random();
        String selectedIsbn = ISBN_VALUES[random.nextInt(ISBN_VALUES.length)];

        // Проверка, что значение не null и не пустое
        if (selectedIsbn == null || selectedIsbn.isEmpty()) {
            throw new IllegalStateException("Selected ISBN cannot be null or empty");
        }
        return selectedIsbn;
    }
}