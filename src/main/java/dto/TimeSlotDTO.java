package dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter @Setter
public class TimeSlotDTO {
	Long tourId;
	LocalDate daytimeStart;
	Integer slot;
	Integer price;
}
