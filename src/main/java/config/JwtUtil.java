package config;

import entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

	private static final String SECRET = "my-256-bit-secret-account-256-bit-secret";
	private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

	public String generateToken(User user) {
		return Jwts.builder()
			.subject(user.getEmail())
			.claim("userId", user.getUserId())
			.claim("isAdmin", user.getIsAdmin())
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + 86400000))
			.signWith(key, Jwts.SIG.HS256)
			.compact();
	}


	public Claims extractAllClaims(String token) {
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	public boolean validateToken(String token) {
		try {
			Claims claims = extractAllClaims(token);
			return claims != null;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}
