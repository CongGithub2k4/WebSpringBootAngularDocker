package dto;

import entity.TourParticular;
import entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookingDTO {
    //vì khi book vé 1 tour, Ta cần 2 thwujc thể tour và user, chsung đều trong 1 json nên
    //không thể là 2 requestBody riêng. nên cần gộp nó lại làm 1
    TourParticular tour;
    User user;
}
