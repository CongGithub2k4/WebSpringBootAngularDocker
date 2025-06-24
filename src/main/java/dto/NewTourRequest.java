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
	//private String | MultipartFile linkImage;
	//private Long adminIdCreateTour;
	//private LocalDate daytimeCreateTour;
	private List<String> thoughoutDestination;
	private LocalDate daytimeStart;
	private Integer totalSlot;
	//private Integer slotRemain;
	private Integer price;
	//private Integer status; //0 la mo den luc khoi hanh se di,1 la dang di, 2 la da xong, 3 la admin huy tour
	//private Long adminId;
	//private LocalDate daytimeCreate;
}
