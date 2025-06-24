package config;

import entity.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager,JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String token = getTokenFromRequest(request);

		// Nếu có token thì cố gắng xác thực
		if (token != null/* && jwtUtil.validateToken(token)*/) {
			try {
				if(jwtUtil.validateToken(token)) {
					Claims claims = jwtUtil.extractAllClaims(token);
					// ... tạo userDetails như bạn đã làm
					CustomUserDetails userDetails = new CustomUserDetails(
						claims.get("userId", Integer.class).longValue(),
						claims.get("userName", String.class),
						claims.get("email", String.class),
						claims.get("phoneNumber", String.class),
						claims.get("userAddress", String.class),
						claims.get("isAdmin", Integer.class)
					);

					UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}

			} catch (JwtException | IllegalArgumentException e) {
				//response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
				//return;
				/***
				*Nếu token sai thì vẫn cho đi, để controller quyết định xử lý
				*Hoặc bạn có thể log nếu muốn
				 * */
			}
		}
		// Nếu không có token, vẫn cho qua
		filterChain.doFilter(request, response);
	}


	private String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
