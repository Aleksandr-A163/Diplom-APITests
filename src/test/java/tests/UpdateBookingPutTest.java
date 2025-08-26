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

@DisplayName("Authorized PUT update of booking")
@Owner("Anosov Aleksandr")
@Story("Booking update (PUT)")
@Feature("Booking")
@Tag("Booking")
public class UpdateBookingPutTest extends TestBase {

    @Test
    @DisplayName("PUT /booking/{id} updates all fields with valid token")
    @Severity(SeverityLevel.CRITICAL)
    void putUpdatesAllFields() {
        ApiSteps steps = new ApiSteps();
        ApiSteps.registerUser(); // need token (cookie)

        // Create booking
        BookingResponse created = steps.createBooking(
                Booking.builder()
                        .firstname("Hans").lastname("Gruber")
                        .totalprice(200).depositpaid(false)
                        .bookingdates(new BookingDates("2025-09-01", "2025-09-05"))
                        .additionalneeds("Breakfast")
                        .build()
        );

        // Update all fields with PUT
        Booking updated = Booking.builder()
                .firstname("John").lastname("Doe")
                .totalprice(555).depositpaid(true)
                .bookingdates(new BookingDates("2025-10-10", "2025-10-15"))
                .additionalneeds("Dinner")
                .build();

        steps.updateBookingPut(created.getBookingid(), updated);

        // Checking for updated fields
        Booking after = steps.getBookingById(created.getBookingid());
        assertThat(after).usingRecursiveComparison().isEqualTo(updated);
    }
}
