package tests;

import api.ApiSteps;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.*;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.RequestResponseSpecs.*;

/**
 * Tests for creating and fetching a booking (kept class/file name).
 */
@DisplayName("Tests for booking creation (Restful-Booker)")
@Owner("Anosov Aleksandr")
@Story("Booking create/get actions")
@Feature("Booking")
@Tag("Booking")
public class AddNewBookTest extends TestBase {

    @Test
    @DisplayName("Create booking then get by id and validate schema")
    @Severity(SeverityLevel.CRITICAL)
    void createAndFetchBooking() {
        ApiSteps steps = new ApiSteps();
        steps.ping();

        Booking payload = Booking.builder()
                .firstname("Hans")
                .lastname("Gruber")
                .totalprice(200)
                .depositpaid(false)
                .bookingdates(new BookingDates(
                        LocalDate.now().plusDays(1).toString(),
                        LocalDate.now().plusDays(5).toString()))
                .additionalneeds("Breakfast")
                .build();

        BookingResponse created = steps.createBooking(payload);
        assertThat(created.getBookingid()).isPositive();

        // Schema validation (POST /booking response)
        given().spec(registerAndLoginRequestSpec)
                .body(payload)
                .when().post("/booking")
                .then().spec(responseSpec200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-response.json"));

        Booking fetched = steps.getBookingById(created.getBookingid());
        assertThat(fetched).usingRecursiveComparison().isEqualTo(created.getBooking());
    }

    @Test
    @DisplayName("PUT without token should return 401 or 403")
    @Severity(SeverityLevel.NORMAL)
    void putWithoutAuth() {
        ApiSteps steps = new ApiSteps();
        BookingResponse created = steps.createBooking(
                Booking.builder()
                        .firstname("No").lastname("Auth")
                        .totalprice(50).depositpaid(true)
                        .bookingdates(new BookingDates("2025-09-01", "2025-09-02"))
                        .build()
        );

        int status = given()
                .contentType(JSON)
                .body(Map.of("firstname", "X")) // корректный JSON без конкатенации
                .when().put("/booking/{id}", created.getBookingid())
                .then().extract().statusCode();

        assertThat(status).isIn(HttpStatus.SC_UNAUTHORIZED, HttpStatus.SC_FORBIDDEN);
    }
}
