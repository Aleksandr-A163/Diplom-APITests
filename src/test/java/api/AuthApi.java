package api;

import io.qameta.allure.Step;
import config.TestDataConfig;
import helpers.FakerData;
import models.*;
import static com.codeborne.selenide.Selenide.open;
import org.openqa.selenium.Cookie;
import org.aeonbits.owner.ConfigFactory;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static specs.RequestResponseSpecs.*;

public class AuthApi {

    static final TestDataConfig testDataConfig = ConfigFactory.create(TestDataConfig.class, System.getProperties());

    private static String userId;
    private static String userLogin;  // Сохраняем сгенерированный логин
    private static String userPassword;  // Сохраняем сгенерированный пароль

    private static String generateUserLogin() {
        return FakerData.generateUsername();
    }

    private static String generateUserPassword() {
        return FakerData.generatePassword(9, 16);
    }

    @Step("Create a new user")
    public static void registerUser() {
        userLogin = generateUserLogin();  // Сохраняем сгенерированный логин
        userPassword = generateUserPassword();  // Сохраняем сгенерированный пароль

        RegistrationLoginRequestModel userData = new RegistrationLoginRequestModel();
        userData.setUserName(userLogin);
        userData.setPassword(userPassword);

        Response response = given(registerAndLoginRequestSpec)
                .body(userData)
                .when()
                .post("/Account/v1/User")
                .then()
                .spec(responseSpec201)
                .extract().response();

        System.out.println("Registration Raw Response: " + response.asString());

        if (response.getContentType().contains("application/json")) {
            RegistrationResponseModel registrationResponse = response.as(RegistrationResponseModel.class);
            userId = registrationResponse.getUserId();
        } else {
            throw new IllegalStateException("Unexpected content type: " + response.getContentType());
        }
    }

    @Step("Get authorization token")
    public static LoginResponseModel getToken() {
        RegistrationLoginRequestModel userData = new RegistrationLoginRequestModel();
        userData.setUserName(userLogin);  // Используем сохраненный логин
        userData.setPassword(userPassword);  // Используем сохраненный пароль

        Response response = given(registerAndLoginRequestSpec)
                .body(userData)
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .spec(loginResponseSpec200)
                .extract().response();

        System.out.println("Token Raw Response: " + response.asString());

        if (response.getContentType().contains("application/json")) {
            LoginResponseModel loginResponse = response.as(LoginResponseModel.class);

            loginResponse.setUserId(userId);  // Устанавливаем userId

            System.out.println("Authorization Token Response: " + loginResponse);

            if (loginResponse.getUserId() == null || loginResponse.getExpires() == null || loginResponse.getToken() == null) {
                throw new IllegalArgumentException("One or more required attributes in authResponse are null");
            }

            return loginResponse;
        } else {
            throw new IllegalStateException("Unexpected content type: " + response.getContentType());
        }
    }

    @Step("Set authorization cookies")
    public static void setCookiesInBrowser(LoginResponseModel authResponse) {
        if (authResponse == null) {
            throw new IllegalArgumentException("authResponse is null");
        }
        if (authResponse.getUserId() == null || authResponse.getExpires() == null || authResponse.getToken() == null) {
            throw new IllegalArgumentException("One or more required attributes in authResponse are null");
        }

        open("/images/Toolsqa.jpg");
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.getUserId()));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.getExpires()));
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.getToken()));
    }

    public static String extractValueFromCookieString(String cookieString) {
        String cookieValue = String.valueOf(getWebDriver().manage().getCookieNamed(cookieString));
        return cookieValue.substring(cookieValue.indexOf("=") + 1, cookieValue.indexOf(";"));
    }
}