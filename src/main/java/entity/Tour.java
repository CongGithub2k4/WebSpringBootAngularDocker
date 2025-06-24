package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
//@AllArgsConstructor
public class Tour {
    private Long tourId;
    private String startDestination;
    private String tourName;
    private Integer dayNumber;
    private Integer nightNumber;
    private String linkImage;
    private Long adminIdCreateTour;
    private LocalDate daytimeCreateTour;
    private List<String> thoughoutDestination;
    public void setThoughoutDestination(List<String> thoughoutDestination) {
        this.thoughoutDestination = thoughoutDestination;
    }
}
