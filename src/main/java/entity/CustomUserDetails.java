package entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

	private final Long userId;
	private final String userName;
	private final String email;
	private final String phoneNumber;
	private final String userAddress;
	private final Integer isAdmin;

	public CustomUserDetails(Long userId, String userName, String email, String phoneNumber, String userAddress, Integer isAdmin) {
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.userAddress = userAddress;
		this.isAdmin = isAdmin;
	}

	public Long getUserId() {
		return userId;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public boolean isAdmin() {
		return isAdmin != null && isAdmin == 1;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(isAdmin() ? "ROLE_ADMIN" : "ROLE_USER"));
	}

	@Override public String getPassword() { return null; }

	@Override public String getUsername() { return userName; }

	@Override public boolean isAccountNonExpired() { return true; }

	@Override public boolean isAccountNonLocked() { return true; }

	@Override public boolean isCredentialsNonExpired() { return true; }

	@Override public boolean isEnabled() { return true; }
}
