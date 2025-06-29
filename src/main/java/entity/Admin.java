package entity;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class Admin extends User {
    private Long adminId;
    private Long userId;
}
