package service;

import dao.AuthDAO;
import dao.UserDAO;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthService(AuthDAO authDAO, UserDAO userDAO, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User getUserByEmailOrSDT(String emailOrSDT) {
        return authDAO.getUserByEmailOrSDT(emailOrSDT);
    }

    public boolean checkExistedEmailOrPhone(String email, String phone) {
        return authDAO.checkExistedEmailOrPhone(email, phone);
    }
    public boolean authenticate(String emailOrSDT, String rawPassword) {
        User user = authDAO.getUserByEmailOrSDT(emailOrSDT);
        if (user == null) {
            return false;
        }

        if (!rawPassword.equals(user.getUserPassword()) && !bCryptPasswordEncoder.matches(rawPassword, user.getUserPassword())) {
            return false;
        }

        // Mã hóa mật khẩu nếu chưa được mã hóa
        if (!user.getUserPassword().startsWith("$2a$")) {
            user.setUserPassword(bCryptPasswordEncoder.encode(rawPassword));
            userDAO.userUpdateUser(user);  // Lưu lại mật khẩu đã mã hóa
        }

        return true;
    }

    public void updatePassword(String emailOrSDT, String password) {
        User user = authDAO.getUserByEmailOrSDT(emailOrSDT);
        authDAO.updatePassword(user.getUserId(), password);
    }

    public boolean checkExistedAccount(String emailOrSDT) {
        return checkExistedEmailOrPhone(emailOrSDT, emailOrSDT);
    }

    public boolean checkConflictEmailWhenUpdateInfo(Long userId, String email) {
        User emailUser = getUserByEmailOrSDT(email);
		if(emailUser==null) {
            return false;
        }
        System.out.println("userId token= "+ userId +" , emailUser= "+ emailUser.getUserId());
        return userId != emailUser.getUserId();
	}
    public boolean checkConflictPhoneWhenUpdateInfo(Long userId, String phone) {
        User phoneUser = getUserByEmailOrSDT(phone);
        if(phoneUser==null) {
            return false;
        }
        System.out.println("userId token= "+ userId +" , phoneUser= "+ phoneUser.getUserId());
        return userId != phoneUser.getUserId();
    }
}
