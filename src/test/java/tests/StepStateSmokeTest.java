package tests;

import api.ApiSteps;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.Booking;
import models.BookingDates;
import models.BookingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ApiSteps stores last booking id & payload")
@Owner("Anosov Aleksandr")
@Tag("Booking")
public class StepStateSmokeTest extends TestBase {

    @Test
    @Severity(SeverityLevel.TRIVIAL)
    void stepKeepsState() {
        ApiSteps steps = new ApiSteps();

        Booking payload = Booking.builder()
                .firstname("State").lastname("Check")
                .totalprice(111).depositpaid(true)
                .bookingdates(new BookingDates("2025-11-01", "2025-11-03"))
                .build();

        BookingResponse created = steps.createBooking(payload);

        assertThat(steps.getLastBookingId()).isEqualTo(created.getBookingid());
        assertThat(steps.getLastBookingPayload()).usingRecursiveComparison().isEqualTo(payload);
    }
}
