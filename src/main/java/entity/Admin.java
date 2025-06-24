package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter @Setter
//@AllArgsConstructor
public class Admin extends User {

    private Long adminId;

    //public Admin(Long userId, String userName, String email, String phoneNumber, String userPassword, String userAddress, Integer isAdmin) {
        //super(userId, userName, email, phoneNumber, userPassword, userAddress, isAdmin);
    //}
    private Long userId;
}
