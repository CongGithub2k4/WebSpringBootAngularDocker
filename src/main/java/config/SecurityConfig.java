package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity //Cần để xử lý filter token từ fronend hửi về
public class SecurityConfig {
	private final JwtUtil jwtUtil;

	public SecurityConfig(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		return http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
				//.requestMatchers("/auth/**").permitAll()
				.anyRequest().permitAll()
			)
			.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(new JwtAuthenticationFilter(authenticationManager, jwtUtil), UsernamePasswordAuthenticationFilter.class)
			.build();
		/***
		 * Các Request vs link auth/.... thì ko cần đăng nhập vì chưa có token.
		 * Còn mọi link khác như tours/..., search/...., đều cần có Token.
		 * Tuy nhiên có nhiều link ở trang chủ chỉ là xem sản phẩm chưa cần Tokens.
		 * thì .anyRequest().authenticated() sẽ khiến những request đó bị 401 nếu không có token.
		 * Sửa thành
		 * .authorizeHttpRequests(auth -> auth
		 *     .anyRequest().permitAll()
		 * )
		 * Điều này kết hợp với JwtAuthenticationFilter.java hàm doInternal..() đã sửa ở bước trước, giúp:
		 * 	- Nếu có token: tự gán người dùng vào SecurityContextHolder
		 * 	- Nếu không có token: không gán gì, để controller tự quyết định
		 */
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	@Configuration
	public class WebConfig implements WebMvcConfigurer {
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**")
				.allowedOrigins(
				"http://localhost:4200", // Cấu hình cho phép localhost:4200
				"http://localhost",     // Thêm dòng này để cho phép Nginx ở cổng 80
				"http://localhost:80"  // Cổng 80 tường minh (đôi khi cần thiết)
				//"http://<tên-miền-của-bạn-nếu-có>", // Nếu bạn deploy với tên miền thực tế
				//"http://<IP-của-docker-host>" // Nếu bạn truy cập bằng IP của host
				)
				.allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowedHeaders("*")
				.allowCredentials(true);
		}
	}
}

