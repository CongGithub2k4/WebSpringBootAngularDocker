package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class User {
    private Long userId;
    private String userName;
    private String email;
    private String phoneNumber;
    private String userPassword;
    private String userAddress;
    private Integer isAdmin;
}
