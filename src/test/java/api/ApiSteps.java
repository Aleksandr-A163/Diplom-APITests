package api;

import io.qameta.allure.Step;
import helpers.FakerData;
import config.TestData;
import models.*;
import static com.codeborne.selenide.Selenide.open;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.Cookie;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static specs.RequestResponseSpecs.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiSteps {

    private static String userId;
    private static String userLogin;  // Сохраняем сгенерированный логин
    private static String userPassword;// Сохраняем сгенерированный пароль
    private String isbn;  // Поле для хранения значения ISBN

    private static String generateUserLogin() {
        return FakerData.generateUsername();
    }

    private static String generateUserPassword() {
        return FakerData.generatePassword(9, 16);
    }

    @DisplayName("User API actions")
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



    @DisplayName("Book API actions")
    @Step("Add a new book to user profile")
    public void addRandomBook() {
        String token = extractValueFromCookieString("token");

        // Генерация случайного ISBN
        this.isbn = TestData.getRandomIsbn();
        System.out.println("Generated ISBN: " + isbn);  // Вывод значения для отладки

        // Создание тела запроса для добавления книги
        AddBookToBasketRequestBodyModel bookData = new AddBookToBasketRequestBodyModel();
        bookData.setUserId(userId);
        bookData.setIsbn(this.isbn);

        // Выполнение запроса на добавление книги
        Response addBookResponse = given(registerAndLoginRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer " + token)
                .body(bookData)
                .when()
                .post("BookStore/v1/Books")
                .then()
                .spec(responseSpec201)
                .extract()
                .response();

        System.out.println("Response: " + addBookResponse.asString());  // Вывод ответа сервера для отладки

        // Проверка, что книга добавлена успешно
        assertThat(this.isbn, equalTo(addBookResponse.path("books[0].isbn")));
    }

    @Step("Attempt to add a book with invalid token")
    public void addBookWithInvalidToken() {
        // Генерация случайного ISBN
        this.isbn = TestData.getRandomIsbn();
        System.out.println("Generated ISBN: " + isbn);  // Вывод значения для отладки

        // Создание тела запроса для добавления книги
        AddBookToBasketRequestBodyModel bookData = new AddBookToBasketRequestBodyModel();
        bookData.setUserId(userId);
        bookData.setIsbn(this.isbn);

        // Выполнение запроса с некорректным токеном
        Response addBookResponse = given(registerAndLoginRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer invalid_token")  // Некорректный токен
                .body(bookData)
                .when()
                .post("BookStore/v1/Books")
                .then()
                .spec(unauthorizedResponseSpec401)
                .extract()
                .response();

        System.out.println("Unauthorized Add Book Response: " + addBookResponse.asString());

        // Проверка, что запрос завершился с ошибкой 401
        assertThat(addBookResponse.getStatusCode()).isEqualTo(401);
    }

    @Step("Delete a book from user profile by ISBN")
    public static Response deleteBook(String isbn) {
        String token = extractValueFromCookieString("token");

        // Создание тела запроса для удаления книги
        DeleteBooksRequestModel deleteBookData = new DeleteBooksRequestModel();
        deleteBookData.setUserId(userId);
        deleteBookData.setIsbn(isbn);

        // Выполнение запроса на удаление книги
        Response response = given()
                .spec(registerAndLoginRequestSpec)
                .header("Authorization", "Bearer " + token)
                .body(deleteBookData)
                .when()
                .delete("/BookStore/v1/Book")
                .then()
                .spec(responseSpec204)
                .extract()
                .response();

        System.out.println("Delete Book Response: " + response.asString());

        return response;
    }

    @Step("Attempt to delete a non-existent book by ISBN")
    public static Response deleteNonExistentBook(String isbn) {
        String token = extractValueFromCookieString("token");

        // Создание тела запроса для удаления книги
        DeleteBooksRequestModel deleteBookData = new DeleteBooksRequestModel();
        deleteBookData.setUserId(userId);
        deleteBookData.setIsbn(isbn);

        // Выполнение запроса на удаление несуществующей книги
        Response response = given()
                .spec(registerAndLoginRequestSpec)
                .header("Authorization", "Bearer " + token)
                .body(deleteBookData)
                .when()
                .delete("/BookStore/v1/Book")
                .then()
                .spec(responseSpec400)  // Ожидаем код 400 Bad Request или 404 Not Found
                .extract()
                .response();

        System.out.println("Delete Non-Existent Book Response: " + response.asString());

        return response;
    }

    // Метод для получения значения ISBN
    public String getIsbn() {
        return this.isbn;
    }

}