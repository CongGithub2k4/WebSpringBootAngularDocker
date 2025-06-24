package dao;

import entity.Booking;
import entity.TourParticular;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

@Repository
public class BookingDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /****
     * Sau khi admin hủy tour, status ở TourParticualr đã chỉnh
     * giờ cần update lại status của các Booking liên quan
     * Sau đó gán thêm Notice mới ứng với các user bị
     * @param tour_id
     * @param daytime_start
     */
    public void updateStatus(Long tour_id, LocalDate daytime_start) {
        String sql = "UPDATE Booking \n" +
                " SET status =3 \n" +
                " WHERE tour_id = ? AND daytime_start = ? ";
        jdbcTemplate.update(sql,tour_id,java.sql.Date.valueOf(daytime_start));
    }

    /*****
     * Khi khách hàng hủy Booking đã đặt, thì gán nó status = 2
     * Sau đó TourDAO.returnSlot(Long booking_id, Integer number_slot) với number_slot là số chỗ đã đặt
     * @param booking_id
     */
    public void cancelBooking(Long booking_id) {
        String sql = "UPDATE Booking \n" +
                " SET status = 2 \n" +
                " WHERE booking_id = ?";
        jdbcTemplate.update(sql,booking_id);
    }
    public List<Booking> getAllBookingByUserIDandStatus(Long user_id, Integer status, int page) {
        int limit = 10;
        int offset = (page - 1) * limit;
        String sql = "SELECT sub.* " +
            "FROM (SELECT b.*, t.tour_name, u.user_name, " +
            "      ROW_NUMBER() OVER (ORDER BY b.booking_id DESC) rn " +
            "      FROM (Booking b JOIN Tour t ON b.tour_id = t.tour_id) JOIN UserTable u ON b.user_id = u.user_id " +
            "      WHERE b.user_id = ? AND b.status = ?) sub \n" +
            "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Booking.class), user_id, status, offset, offset, limit);
    }
    public Integer TOTALgetAllBookingByUserIDandStatus(Long user_id, Integer status) {
        String sql = "SELECT COUNT(*) " +
            "      FROM (Booking b JOIN Tour t ON b.tour_id = t.tour_id) JOIN UserTable u ON b.user_id = u.user_id " +
            "      WHERE b.user_id = ? AND b.status = ? ";
        return jdbcTemplate.queryForObject(sql, Integer.class,user_id, status);
    }
    /****
     * User mua vé thì phải thêm vào Booking
     * Đồng thời trừ ở TourParticular đi
     * @param tour
     * @param user_id
     * @param slot_reserved
     */
    @Transactional
    public void userBuyNewTour(TourParticular tour, Long user_id, Integer slot_reserved) {
        String sql = "INSERT INTO Booking (user_id, tour_id, daytime_start, slot_reserved, total_purchase, status)\n" +
            "VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql,user_id, tour.getTourId(),
            java.sql.Date.valueOf(tour.getDaytimeStart()),
            slot_reserved, slot_reserved* tour.getPrice(), 0);
    }

    public Booking adminGetBookingByBookingId(Long booking_id) {
        try {
            String sql = "SELECT * FROM Booking WHERE booking_id = ? ";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Booking.class), booking_id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public Booking systemGetBookingByUserIdTourIdDaytimeStart(Long user_id, Long tour_id,
                                                              LocalDate daytime_start) {
        try {
            String sql = "SELECT * FROM Booking WHERE user_id = ? AND tour_id = ? AND daytime_start = ? ";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Booking.class),
                user_id, tour_id, java.sql.Date.valueOf(daytime_start));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /****
     * Lấy thông tin các Booking cho admin-side
     * @return List <Booking>
     */
    public List<Booking> adminGetAllBooking(Integer status,int page) {
        int limit = 10;
        int offset = (page - 1) * limit;
        String sql = "SELECT sub.* \n" +
                "FROM (SELECT b.*, t.tour_name, u.user_name, " +
                "      ROW_NUMBER() OVER (ORDER BY b.booking_id DESC) rn " +
                "      FROM (Booking b JOIN Tour t ON b.tour_id = t.tour_id) JOIN UserTable u ON b.user_id = u.user_id" +
                "      WHERE b.status = ? ) sub \n" +
                "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Booking.class),
            status, offset, offset, limit);
    }
    public Integer TOTALadminGetAllBooking(Integer status) {
        String sql = "SELECT COUNT(*)" +
                "FROM (Booking b JOIN Tour t ON b.tour_id = t.tour_id) JOIN UserTable u ON b.user_id = u.user_id " +
                " WHERE b.status = ? ";
        return jdbcTemplate.queryForObject(sql, Integer.class, status);
    }

    /****
     * Lấy thông tin các Booking cho amin-side theo số đt người dùng
     * @return List< Booking>
     */
    public List<Booking> adminGetAllBookingByUserSDT(Integer status, String sdt, int page) {
        int limit = 10;
        int offset = (page - 1) * limit;
        String sql = "SELECT sub.* " +
                "FROM (SELECT b.*, t.tour_name, u.user_name, " +
                "      ROW_NUMBER() OVER (ORDER BY b.booking_id DESC) rn " +
                "      FROM (Booking b JOIN Tour t ON b.tour_id = t.tour_id) JOIN UserTable u ON b.user_id = u.user_id " +
                "      WHERE u.phone_number = ? AND b.status = ? ) sub \n" +
                "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Booking.class),
            sdt, status, offset, offset, limit);
    }
    public Integer TOTALadminGetAllBookingByUserSDT(Integer status,String sdt) {
        String sql = "SELECT COUNT(*) " +
                "      FROM (Booking b JOIN Tour t ON b.tour_id = t.tour_id) JOIN UserTable u ON b.user_id = u.user_id " +
                "      WHERE u.phone_number = ? AND b.status = ? ";
        return jdbcTemplate.queryForObject(sql, Integer.class, sdt, status);
    }

    /****
     * Lấy thông tin các Booking cho amin-side theo tourID
     * @return List< Booking>
     */
    /*public List<Booking> adminGetAllBookingByTourID(Long tour_id, int page) {
        int limit = 10;
        int offset = (page - 1) * limit;
        String sql = "SELECT b.*, t.tour_name, u.user_name \n" +
                "FROM ( Booking b JOIN Tour t ON b.tour_id = t.tour_id ) JOIN UserTable u ON b.user_id = u.user_id\n" +
                "WHERE b.tour_id = ?\n" +
                "ORDER BY daytime_create DESC\n" +
                "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Booking.class), tour_id, limit, offset);
    }*/
    public List<Booking> adminGetAllBookingByTourID(Integer status,Long tour_id, int page) {
        int limit = 10;
        int offset = (page - 1) * limit;
        String sql = "SELECT sub.*  " +
                "FROM (SELECT b.*, t.tour_name, u.user_name, " +
                "      ROW_NUMBER() OVER (ORDER BY b.booking_id DESC) rn " +
                "      FROM (Booking b JOIN Tour t ON b.tour_id = t.tour_id) JOIN UserTable u ON b.user_id = u.user_id " +
                "      WHERE b.tour_id = ? AND b.status = ? ) sub \n" +
                "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Booking.class),
            tour_id, status, offset, offset, limit);
    }
    public Integer TOTALadminGetAllBookingByTourID(Integer status,Long tour_id) {
        String sql = "SELECT COUNT(*) " +
                "      FROM (Booking b JOIN Tour t ON b.tour_id = t.tour_id) JOIN UserTable u ON b.user_id = u.user_id " +
                "      WHERE b.tour_id = ? AND b.status = ? ";
        return jdbcTemplate.queryForObject(sql, Integer.class, tour_id, status);
    }

    public List<Booking> adminGetAllBookingByUserSDTandTourID(Integer status,String sdt, Long tour_id, int page) {
        int limit = 10;
        int offset = (page - 1) * limit;
        String sql = "SELECT sub.* " +
            "FROM (SELECT b.*, t.tour_name, u.user_name, " +
            "      ROW_NUMBER() OVER (ORDER BY b.booking_id DESC) rn " +
            "      FROM (Booking b JOIN Tour t ON b.tour_id = t.tour_id) JOIN UserTable u ON b.user_id = u.user_id " +
            "      WHERE b.tour_id = ? AND u.phone_number = ? AND b.status = ? ) sub \n" +
            "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Booking.class),
            tour_id ,sdt, status, offset, offset, limit);
    }
    public Integer TOTALadminGetAllBookingByUserSDTandTourID(Integer status,String sdt ,Long tour_id) {
        String sql = "SELECT COUNT(*) " +
            "      FROM (Booking b JOIN Tour t ON b.tour_id = t.tour_id) JOIN UserTable u ON b.user_id = u.user_id " +
            "      WHERE b.tour_id = ? AND u.phone_number = ? AND b.status = ? ";
        return jdbcTemplate.queryForObject(sql, Integer.class, tour_id, sdt, status);
    }

    public List<Booking> adminGetAllBookingOfTourParticular(Long tour_id, LocalDate daytime_start, int page) {
        int limit = 10;
        int offset = (page - 1) * limit;
        String sql = "SELECT sub.* " +
            "FROM (SELECT b.*, t.tour_name, u.user_name, " +
            "      ROW_NUMBER() OVER (ORDER BY b.booking_id DESC) rn " +
            "      FROM (Booking b JOIN Tour t ON b.tour_id = t.tour_id) JOIN UserTable u ON b.user_id = u.user_id " +
            "      WHERE b.tour_id = ? AND b.daytime_start = ? ) sub \n" +
            "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Booking.class),
            tour_id , java.sql.Date.valueOf(daytime_start) , offset, offset, limit);
    }
    public Integer TOTALadminGetAllBookingOfTourParticular(Long tour_id, LocalDate daytime_start) {
        String sql = "SELECT COUNT(*) " +
            "      FROM (Booking b JOIN Tour t ON b.tour_id = t.tour_id) JOIN UserTable u ON b.user_id = u.user_id " +
            "      WHERE b.tour_id = ? AND b.daytime_start = ? ";
        return jdbcTemplate.queryForObject(sql, Integer.class, tour_id, java.sql.Date.valueOf(daytime_start));
    }
}
