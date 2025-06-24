package dao;

import entity.TourParticular;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class TourDAO {
    private final JdbcTemplate jdbcTemplate;
    public static final Integer MAX_TOUR_PER_PAGE = 10;

    @Autowired
    public TourDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> findAllStartDestination() {
        String sql ="SELECT DISTINCT start_destination FROM Tour";
        return jdbcTemplate.query(sql,new SingleColumnRowMapper<>(String.class));
    }

    /******
     * Lấy danh sách các điểm đến
     * @return List<String>
     */
    public List<String> findAllDestination() {
        String sql ="SELECT DISTINCT thoughout_destination FROM TourDestination";
        return jdbcTemplate.query(sql,new SingleColumnRowMapper<>(String.class));
    }

    /*****
     * Tìm các điểm đến mà Tour đi qua dựa theo tour_id của nó
     * @param tour_id
     * @return List <String> : danh sách các điểm đến thoughout_destination theo tour_id
     */
    public List<String> findAllDestinationByTourId(Long tour_id) {
        String sql = "SELECT DISTINCT thoughout_destination " +
                "FROM TourDestination " +
                "WHERE tour_id = ?";
        return jdbcTemplate.query(sql, new SingleColumnRowMapper<>(String.class), tour_id);
    }

    /***
     * Chèn thêm vào DB các điểm đến của 1 Tour mới
     * @param des
     * @param tour_id
     */
    public void insertDestination(List<String> des, Number tour_id) {
        String sqlDest = "INSERT INTO TourDestination VALUES (?, ?)";
        for (String place : des) {
            jdbcTemplate.update(sqlDest, tour_id.longValue(), place);
        }
    }

    public List<LocalDate> findAllDaytimeStartOfTourId(Long tourId) {
        String sql = "SELECT daytime_start FROM TourParticular WHERE tour_id = ? " +
            "AND daytime_start >= SYSDATE";
        return jdbcTemplate.queryForList(sql, LocalDate.class,tourId);
    }

    public List<Long> findAllTourId() {
        String sql = "SELECT DISTINCT tour_id FROM Tour";
        return jdbcTemplate.queryForList(sql, Long.class);
    }

    /***
     * Ta thêm 1 Tour mới hoàn toàn. Khi ấn vào Nút "Thêm Tour mới" , 1 hàm DAO sẽ lấy tour_id lớn nhất .
     * tour_id của tour mới sẽ là tour_id lớn kia + 1.(Mặc định Frontend sẽ lấy và lưu vào trường input - ko thể thay đổi)
     * vì điền cả các ngày khởi hành cụ thể, các điểm đến nên cần INSERT vào 3 bảng
     * Tour, TourParticular, TourDestination
     * @param tour
     */
//    public void addNewTour(TourParticular tour, Long user_id) {
//        String query = "SELECT admin_id FROM AdminTable WHERE user_id = ?";
//        Integer admin_id = jdbcTemplate.queryForObject(query, Integer.class, user_id);
//        // Insert vào bảng Tour trước và lấy ra tour_id
//        SimpleJdbcInsert insertTour = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName("Tour")
//                .usingGeneratedKeyColumns("tour_id");
//
//        Map<String, Object> tourParams = new HashMap<>();
//        tourParams.put("start_destination", tour.getStartDestination());
//        tourParams.put("tour_name", tour.getTourName());
//        tourParams.put("day_number", tour.getDayNumber());
//        tourParams.put("night_number", tour.getNightNumber());
//        tourParams.put("link_image", tour.getLinkImage());
//        tourParams.put("admin_id_create_tour", admin_id);
//
//        Number tourId = insertTour.executeAndReturnKey(tourParams);
//
//        //Vì lệnh bên trên khi chèn thông tin Tour nó để trống cột daytime_create_tour mặc
//        // dù cột này DEFAULT SYSDATE . Chỉ có lệnh INSERT ... mới khiến nó tự cập nhật
//        String sql_add = "UPDATE Tour SET daytime_create_tour = SYSDATE WHERE tour_id = ?";
//        jdbcTemplate.update(sql_add,tourId);
//
//        // Insert vào TourParticular
//        String sqlParticular = "INSERT INTO TourParticular " +
//                "(tour_id, daytime_start, total_slot, slot_remain, price, admin_id) " +
//                "VALUES (?, ?, ?, ?, ?, ?)";
//
//        jdbcTemplate.update(sqlParticular,
//                tourId.longValue(),
//                java.sql.Date.valueOf(tour.getDaytimeStart()), // LocalDate -> java.sql.Date
//                tour.getTotalSlot(),
//                tour.getSlotRemain(),
//                tour.getPrice(),
//                admin_id);
//        //Thêm vào bảng TourDestination
//        insertDestination(tour.getThoughoutDestination(), tourId);
//    }
    public void addNewTour(TourParticular tour, Long user_id) {
        Integer admin_id;
        try {
            String query = "SELECT admin_id FROM AdminTable WHERE user_id = ?";
            admin_id = jdbcTemplate.queryForObject(query, Integer.class, user_id);
        } catch (EmptyResultDataAccessException e) {
            System.err.println("ERROR: User " + user_id + " is not an admin or admin_id not found in AdminTable.");
            e.printStackTrace(); // Print full stack trace
            throw new RuntimeException("Admin ID not found for user: " + user_id, e); // Re-throw to propagate transaction rollback
        } catch (Exception e) {
            System.err.println("ERROR during admin_id lookup: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to lookup admin ID for user: " + user_id, e);
        }

        // ... (rest of the code)

        Number tourId;
        try {
            SimpleJdbcInsert insertTour = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Tour")
                .usingGeneratedKeyColumns("tour_id");

            Map<String, Object> tourParams = new HashMap<>();
            tourParams.put("start_destination", tour.getStartDestination());
            tourParams.put("tour_name", tour.getTourName());
            tourParams.put("day_number", tour.getDayNumber());
            tourParams.put("night_number", tour.getNightNumber());
            tourParams.put("link_image", tour.getLinkImage());
            tourParams.put("admin_id_create_tour", admin_id); // admin_id might be null here if previous try-catch allows

            tourId = insertTour.executeAndReturnKey(tourParams);
        } catch (DataIntegrityViolationException e) {
            System.err.println("ERROR: Data integrity violation when inserting into Tour table: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to insert into Tour table (data integrity): " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("ERROR during Tour insertion: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to insert into Tour table: " + e.getMessage(), e);
        }

        try {
            String sql_add = "UPDATE Tour SET daytime_create_tour = SYSDATE WHERE tour_id = ?";
            jdbcTemplate.update(sql_add,tourId);
        } catch (Exception e) {
            System.err.println("ERROR during Tour update (daytime_create_tour): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update Tour daytime_create_tour: " + e.getMessage(), e);
        }

        try {
            String sqlParticular = "INSERT INTO TourParticular " +
                "(tour_id, daytime_start, total_slot, slot_remain, price, admin_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(sqlParticular,
                tourId.longValue(),
                java.sql.Date.valueOf(tour.getDaytimeStart()),
                tour.getTotalSlot(),
                tour.getSlotRemain(),
                tour.getPrice(),
                admin_id);
        } catch (NullPointerException e) {
            System.err.println("ERROR: tour.getDaytimeStart() is null or conversion failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Invalid daytime_start value: " + e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            System.err.println("ERROR: Data integrity violation when inserting into TourParticular: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to insert into TourParticular (data integrity): " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("ERROR during TourParticular insertion: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to insert into TourParticular: " + e.getMessage(), e);
        }

        // Add similar try-catch for insertDestination
        try {
            insertDestination(tour.getThoughoutDestination(), tourId);
        } catch (Exception e) {
            System.err.println("ERROR during insertDestination: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to insert TourDestination: " + e.getMessage(), e);
        }
    }

    public void addNewDaytimeStart(Long tour_id, LocalDate daytime_start, Integer slot, Integer price, Long user_id) {
        String query = "SELECT admin_id FROM AdminTable WHERE user_id = ?";
        Integer admin_id = jdbcTemplate.queryForObject(query, Integer.class, user_id);
        String sql = "INSERT INTO TourParticular (tour_id, daytime_start, total_slot, slot_remain, price, admin_id) \n" +
            " VALUES(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,tour_id,java.sql.Date.valueOf(daytime_start),
            slot,slot,price,admin_id);
    }

    /****
     * khi admin hủy lịch khởi hành của 1 TourParticular thì nó sẽ đặt status =3
     * sau đó BookingDAO sẽ chỉnh sửa status của các booking tương ứng = 3
     * hàm BookingDAO.updateStatus
     * @param tour_id
     * @param daytime_start
     */
    public void deleteTourParticular(Long tour_id, LocalDate daytime_start) {
        String sql = "UPDATE TourParticular \n" +
                " SET status = 3 \n" +
                " WHERE tour_id = ? AND daytime_start = ? ";
        jdbcTemplate.update(sql,tour_id,java.sql.Date.valueOf(daytime_start));
    }

    /*****
     * Khi khách hàng hủy Tour đã đặt, thì cần cộng lại số lượng về bảng TourParticular
     * @param tour_id
     * @param daytime_start
     * @param number_slot
     */
    public void returnSlot(Long tour_id, LocalDate daytime_start, Integer number_slot) {
        String sql = "UPDATE TourParticular \n" +
                " SET slot_remain = slot_remain + ? \n" +
                " WHERE tour_id = ? AND daytime_start = ?";
        jdbcTemplate.update(sql,number_slot,tour_id,java.sql.Date.valueOf(daytime_start));
    }

    /*****
     * Khi khách hàng mua 1 Tour, thì cần trừ số lượng về bảng TourParticular
     * Trước đó đã thêm 1 dòng vào Booking rồi
     * @param tour_id
     * @param daytime_start
     * @param number_slot
     */
    public void subtractSlot(Long tour_id, LocalDate daytime_start, Integer number_slot) {
        String sql = "UPDATE TourParticular \n" +
                " SET slot_remain = slot_remain - ? \n" +
                " WHERE tour_id = ? AND daytime_start = ? \n";
        jdbcTemplate.update(sql,number_slot,tour_id,java.sql.Date.valueOf(daytime_start));
    }

    /***
     * bên admin side và bên giao diện Tourlist khám phá của user
     * Cái này cần 4 lần gọi để liệt kê ra được các Tour theo từng tình trạng
     * @param status
     * @return
     */
    public List<TourParticular> findAllTourByStatus(Integer status, int page, String orderByClause) {
        int limit = MAX_TOUR_PER_PAGE;
        int offset = (page - 1) * limit;
        String sql = "SELECT sub.* \n"+
                "FROM (SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image, " +
                "      ROW_NUMBER() OVER (ORDER BY " + orderByClause + " ) rn \n" +
                "      FROM Tour t JOIN TourParticular tp ON t.tour_id = tp.tour_id \n" +
                "      WHERE tp.status = ?) sub \n" +
                "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        List<TourParticular> a = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TourParticular.class),
            status, offset, offset, limit);
        for (TourParticular t : a) {
            t.setThoughoutDestination(findAllDestinationByTourId(t.getTourId()));
        }
        return a;
    }
    public Integer TOTALfindAllTourByStatus(Integer status) {
        String sql = //"SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image\n" +
            "SELECT COUNT(*) \n"+
                " FROM Tour t JOIN TourParticular tp ON t.tour_id = tp.tour_id \n" +
                " WHERE tp.status = ? ";
        return jdbcTemplate.queryForObject(sql, Integer.class,
            status);
    }
    /***
     * bên admin side Cái này cần 1 lần gọi để liệt kê ra được các Tour
     * Bao gồm tất cả các loại status
     * @param tour_id
     * @return TourParticular
     */
    public List<TourParticular> findAllTourParticularByIdAndStatus(Long tour_id, Integer status, int page, String orderByClause) {
        int limit = MAX_TOUR_PER_PAGE;
        int offset = (page - 1) * limit;

        String sql = "SELECT sub.* \n" +
            "FROM (SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image, \n" +
            "             ROW_NUMBER() OVER (ORDER BY " + orderByClause + " ) rn \n" +
            "      FROM Tour t JOIN TourParticular tp ON t.tour_id = tp.tour_id \n" +
            "      WHERE t.tour_id = ? AND tp.status = ?) sub \n" +
            "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";

        List<TourParticular> tours = jdbcTemplate.query(sql,
            new BeanPropertyRowMapper<>(TourParticular.class),
            tour_id, status, offset, offset, limit);

        for (TourParticular tour : tours) {
            tour.setThoughoutDestination(findAllDestinationByTourId(tour_id));
        }

        return tours;
    }

    public Integer TOTALfindAllTourParticularByIdAndStatus(Long tour_id, Integer status) {
        String sql = "SELECT COUNT(*)"+
                "FROM Tour t JOIN TourParticular tp ON t.tour_id = tp.tour_id\n" +
                "WHERE t.tour_id = ?  AND tp.status = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class,tour_id,status);
    }

    /******
     * ADMIN SIDE Tìm các tour có điểm khởi hành, điểm đén, time phù hợp Sau đó trả về List<TourParticular>
     * Có cần lọc theo status
     * User side bên lọc tour cx như thế nhưng chỉ lọc status =0
     * @param start_destination, thoughout_destination, daytime_start
     * @return List<Tour>
     */
    public List<TourParticular> findAllTourParticularBy_Start_End_DaytimeStart_Status(String start_destination,
                                                                                      String thoughout_destination, LocalDate daytime_start, Integer status, int page, String orderByClause) {
        int limit = MAX_TOUR_PER_PAGE;
        int offset = (page - 1) * limit;
        String sql = "SELECT sub.* \n"+
                "FROM (SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image, " +
                "      ROW_NUMBER() OVER (ORDER BY " + orderByClause + " ) rn " +
                "      FROM Tour t JOIN TourParticular tp ON t.tour_id = tp.tour_id " +
                "      JOIN (SELECT DISTINCT tour_id FROM TourDestination WHERE thoughout_destination = ?) endplace ON t.tour_id = endplace.tour_id " +
                "      JOIN (SELECT DISTINCT tour_id FROM Tour WHERE start_destination = ?) startplace ON t.tour_id = startplace.tour_id " +
                "      WHERE tp.daytime_start >= ? AND tp.status = ?) sub \n" +
                "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        List<TourParticular> a = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TourParticular.class),
                thoughout_destination, start_destination, java.sql.Date.valueOf(daytime_start), status, offset, offset, limit);
        for (TourParticular t : a) {
            t.setThoughoutDestination(findAllDestinationByTourId(t.getTourId()));
        }
        return a;
    }
    public Integer TOTALfindAllTourParticularBy_Start_End_DaytimeStart_Status(String start_destination, String thoughout_destination, LocalDate daytime_start, Integer status) {
        String sql = "SELECT COUNT(*)\n" +
                "FROM Tour t\n" +
                "JOIN TourParticular tp ON t.tour_id = tp.tour_id\n" +
                "JOIN (SELECT DISTINCT tour_id FROM TourDestination WHERE thoughout_destination = ?) endplace ON t.tour_id = endplace.tour_id\n" +
                "JOIN (SELECT DISTINCT tour_id FROM Tour WHERE start_destination = ?) startplace ON t.tour_id = startplace.tour_id\n" +
                "WHERE tp.daytime_start >= ? \n" +
                "  AND tp.status = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class,
                thoughout_destination, start_destination, java.sql.Date.valueOf(daytime_start), status);
    }
    public List<TourParticular> findAllTourParticularBy_Start_Status(String start_destination, Integer status, int page, String orderByClause) {
        int limit = MAX_TOUR_PER_PAGE;
        int offset = (page - 1) * limit;
        String sql = //"SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image " +
                "SELECT sub.* \n"+
                "FROM (SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image, " +
                "      ROW_NUMBER() OVER (ORDER BY " + orderByClause + " ) rn " +
                "      FROM Tour t JOIN TourParticular tp ON t.tour_id = tp.tour_id " +
                "      JOIN (SELECT DISTINCT tour_id FROM Tour WHERE start_destination = ?) startplace ON t.tour_id = startplace.tour_id " +
                "      WHERE tp.status = ?) sub \n" +
                "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        List<TourParticular> a = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TourParticular.class),
                start_destination, status, offset, offset, limit);
        for (TourParticular t : a) {
            t.setThoughoutDestination(findAllDestinationByTourId(t.getTourId()));
        }
        return a;
    }
    public Integer TOTALfindAllTourParticularBy_Start_Status(String start_destination, Integer status) {
        String sql = "SELECT COUNT(*)\n" +
                " FROM Tour t\n" +
                "   JOIN TourParticular tp ON t.tour_id = tp.tour_id\n" +
                "   JOIN (SELECT DISTINCT tour_id FROM Tour WHERE start_destination = ?) startplace ON t.tour_id = startplace.tour_id\n" +
                " WHERE tp.status = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, start_destination, status);
    }
    public List<TourParticular> findAllTourParticularBy_End_Status(String thoughout_destination, Integer status, int page, String orderByClause) {
        int limit = MAX_TOUR_PER_PAGE;
        int offset = (page - 1) * limit;
        String sql = //"SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image " +
                "SELECT sub.* \n"+
                "FROM (SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image, " +
                "      ROW_NUMBER() OVER (ORDER BY " + orderByClause + " ) rn " +
                "      FROM Tour t JOIN TourParticular tp ON t.tour_id = tp.tour_id " +
                "      JOIN (SELECT DISTINCT tour_id FROM TourDestination WHERE thoughout_destination = ?) endplace ON t.tour_id = endplace.tour_id " +
                "      WHERE tp.status = ?) sub \n" +
                "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        List<TourParticular> a = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TourParticular.class),
                thoughout_destination, status, offset, offset, limit);
        for (TourParticular t : a) {
            t.setThoughoutDestination(findAllDestinationByTourId(t.getTourId()));
        }
        return a;
    }
    public Integer TOTALfindAllTourParticularBy_End_Status(String thoughout_destination, Integer status) {
        String sql = "SELECT COUNT(*) \n" +
            "FROM Tour t \n" +
            " JOIN TourParticular tp ON t.tour_id = tp.tour_id\n" +
            " JOIN (SELECT DISTINCT tour_id FROM TourDestination WHERE thoughout_destination = ?) endplace \n" +
            "  ON t.tour_id = endplace.tour_id \n" +
            " WHERE tp.status = ? \n";
        return jdbcTemplate.queryForObject(sql, Integer.class,thoughout_destination, status);
    }
    public List<TourParticular> findAllTourParticularBy_DaytimeStart_Status(LocalDate daytime_start, Integer status, int page, String orderByClause) {
        int limit = MAX_TOUR_PER_PAGE;
        int offset = (page - 1) * limit;
        String sql = //"SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image " +
                "SELECT sub.* \n"+
                "FROM (SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image, " +
                "      ROW_NUMBER() OVER (ORDER BY " + orderByClause + " ) rn " +
                "      FROM Tour t JOIN TourParticular tp ON t.tour_id = tp.tour_id " +
                "      WHERE tp.daytime_start >= ? AND tp.status = ?) sub \n" +
                "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        List<TourParticular> a = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TourParticular.class),
                java.sql.Date.valueOf(daytime_start), status, offset, offset, limit);
        for (TourParticular t : a) {
            t.setThoughoutDestination(findAllDestinationByTourId(t.getTourId()));
        }
        return a;
    }
    public Integer TOTALfindAllTourParticularBy_DaytimeStart_Status(LocalDate daytime_start, Integer status) {
        String sql = "SELECT COUNT(*)\n" +
                " FROM Tour t\n" +
                "   JOIN TourParticular tp ON t.tour_id = tp.tour_id\n" +
                " WHERE tp.daytime_start >= ?\n" +
                "  AND tp.status = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class,java.sql.Date.valueOf(daytime_start), status);
    }
    public List<TourParticular> findAllTourParticularBy_Start_DaytimeStart_Status(String start_destination, LocalDate daytime_start, Integer status, int page, String orderByClause) {
        int limit = MAX_TOUR_PER_PAGE;
        int offset = (page - 1) * limit;
        String sql =
                "SELECT sub.* \n"+
                "FROM (SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image, " +
                "      ROW_NUMBER() OVER (ORDER BY " + orderByClause +" ) rn " +
                "      FROM Tour t JOIN TourParticular tp ON t.tour_id = tp.tour_id " +
                "      JOIN (SELECT DISTINCT tour_id FROM Tour WHERE start_destination = ?) startplace ON t.tour_id = startplace.tour_id " +
                "      WHERE tp.daytime_start >= ? AND tp.status = ?) sub \n" +
                "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        List<TourParticular> a = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TourParticular.class),
                start_destination, java.sql.Date.valueOf(daytime_start), status, offset, offset, limit);
        for (TourParticular t : a) {
            t.setThoughoutDestination(findAllDestinationByTourId(t.getTourId()));
        }
        return a;
    }
    public Integer TOTALfindAllTourParticularBy_Start_DaytimeStart_Status(String start_destination, LocalDate daytime_start, Integer status) {
        String sql = "SELECT COUNT(*)\n" +
                " FROM Tour t\n" +
                "   JOIN TourParticular tp ON t.tour_id = tp.tour_id\n" +
                "   JOIN (SELECT DISTINCT tour_id FROM Tour WHERE start_destination = ?) startplace ON t.tour_id = startplace.tour_id\n" +
                " WHERE tp.daytime_start >= ?\n" +
                "  AND tp.status = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class,
                start_destination, java.sql.Date.valueOf(daytime_start), status);
    }
    public List<TourParticular> findAllTourParticularBy_End_DaytimeStart_Status(String thoughout_destination, LocalDate daytime_start, Integer status, int page, String orderByClause) {
        int limit = MAX_TOUR_PER_PAGE;
        int offset = (page - 1) * limit;
        String sql = //"SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image " +
                "SELECT sub.* \n"+
                "FROM (SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image, " +
                "      ROW_NUMBER() OVER (ORDER BY " + orderByClause + " ) rn " +
                "      FROM Tour t JOIN TourParticular tp ON t.tour_id = tp.tour_id " +
                "      JOIN (SELECT DISTINCT tour_id FROM TourDestination WHERE thoughout_destination = ?) endplace ON t.tour_id = endplace.tour_id " +
                "      WHERE tp.daytime_start >= ? AND tp.status = ?) sub \n" +
                "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        List<TourParticular> a = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TourParticular.class),
                thoughout_destination, java.sql.Date.valueOf(daytime_start), status, offset, offset, limit);
        for (TourParticular t : a) {
            t.setThoughoutDestination(findAllDestinationByTourId(t.getTourId()));
        }
        return a;
    }
    public Integer TOTALfindAllTourParticularBy_End_DaytimeStart_Status(String thoughout_destination, LocalDate daytime_start, Integer status) {
        String sql = "SELECT COUNT(*) \n"+
                        " FROM Tour t, TourParticular tp,\n" +
                        " WHERE tp.daytime_start >= ? AND t.tour_id = tp.tour_id AND tp.status = ? " +
                            " AND (tp.tour_id IN (SELECT DISTINCT tour_id FROM TourDestination WHERE thoughout_destination = ? ) ) \n" +
                        " ORDER BY tp.daytime_start DESC\n";
        return jdbcTemplate.queryForObject(sql, Integer.class, java.sql.Date.valueOf(daytime_start), status, thoughout_destination);
    }
    public List<TourParticular> findAllTourParticularBy_Start_End_Status(String start_destination, String thoughout_destination, Integer status, int page, String orderByClause) {
        int limit = MAX_TOUR_PER_PAGE;
        int offset = (page - 1) * limit;
        String sql = //"SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image " +
                "SELECT sub.* \n"+
                "FROM (SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image, " +
                "      ROW_NUMBER() OVER (ORDER BY " + orderByClause + " ) rn " +
                "      FROM Tour t JOIN TourParticular tp ON t.tour_id = tp.tour_id " +
                "      JOIN (SELECT DISTINCT tour_id FROM TourDestination WHERE thoughout_destination = ?) endplace ON t.tour_id = endplace.tour_id " +
                "      JOIN (SELECT DISTINCT tour_id FROM Tour WHERE start_destination = ?) startplace ON t.tour_id = startplace.tour_id " +
                "      WHERE tp.status = ?) sub \n" +
                "WHERE sub.rn BETWEEN ? + 1 AND ? + ?";
        List<TourParticular> a = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TourParticular.class),
                thoughout_destination, start_destination, status, offset, offset, limit);
        for (TourParticular t : a) {
            t.setThoughoutDestination(findAllDestinationByTourId(t.getTourId()));
        }
        return a;
    }
    public Integer TOTALfindAllTourParticularBy_Start_End_Status(String start_destination, String thoughout_destination, Integer status) {
        String sql = "SELECT COUNT(*) \n"+
                        " FROM Tour t, TourParticular tp \n" +
                        " WHERE t.tour_id = tp.tour_id AND tp.status = ? " +
                            " AND (tp.tour_id IN (SELECT DISTINCT tour_id FROM TourDestination WHERE thoughout_destination = ? ) ) \n" +
                            " AND (t.tour_id IN (SELECT DISTINCT tour_id FROM Tour WHERE start_destination = ? ) ) \n" +
                        " ORDER BY tp.daytime_start DESC\n";
        return jdbcTemplate.queryForObject(sql, Integer.class,
            status,thoughout_destination, start_destination);
    }




    /***
     * Lấy 3 tour đầu cho giao diện home
     * home/, home, ....
     */
    public List<TourParticular> findTop3() {
        String sql = "SELECT * \n" +
                "FROM (SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image \n" +
                "      FROM Tour t JOIN TourParticular tp ON t.tour_id = tp.tour_id \n" +
                "      WHERE tp.status = 0 \n" +
                "      ORDER BY tp.daytime_start DESC) sub \n" +
                "WHERE ROWNUM <= 3";
        List<TourParticular> a = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TourParticular.class));
        for (TourParticular t : a) {
            t.setThoughoutDestination(findAllDestinationByTourId(t.getTourId()));
        }
        return a;
    }

    public Optional<TourParticular> getTour(Long tour_id, LocalDate daytime_start) {
        String sql = "SELECT tp.*, t.start_destination, t.tour_name, t.day_number, t.night_number, t.link_image \n" +
                " FROM Tour t JOIN TourParticular tp ON t.tour_id = tp.tour_id \n" +
                " WHERE tp.tour_id = ? AND tp.daytime_start = ?";
        try {
            TourParticular tourParticular = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(TourParticular.class),
                    tour_id, java.sql.Date.valueOf(daytime_start));
            tourParticular.setThoughoutDestination(findAllDestinationByTourId(tourParticular.getTourId()));
            return Optional.of(tourParticular);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
