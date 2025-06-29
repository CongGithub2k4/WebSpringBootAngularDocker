package entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class TourParticular extends Tour{
    private LocalDate daytimeStart;
    private Integer totalSlot;
    private Integer slotRemain;
    private Integer price;
    private Integer status;
    private Long adminId;
    private LocalDate daytimeCreate;
    public static void main(String[] args) {
        System.out.println(LocalDate.now());
    }
}
