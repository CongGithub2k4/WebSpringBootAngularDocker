package dao;

import entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class NoticeDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NoticeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void addNewNotice(Notice notice) {
        String sql = "INSERT INTO Notice(booking_id ,user_id, notice_type, tour_id, daytime_start, status) " +
                "VALUES(?,?,?,?,?,?)";
        if(notice.getDaytimeStart() == null) return;
        jdbcTemplate.update(sql,notice.getBookingId(),notice.getUserId(),notice.getNoticeType(),
                notice.getTourId(),java.sql.Date.valueOf(notice.getDaytimeStart()),notice.getStatus());
    }
    public void changeNoticeStatus(Integer notice_id) {
        String sql = "UPDATE Notice SET status = 1 WHERE notice_id = ?";
        int rows = jdbcTemplate.update(sql, notice_id);
        if (rows == 0) {
            throw new DataAccessException("No notice found with id " + notice_id) {};
        }
    }
    public void updateAfterAdminCancelTour(Long tour_id, LocalDate daytime_start) {
        //SAU ĐÓ CẦN THÊM THÔNG BÁO VÀO BẢNG Notice
        String sql_select = "SELECT user_id FROM Booking \n" +
            "WHERE tour_id = ? AND daytime_start = ? ";
        List<Long> list_userId = jdbcTemplate.queryForList(sql_select, Long.class,
                            tour_id, java.sql.Date.valueOf(daytime_start));
        String sql_insert = "INSERT INTO Notice( booking_id, user_id, notice_type, tour_id, daytime_start, status) " +
            "VALUES(?,?,?,?,?,?)";
        if (list_userId.isEmpty()) return;
        for(Long user_id : list_userId) {
            String sql_query = "SELECT booking_id FROM Booking \n" +
                "WHERE tour_id = ? AND daytime_start = ? AND user_id = ?";
            List<Long> list_bookingId = jdbcTemplate.queryForList(sql_query, Long.class,
                tour_id, java.sql.Date.valueOf(daytime_start), user_id);
            for(Long booking_id : list_bookingId) {
                jdbcTemplate.update(sql_insert, booking_id, user_id, 2,
                    tour_id, java.sql.Date.valueOf(daytime_start), 0);
            }
        }
    }
    public List<Notice> getAllNotice(Long user_id, int page) {
        int limit = 10;
        int offset = (page - 1) * limit;
        String sql = "SELECT sub.* FROM (" +
                "   SELECT n.*, ROW_NUMBER() OVER (ORDER BY n.notice_id DESC) rn \n" +
                "   FROM Notice n \n" +
                "   WHERE n.user_id = ?" +
                ") sub \n" +
            "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Notice.class), user_id, offset, offset, limit);
    }
    public Integer TOTALgetAllNotice(Long user_id) {
        String sql = "SELECT COUNT(*) " +
            "      FROM Notice \n" +
            "      WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, user_id);
        } catch (EmptyResultDataAccessException e) {
            return 0; // Không có thông báo nào
        }
    }
}
