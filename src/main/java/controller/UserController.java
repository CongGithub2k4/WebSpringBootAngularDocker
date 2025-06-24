package controller;

import config.ExtractFromSpringSecurityContext;
import dto.AdminUserDTO;
import dto.CreateUserRequest;
import dto.UserDTO;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.AuthService;
import service.UserService;

import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }


    //http://localhost:8080/users/user-create
    @PostMapping("/user-create")
    public ResponseEntity<String> userCreateUser(@RequestBody CreateUserRequest user) {
        if(authService.checkExistedEmailOrPhone(user.getEmail(), user.getPhoneNumber()) ) {
            return new ResponseEntity<>("Email or phone used by other account", HttpStatus.CONFLICT);
        }
        userService.userCreateUser(user);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }


    //http://localhost:8080/users/admin-get-user-by-id?id=1
    @GetMapping("/admin-get-user-by-id")
    public ResponseEntity<AdminUserDTO> adminGetUserById(@RequestParam Long id) {
        try {
            if(ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
                System.out.println("Là admin rroofi");
                AdminUserDTO user = userService.adminGetUserById(id);
                if (user != null) {
                    System.out.println("User vs id"+ id +"Có");
                    return new ResponseEntity<>(user, HttpStatus.OK);
                } else {
                    System.out.println("Khong có user");
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            else {
                System.out.println("Khong phai amdin");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (RuntimeException ex) {
            System.out.println("Token còn lỗi");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // http://localhost:8080/users/admin-get-user-by-phone?sdt=01234567891
    @GetMapping("/admin-get-user-by-phone")
    public ResponseEntity<AdminUserDTO> adminGetUserBySDT(@RequestParam String sdt) {
        try {
            if(ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
                AdminUserDTO user = userService.adminGetUserBySDT(sdt);
                if (user != null) {
                    return new ResponseEntity<>(user, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // http://localhost:8080/users/admin-get-user-by-email?email=user1@gmail.com
    @GetMapping("/admin-get-user-by-email")
    public ResponseEntity<AdminUserDTO> adminGetUserByEmail(@RequestParam String email) {
        try {
            if(ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
                AdminUserDTO user = userService.adminGetUserByEmail(email);
                if (user != null) {
                    return new ResponseEntity<>(user, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // http://localhost:8080/users/admin-get-user-all?page=1&type=1
    @GetMapping("/admin-get-user-all")
    public ResponseEntity<Map<String, Object>> adminGetAllUsers(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "1") int type
    ) {
        try {
            if(!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            Map<String, Object> response = userService.adminGetAllUsers(page, type);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // http://localhost:8080/users/user-update
    @PutMapping("/user-update")
    public ResponseEntity<String> userUpdateUser(@RequestBody User updatedUser) {
        try {
            if (ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Long userId = ExtractFromSpringSecurityContext.extractUserIdFromSecurityContext();
            User existingUser = userService.userGetInfoById(userId);
            /*if(authService.checkExistedEmailOrPhone(updatedUser.getEmail(),
                updatedUser.getPhoneNumber())) {
                User emailUser = authService.getUserByEmailOrSDT(updatedUser.getEmail());
                User phoneUser = authService.getUserByEmailOrSDT(updatedUser.getPhoneNumber());
                if()
            }*/
            if (existingUser != null) {
                updatedUser.setUserId(userId);
                userService.userUpdateUser(updatedUser);
                return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // http://localhost:8080/users/user-get-info
    @GetMapping("/user-get-info")
    public ResponseEntity<User> userGetInfoById() {
        try {
            Long userId = ExtractFromSpringSecurityContext.extractUserIdFromSecurityContext();
            User user = userService.userGetInfoById(userId);
            /*if (ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }*/
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
