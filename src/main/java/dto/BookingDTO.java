package dto;

import entity.TourParticular;
import entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookingDTO {
    TourParticular tour;
    User user;
}
