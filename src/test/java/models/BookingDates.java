
package models;

import lombok.*;

/** DTO for booking dates. */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BookingDates {
    private String checkin;
    private String checkout;
}
