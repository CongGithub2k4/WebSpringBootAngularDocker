package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
//@AllArgsConstructor
public class TourParticular extends Tour{
    private LocalDate daytimeStart;
    private Integer totalSlot;
    private Integer slotRemain;
    private Integer price;
    private Integer status; //0 la mo den luc khoi hanh se di,1 la dang di, 2 la da xong, 3 la admin huy tour
    private Long adminId;
    private LocalDate daytimeCreate;
    public static void main(String[] args) {
        System.out.println(LocalDate.now());
    }
}
