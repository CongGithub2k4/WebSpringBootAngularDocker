package dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdminUserDTO {
	private Long userId;
	private String userName;
	private String email;
	private String phoneNumber;
	private String userAddress;
	private Integer isAdmin;
	private Long bookingCount;
	private Long totalSpent;
}
