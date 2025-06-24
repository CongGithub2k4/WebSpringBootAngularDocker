package controller;

import config.ExtractFromSpringSecurityContext;
import config.JwtUtil;
import dto.LoginRequest;
import dto.LoginResponse;
import dto.UserDTO;
import entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthService authService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // http://localhost:8080/auth/login
    // ?emailOrSDT=user1@gmail.com&password=123456
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        boolean authenticated = authService.authenticate(
            loginRequest.getEmailOrSDT(),
            loginRequest.getPassword()
        );
        if (!authenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
        }

        User user = authService.getUserByEmailOrSDT(loginRequest.getEmailOrSDT());

        String token = jwtUtil.generateToken(user);
        UserDTO userDTO = new UserDTO(
            user.getUserId(),
            user.getUserName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getUserAddress(),
            user.getIsAdmin()
        );
        return ResponseEntity.ok(new LoginResponse(token, userDTO));
    }


    // Dùng cho khi cần thay đổi thông tin user về sđt hay email thì cần xem xem có ai dùng chưa.
    // Ngoài ra khi đăng ký acc ới cũng cần xét
    // http://localhost:8080/auth/check-exists?email=user1@gmail.com&phone=01234567891
    @GetMapping("/check-exists")
    public ResponseEntity<Boolean> checkEmailOrPhoneExists(@RequestParam String email, @RequestParam String phone) {
        return ResponseEntity.ok(authService.checkExistedEmailOrPhone(email, phone));
    }

    // http://localhost:8080/auth/forgetpass?emailOrSDT=01234567891
    @GetMapping("/forgetpass")
    public ResponseEntity<String> forgetPassword(@RequestParam String emailOrSDT) {
        boolean existed = authService.checkExistedAccount(emailOrSDT);
        if(!existed) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email hoặc số đện thoại chưa đăng ký tài khoản");
        }
        authService.updatePassword(emailOrSDT, "123456");
        return ResponseEntity.ok("123456");
    }

    // http://localhost:8080/auth/checkConflictEmail?email=user1@gmail.com
    @GetMapping("/checkConflictEmail")
    public ResponseEntity<Boolean> checkConflictEmail(@RequestParam String email) {
        try {
            Long userId = ExtractFromSpringSecurityContext.extractUserIdFromSecurityContext();
            return ResponseEntity.ok(authService.checkConflictEmailWhenUpdateInfo(userId,email));
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    // http://localhost:8080/auth/checkConflictPhone?phone=01234567891
    @GetMapping("/checkConflictPhone")
    public ResponseEntity<Boolean> checkConflictPhone(@RequestParam String phone) {
        try {
            Long userId = ExtractFromSpringSecurityContext.extractUserIdFromSecurityContext();
            return ResponseEntity.ok(authService.checkConflictPhoneWhenUpdateInfo(userId,phone));
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Claims claims = jwtUtil.extractAllClaims(token);
            return ResponseEntity.ok(claims);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ");
        }
    }
}

