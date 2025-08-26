
package models;

import lombok.*;

/** DTO for booking payload. */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Booking {
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;
}
