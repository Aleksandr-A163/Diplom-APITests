package tests;

import io.qameta.allure.Step;
import config.TestData;
import models.AddBookToBasketRequestBodyModel;
import api.ApiSteps;
import io.restassured.response.Response;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static io.restassured.RestAssured.given;
import static specs.RequestResponseSpecs.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AddNewBook {

    private String isbn;  // Поле для хранения значения ISBN

    @Step("Add a new book to user profile")
    public void addRandomBook() {
        AddBookToBasketRequestBodyModel bookData = new AddBookToBasketRequestBodyModel();

        String userID = ApiSteps.extractValueFromCookieString("userID");
        String token = ApiSteps.extractValueFromCookieString("token");

        this.isbn = TestData.getRandomIsbn();  // Сохраняем случайное значение ISBN
        System.out.println("Generated ISBN: " + isbn);  // Вывод значения для отладки

        bookData.setUserId(userID);
        bookData.setIsbn(this.isbn);  // Устанавливаем значение ISBN

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

        assertThat(bookData.getCollectionOfIsbns()
                .get(0).getIsbn(), equalTo(addBookResponse.path("books[0].isbn")));
    }

    @Step("Attempt to add a book with invalid token")
    public void addBookWithInvalidToken() {
        AddBookToBasketRequestBodyModel bookData = new AddBookToBasketRequestBodyModel();

        String userID = ApiSteps.extractValueFromCookieString("userID");
        String invalidToken = "invalid_token";

        this.isbn = TestData.getRandomIsbn();  // Сохраняем случайное значение ISBN
        System.out.println("Generated ISBN: " + isbn);  // Вывод значения для отладки

        bookData.setUserId(userID);
        bookData.setIsbn(this.isbn);  // Устанавливаем значение ISBN

        Response addBookResponse = given(registerAndLoginRequestSpec)
                .contentType(JSON)
                .header("Authorization", "Bearer " + invalidToken)  // Некорректный токен
                .body(bookData)
                .when()
                .post("BookStore/v1/Books")
                .then()
                .spec(unauthorizedResponseSpec401)  // Ожидаем код 401 Unauthorized
                .extract()
                .response();

        System.out.println("Unauthorized Add Book Response: " + addBookResponse.asString());

        assertThat(addBookResponse.getStatusCode()).isEqualTo(401);

        String errorMessage = addBookResponse.jsonPath().getString("message");
        System.out.println("Error Message: " + errorMessage);
    }

    // Метод для получения значения ISBN
    public String getIsbn() {
        return this.isbn;
    }
}