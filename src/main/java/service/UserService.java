package service;

import dao.UserDAO;
import dto.AdminUserDTO;
import dto.CreateUserRequest;
import dto.UserDTO;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;

    private final UserDAO userDAO;

    private static final int PAGE_SIZE = 10; // Số item trên mỗi trang

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserDAO userDAO) {
        this.passwordEncoder = passwordEncoder;
        this.userDAO = userDAO;
    }

    @Transactional
    public void userCreateUser(CreateUserRequest user) {
        String hashedPassword = passwordEncoder.encode(user.getUserPassword());
        user.setUserPassword(hashedPassword);
        userDAO.userCreateUser(user);
    }

    public AdminUserDTO adminGetUserById(Long user_id) {
        return userDAO.adminGetUserById(user_id);
    }

    public AdminUserDTO adminGetUserBySDT(String sdt) {
        return userDAO.adminGetUserBySDT(sdt);
    }

    public AdminUserDTO adminGetUserByEmail(String email) {
        return userDAO.adminGetUserByEmail(email);
    }

    public Map<String, Object> adminGetAllUsers(int pageNumber, int type) {
        int offset = (pageNumber - 1) * PAGE_SIZE;
        List<AdminUserDTO> users = userDAO.adminGetAllUsers(offset, PAGE_SIZE, type);
        int totalUsers = userDAO.getTotalUsers();
        int totalPages = (int) Math.ceil((double) totalUsers / PAGE_SIZE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", pageNumber);
        response.put("totalPages", totalPages);
        response.put("pageSize", PAGE_SIZE);
        response.put("data", users);
        System.out.println("\n \n Đã lấy user theo "+ pageNumber +" "+ type +"\n\n");
        return response;
    }

    @Transactional
    public void userUpdateUser(User user) {
        userDAO.userUpdateUser(user);
    }
    public User userGetInfoById(Long user_id) {
        return userDAO.userGetInfoById(user_id);
    }
}
