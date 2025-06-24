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

	private static final String SECRET = "your-256-bit-secret-your-256-bit-secret"; // >= 32 ký tự
	private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

	public String generateToken(User user) {
		return Jwts.builder()
			.subject(user.getEmail())                         // setSubject() → subject()
			.claim("userId", user.getUserId())                // claim vẫn dùng được
			.claim("isAdmin", user.getIsAdmin())
			.issuedAt(new Date())                             // setIssuedAt() → issuedAt()
			.expiration(new Date(System.currentTimeMillis() + 86400000))
			.signWith(key, Jwts.SIG.HS256)                    // Cách mới để chọn thuật toán
			.compact();
	}


	public Claims extractAllClaims(String token) {
		return Jwts.parser()                // parser() mới của jjwt 0.12.x
			.verifyWith(key)               // KHÔNG dùng .setSigningKey nữa
			.build()
			.parseSignedClaims(token)      // dùng parseSignedClaims (không phải parseClaimsJws)
			.getPayload();                 // trả về Claims payload
	}

	public boolean validateToken(String token) {
		try {
			Claims claims = extractAllClaims(token);
			return claims != null; // đơn giản là dùng claims
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}
