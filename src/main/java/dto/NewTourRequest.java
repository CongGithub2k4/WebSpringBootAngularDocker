package dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
@Getter @Setter
public class NewTourRequest {
	private String startDestination;
	private String tourName;
	private Integer dayNumber;
	private Integer nightNumber;
	private List<String> thoughoutDestination;
	private LocalDate daytimeStart;
	private Integer totalSlot;
	private Integer price;
}
