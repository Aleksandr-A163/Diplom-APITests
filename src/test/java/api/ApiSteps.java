package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Booking;
import models.BookingResponse;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static specs.RequestResponseSpecs.*;

/**
 * Step-style facade for Restful-Booker API.
 * Kept class name and package to minimize changes.
 */
public class ApiSteps {

    private static String token;          // auth token (cookie)
    private int lastBookingId;            // stored for convenience
    private Booking lastBookingPayload;   // stored payload

    // ============ Auth ============

    /** Compatibility method for old extension: registers user. Here it just authenticates. */
    @Step("Authenticate with default credentials")
    public static void registerUser() {
        authenticate("admin", "password123");
    }



    /** Returns last auth token. */
    public static String getToken() { return token; }

    @Step("Create auth token for {username}")
    public static void authenticate(String username, String password) {
        Map<String, String> body = Map.of(
                "username", username,
                "password", password
        );

        token = given()
                .spec(registerAndLoginRequestSpec)
                .body(body)
                .when().post("/auth")
                .then().spec(responseSpec200)
                .extract().path("token");
    }

    // ============ Booking ============

    @Step("Ping API")
    public ValidatableResponse ping() {
        return given().spec(registerAndLoginRequestSpec)
                .when().get("/ping")
                .then().spec(responseSpec201);
    }

    @Step("Create booking")
    public BookingResponse createBooking(Booking booking) {
        this.lastBookingPayload = booking;

        BookingResponse resp = given().spec(registerAndLoginRequestSpec)
                .body(booking)
                .when().post("/booking")
                .then().spec(responseSpec200)
                .extract().as(BookingResponse.class);

        this.lastBookingId = resp.getBookingid();
        return resp;
    }

    @Step("Get booking by id {id}")
    public Booking getBookingById(int id) {
        return given().spec(registerAndLoginRequestSpec)
                .when().get("/booking/{id}", id)
                .then().spec(responseSpec200)
                .extract().as(Booking.class);
    }

    @Step("PUT update booking {id}")
    public void updateBookingPut(int id, Booking booking) {
        given().spec(registerAndLoginRequestSpec)
                .cookie("token", token)
                .body(booking)
                .when().put("/booking/{id}", id)
                .then().spec(responseSpec200);
    }

    @Step("PATCH update booking {id}")
    public void partialUpdateBooking(int id, String patchJson) {
        given().spec(registerAndLoginRequestSpec)
                .cookie("token", token)
                .body(patchJson)
                .when().patch("/booking/{id}", id)
                .then().spec(responseSpec200);
    }

    @Step("Delete booking {id}")
    public void deleteBooking(int id) {
        given().spec(registerAndLoginRequestSpec)
                .cookie("token", token)
                .when().delete("/booking/{id}", id)
                .then().spec(responseSpec201);
    }

    public int getLastBookingId() { return lastBookingId; }
    public Booking getLastBookingPayload() { return lastBookingPayload; }
}
