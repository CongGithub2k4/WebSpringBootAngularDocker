package config;

import entity.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ExtractFromSpringSecurityContext {
	public static Long extractUserIdFromSecurityContext() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || ! authentication.isAuthenticated()) {
			throw new RuntimeException("Người dùng chưa đăng nhập");
		}

		Object principal = authentication.getPrincipal();

		if (!(principal instanceof CustomUserDetails)) {
			throw new RuntimeException("Thông tin người dùng không hợp lệ hoặc chưa đăng nhập");
		}

		return ((CustomUserDetails) principal).getUserId();
	}
	public static boolean extractIsAdminFromSecurityContext() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || ! authentication.isAuthenticated()) {
			throw new RuntimeException("Người dùng chưa đăng nhập");
		}
		Object principal = authentication.getPrincipal();
		if (!(principal instanceof CustomUserDetails)) {
			throw new RuntimeException("Thông tin người dùng không hợp lệ");
		}
		return ((CustomUserDetails) principal).isAdmin();
	}
}
