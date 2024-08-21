package tests;

import io.qameta.allure.Step;
import config.TestDataConfig;
import models.*;
import api.AuthApi;
import org.aeonbits.owner.ConfigFactory;
import io.restassured.response.Response;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


import static io.restassured.RestAssured.given;
import static specs.RequestResponseSpecs.*;

public class AddNewBook {
    static final TestDataConfig testDataConfig = ConfigFactory.create(TestDataConfig.class, System.getProperties());

    @Step("Add a new book to user profile")
    public void addListOfBook() {
        AddBookToBasketRequestBodyModel bookData = new AddBookToBasketRequestBodyModel();
        String userID = AuthApi.extractValueFromCookieString("userID");
        String token = AuthApi.extractValueFromCookieString("token");
        bookData.setUserId(userID);
        bookData.setIsbn(testDataConfig.isbn());

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
        assertThat(bookData.getCollectionOfIsbns()
                .get(0).getIsbn(), equalTo(addBookResponse.path("books[0].isbn")));

    }
}