package dao;

import dto.AdminUserDTO;
import dto.CreateUserRequest;
import dto.UserDTO;
import entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

@Repository
public class UserDAO {
	private final JdbcTemplate jdbcTemplate;
	private static final Integer MAX_SIZE_PER_PAGE = 10;

	@Autowired
	public UserDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void userCreateUser(CreateUserRequest user) {
		String sql = "INSERT INTO UserTable (user_name, email, phone_number, user_password) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, user.getUserName(), user.getEmail(), user.getPhoneNumber(), user.getUserPassword());
	}

	public void userUpdateUser(User user) {
		String sql = "UPDATE UserTable " +
			"SET user_name = ?, email = ?, phone_number = ?, user_password = ?, user_address = ? " +
			"WHERE user_id = ?";
		jdbcTemplate.update(sql, user.getUserName(), user.getEmail(), user.getPhoneNumber(),
			user.getUserPassword(), user.getUserAddress(), user.getUserId());
	}

	public User userGetInfoById(Long id) {
		try {
			String sql = "SELECT * FROM UserTable WHERE user_id = ? AND is_admin = 0";
			return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public AdminUserDTO adminGetUserById(Long id) {
		try {
			String sql = """
				SELECT u.user_id, u.user_name, u.email, u.phone_number, u.user_address, u.is_admin,
				    COUNT(b.booking_id) AS booking_count, COALESCE(SUM(b.total_purchase), 0) AS total_spent
				FROM UserTable u LEFT JOIN Booking b ON u.user_id = b.user_id
				WHERE u.user_id = ? AND u.is_admin = 0
				GROUP BY u.user_id, u.user_name, u.email, u.phone_number, u.user_address, u.is_admin
				""";
			return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(AdminUserDTO.class), id);
		} catch (EmptyResultDataAccessException e) {
			System.out.println(e.toString());
			return null;
		}
	}

	public AdminUserDTO adminGetUserBySDT(String sdt) {
		try {
			String sql = """
				SELECT u.user_id, u.user_name, u.email, u.phone_number, u.user_address, u.is_admin,
				    COUNT(b.booking_id) AS booking_count, COALESCE(SUM(b.total_purchase), 0) AS total_spent
				FROM UserTable u LEFT JOIN Booking b ON u.user_id = b.user_id
				WHERE u.phone_number = ? AND u.is_admin = 0
				GROUP BY u.user_id, u.user_name, u.email, u.phone_number, u.user_address, u.is_admin
				""";
			return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(AdminUserDTO.class), sdt);
		} catch (EmptyResultDataAccessException e) {
			System.out.println(e.toString());
			return null;
		}
	}

	public AdminUserDTO adminGetUserByEmail(String email) {
		try {
			String sql = """
				SELECT u.user_id, u.user_name, u.email, u.phone_number, u.user_address, u.is_admin,
				    COUNT(b.booking_id) AS booking_count, COALESCE(SUM(b.total_purchase), 0) AS total_spent
				FROM UserTable u LEFT JOIN Booking b ON u.user_id = b.user_id
				WHERE u.email = ? AND u.is_admin = 0
				GROUP BY u.user_id, u.user_name, u.email, u.phone_number, u.user_address, u.is_admin
				""";
			return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(AdminUserDTO.class), email);
		} catch (EmptyResultDataAccessException e) {
			System.out.println(e.toString());
			return null;
		}
	}

	public int getTotalUsers() {
		String sql = "SELECT COUNT(*) FROM UserTable WHERE is_admin = 0";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	public List<AdminUserDTO> adminGetAllUsers(int offset, int limit, int type) {
		String sql;
		String orderBy;
		if (type == 1) orderBy = "ORDER BY u.user_id DESC";
		else if (type == 2) orderBy = "ORDER BY u.user_id ASC";
		else if (type == 3) orderBy = "ORDER BY COUNT(b.booking_id) ASC, COALESCE(SUM(b.total_purchase), 0) ASC";
		else if (type == 4) orderBy = "ORDER BY COUNT(b.booking_id) DESC, COALESCE(SUM(b.total_purchase), 0) DESC";
		else if (type == 5) orderBy = "ORDER BY COALESCE(SUM(b.total_purchase), 0) ASC, COUNT(b.booking_id) ASC";
		else orderBy = "ORDER BY COALESCE(SUM(b.total_purchase), 0) DESC, COUNT(b.booking_id) DESC";
		try{
			sql = """
			SELECT * FROM (
			    SELECT u.user_id, u.user_name, u.email, u.phone_number, u.user_address, u.is_admin,
			        COUNT(b.booking_id) AS booking_count,
			        COALESCE(SUM(b.total_purchase), 0) AS total_spent,
			        ROW_NUMBER() OVER (%s) AS rn
			    FROM UserTable u LEFT JOIN Booking b ON u.user_id = b.user_id
			    WHERE u.is_admin = 0
			    GROUP BY u.user_id, u.user_name, u.email, u.phone_number, u.user_address, u.is_admin
			) sub
			WHERE rn BETWEEN ? + 1 AND ? + ?
			""".formatted(orderBy);
			return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(AdminUserDTO.class),
			offset, offset, limit);
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}
}

