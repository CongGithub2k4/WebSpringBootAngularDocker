package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("")
    public String home() {
        long tableCount = getTableCount();
        String result = "Welcome! There are " + tableCount + " tables in your schema.";
        return result;
    }

    private long getTableCount() {
        String sql = "SELECT COUNT(*) FROM user_tables";
        try {
            Long count = jdbcTemplate.queryForObject(sql, Long.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            System.err.println("Error counting tables: " + e.getMessage());
            return -1; // Or throw an exception, depending on your error handling strategy
        }
    }
}