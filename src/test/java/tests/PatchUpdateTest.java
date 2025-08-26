package tests;

import api.ApiSteps;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.Booking;
import models.BookingDates;
import models.BookingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Authorized PATCH update of booking")
@Owner("Anosov Aleksandr")
@Story("Booking partial update (PATCH)")
@Feature("Booking")
@Tag("Booking")
public class PatchUpdateTest extends TestBase {

    @Test
    @DisplayName("PATCH /booking/{id} updates only firstname with valid token")
    @Severity(SeverityLevel.NORMAL)
    void patchFirstnameOnly() {
        ApiSteps steps = new ApiSteps();
        ApiSteps.registerUser();

        BookingResponse created = steps.createBooking(
                Booking.builder()
                        .firstname("Before").lastname("Patch")
                        .totalprice(99).depositpaid(true)
                        .bookingdates(new BookingDates("2025-10-01", "2025-10-03"))
                        .additionalneeds("Lunch")
                        .build()
        );

        // Update only firstname
        steps.partialUpdateBooking(created.getBookingid(), "{\"firstname\":\"After\"}");

        Booking after = steps.getBookingById(created.getBookingid());
        assertThat(after.getFirstname()).isEqualTo("After");
        assertThat(after.getLastname()).isEqualTo("Patch");
        assertThat(after.getTotalprice()).isEqualTo(99);
        assertThat(after.getAdditionalneeds()).isEqualTo("Lunch");
    }
}
