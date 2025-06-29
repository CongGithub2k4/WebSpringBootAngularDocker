package entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long userId;
    private String userName;
    private String email;
    private String phoneNumber;
    private String userPassword;
    private String userAddress;
    private Integer isAdmin;
}
