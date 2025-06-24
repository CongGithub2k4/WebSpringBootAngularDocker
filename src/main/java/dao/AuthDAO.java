package dao;

import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

@Repository
public class AuthDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUserByEmailOrSDT(String emailOrSDT) {
        String sql = "SELECT * FROM UserTable WHERE phone_number = ? OR email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), emailOrSDT, emailOrSDT);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean checkExistedEmailOrPhone(String email, String phone) {
        String sql = "SELECT COUNT(*) FROM UserTable WHERE phone_number = ? OR email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phone, email);
        return count != null && count > 0;
    }

    public void updatePassword(Long userId, String password) {
        String sql = "UPDATE UserTable SET user_password = ? WHERE user_id = ? ";
        jdbcTemplate.update(sql, password, userId);
    }

}
