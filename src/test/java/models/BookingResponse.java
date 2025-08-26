
package models;

import lombok.Data;

/** Response DTO for POST /booking. */
@Data
public class BookingResponse {
    private int bookingid;
    private Booking booking;
}
