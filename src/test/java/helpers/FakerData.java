package helpers;

import com.github.javafaker.Faker;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FakerData {
    private static final Faker faker = new Faker();

    public static String generatePassword(int minLength, int maxLength) {
        // Добавление переменных отвечающих за необходимые символы в пароле
        char lowerCase = faker.regexify("[a-z]").charAt(0);
        char upperCase = faker.regexify("[A-Z]").charAt(0);
        char oneDigit = faker.regexify("[0-9]").charAt(0);
        char specialChar = faker.regexify("[!@#$%^&*()]").charAt(0);

        // Генерация оставшейся части пароля
        String remainingChars = faker.internet().password(minLength - 3, maxLength - 3, true, true, true);

        // Объединение всех частей
        String combined = "" + lowerCase + upperCase + + oneDigit + specialChar + remainingChars;

        // Задание случайного сочетания частей пароля
        List<Character> passwordChars = combined.chars()
                                                .mapToObj(e -> (char)e)
                                                .collect(Collectors.toList());
        Collections.shuffle(passwordChars);

        // Перевод обратно в строку пароля
        StringBuilder passwordBuilder = new StringBuilder();
        for (char c : passwordChars) {
            passwordBuilder.append(c);
        }

        return passwordBuilder.toString();
    }

    public static String generateUsername() {
        return faker.name().username();
    }
}