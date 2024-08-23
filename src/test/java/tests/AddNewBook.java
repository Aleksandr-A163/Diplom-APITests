package tests;

import io.qameta.allure.Step;
import config.TestData;
import models.AddBookToBasketRequestBodyModel;
import api.AuthApi;
import io.restassured.response.Response;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static io.restassured.RestAssured.given;
import static specs.RequestResponseSpecs.*;

public class AddNewBook {

    @Step("Add a new book to user profile")
    public void addListOfBook() {
        AddBookToBasketRequestBodyModel bookData = new AddBookToBasketRequestBodyModel();

        String userID = AuthApi.extractValueFromCookieString("userID");
        String token = AuthApi.extractValueFromCookieString("token");

        String isbn = TestData.getRandomIsbn();  // Получаем случайное значение ISBN
        System.out.println("Generated ISBN: " + isbn);  // Вывод значения для отладки

        bookData.setUserId(userID);
        bookData.setIsbn(isbn);  // Устанавливаем значение ISBN

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
}