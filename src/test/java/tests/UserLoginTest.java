package tests;

import api.ApiSteps;
import io.qameta.allure.Owner;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import models.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("Auth")
@DisplayName("Tests for user login")
@Owner("Anosov Aleksandr")
@Feature("User Authorization")
public class UserLoginTest extends TestBase {

    @Test
    @DisplayName("Authorization of a random user")
    @Owner("Anosov Aleksandr")
    @Description("Test for successful authorization of a randomly generated user.")
    @Story("User Authorization")
    void userAuthorizationTest() {
        // Регистрация нового пользователя
        ApiSteps.registerUser();

        // Получение токена авторизации
        LoginResponseModel loginResponse = ApiSteps.getToken();

        // Проверка, что токен не null
        assertThat(loginResponse.getToken()).isNotNull();
        System.out.println("Generated Token: " + loginResponse.getToken());  // Вывод токена для отладки

        // Установка cookies в браузере
        ApiSteps.setCookiesInBrowser(loginResponse);

        // Проверка, что cookies установлены корректно
        String userIdFromCookies = ApiSteps.extractValueFromCookieString("userID");
        String tokenFromCookies = ApiSteps.extractValueFromCookieString("token");

        assertThat(userIdFromCookies).isEqualTo(loginResponse.getUserId());
        assertThat(tokenFromCookies).isEqualTo(loginResponse.getToken());

        ApiSteps.getUserData();
    }

    @Test
    @DisplayName("Authorization with invalid credentials")
    @Owner("Anosov Aleksandr")
    @Description("Test for handling invalid authorization with incorrect token.")
    @Story("User Authorization")
    @Feature("User account")
    void invalidUserAuthorizationTest() {
        // Регистрация нового пользователя
        ApiSteps.registerUser();

        // Получение токена авторизации
        LoginResponseModel loginResponse = ApiSteps.getToken();

        // Установка cookies в браузере
        ApiSteps.setCookiesInBrowser(loginResponse);

        // Попытка получить данные пользователя с некорректным токеном
        Response unauthorizedResponse = ApiSteps.getUserDataUnauthorized();

        // Проверка, что статус-код ответа 401 Unauthorized
        assertThat(unauthorizedResponse.getStatusCode()).isEqualTo(401);

        // Вывод сообщения об ошибке для отладки
        String errorMessage = unauthorizedResponse.jsonPath().getString("message");
        System.out.println("Error Message: " + errorMessage);
    }
}