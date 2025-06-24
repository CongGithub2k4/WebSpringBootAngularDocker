package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
	private String userName;
	private String email;
	private String phoneNumber;
	private String userPassword;
}
