package config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUpdater {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "your_plain_password";  // Thay thế với mật khẩu thô cần mã hóa
		String encodedPassword = encoder.encode(rawPassword);
		System.out.println(encodedPassword);
	}
}

