package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
	private Long userId;
	private String userName;
	private String email;
	private String phoneNumber;
	private String userAddress;
	private Integer isAdmin;
}
