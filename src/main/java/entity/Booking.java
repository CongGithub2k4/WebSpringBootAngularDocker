package entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class Booking {
    private Long bookingId;
    private Long userId;
    private Long tourId;
    private LocalDate daytimeStart;
    private Integer slotReserved;
    private Integer totalPurchase;
    private LocalDate daytimeCreate;
    private Integer status;
    private String userName;
    private String tourName;
}
