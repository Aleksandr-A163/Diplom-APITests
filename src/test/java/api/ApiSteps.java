package api;

import io.qameta.allure.Step;
import helpers.FakerData;
import models.*;
import static com.codeborne.selenide.Selenide.open;
import org.openqa.selenium.Cookie;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static specs.RequestResponseSpecs.*;

public class ApiSteps {

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

    @Step("Get user data by user ID")
    public static LoginResponseModel getUserData() {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is not set. Please register a user first.");
        }

        String token = extractValueFromCookieString("token");

        Response response = given()
                .spec(registerAndLoginRequestSpec)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/Account/v1/User/" + userId)
                .then()
                .spec(loginResponseSpec200)
                .extract().response();

        System.out.println("User Data Raw Response: " + response.asString());

        if (response.getContentType().contains("application/json")) {
            LoginResponseModel userDataResponse = response.as(LoginResponseModel.class);

            System.out.println("User Data Response: " + userDataResponse);

            return userDataResponse;
        } else {
            throw new IllegalStateException("Unexpected content type: " + response.getContentType());
        }
    }

    @Step("Attempt to get user data with invalid credentials")
    public static Response getUserDataUnauthorized() {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is not set. Please register a user first.");
        }

        // Здесь мы намеренно не передаем токен или передаем некорректный токен
        String invalidToken = "invalid_token";

        Response response = given()
                .spec(registerAndLoginRequestSpec)
                .header("Authorization", "Bearer " + invalidToken)  // Используем некорректный токен
                .when()
                .get("/Account/v1/User/" + userId)
                .then()
                .spec(unauthorizedResponseSpec401)  // Ожидаем ответ 401 Unauthorized
                .extract().response();

        System.out.println("Unauthorized User Data Response: " + response.asString());

        return response;
    }

    @Step("Delete a book from user profile by ISBN")
    public static Response deleteBookByIsbn(String isbn) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is not set. Please register a user first.");
        }

        String token = extractValueFromCookieString("token");

        // Создание тела запроса для удаления книги
        DeleteBooksRequestModel deleteBookData = new DeleteBooksRequestModel();
        deleteBookData.setUserId(userId);
        deleteBookData.setIsbn(isbn);

        Response response = given()
                .spec(registerAndLoginRequestSpec)
                .header("Authorization", "Bearer " + token)
                .body(deleteBookData)
                .when()
                .delete("/BookStore/v1/Book")
                .then()
                .spec(responseSpec204)
                .extract().response();

        System.out.println("Delete Book Response: " + response.asString());

        return response;
    }

    @Step("Attempt to delete a non-existent book by ISBN")
    public static Response deleteNonExistentBookByIsbn(String isbn) {
    if (userId == null) {
        throw new IllegalArgumentException("User ID is not set. Please register a user first.");
    }

    String token = extractValueFromCookieString("token");

    // Создание тела запроса для удаления несуществующей книги
    DeleteBooksRequestModel deleteBookData = new DeleteBooksRequestModel();
    deleteBookData.setUserId(userId);
    deleteBookData.setIsbn(isbn);

    Response response = given()
            .spec(registerAndLoginRequestSpec)
            .header("Authorization", "Bearer " + token)
            .body(deleteBookData)
            .when()
            .delete("/BookStore/v1/Book")
            .then()
            .spec(responseSpec400)  // Ожидаем код 400 Bad Request или 404 Not Found
            .extract().response();

    System.out.println("Delete Non-Existent Book Response: " + response.asString());

    return response;
}

}