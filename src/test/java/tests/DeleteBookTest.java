
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

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for deleting a booking (kept class/file name).
 */
@DisplayName("Tests for booking deletion (Restful-Booker)")
@Owner("Anosov Aleksandr")
@Story("Booking delete actions")
@Feature("Booking")
@Tag("Booking")
public class DeleteBookTest extends TestBase {

    @Test
    @DisplayName("Delete booking with valid token returns 201")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteBookingWithToken() {
        ApiSteps steps = new ApiSteps();
        ApiSteps.registerUser();
        BookingResponse created = steps.createBooking(
                Booking.builder()
                        .firstname("Alice").lastname("Cooper")
                        .totalprice(123).depositpaid(true)
                        .bookingdates(new BookingDates("2025-09-10","2025-09-12"))
                        .additionalneeds("Lunch")
                        .build()
        );
        steps.deleteBooking(created.getBookingid());
    }

    @Test
    @DisplayName("Delete booking with invalid token returns 403")
    @Severity(SeverityLevel.NORMAL)
    public void deleteWithInvalidToken() {
        ApiSteps steps = new ApiSteps();
        BookingResponse created = steps.createBooking(
                Booking.builder()
                        .firstname("Bad").lastname("Token")
                        .totalprice(10).depositpaid(true)
                        .bookingdates(new BookingDates("2025-09-10","2025-09-12"))
                        .build()
        );

        int status = given()
                .cookie("token", "invalid123")
                .when().delete("/booking/{id}", created.getBookingid())
                .then().extract().statusCode();

        assertThat(status).isEqualTo(HttpStatus.SC_FORBIDDEN);
    }
}
