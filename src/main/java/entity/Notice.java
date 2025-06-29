package entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class Notice {
    private Long bookingId;
    private Long userId;
    private Integer noticeId;
    private Integer noticeType; //0 la da mua, 1 la da huy, 2 la admin huy
    private Long tourId;
    private LocalDate daytimeStart;
    private Integer status;

    public Notice() {
    }
}
