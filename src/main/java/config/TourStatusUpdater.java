package config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
public class TourStatusUpdater {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TourStatusUpdater(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void chageTourToGoing(String sql) {
        jdbcTemplate.update(sql);
    }
    @Transactional
    public void chageTourToGone(String sql2) {
        jdbcTemplate.update(sql2);
    }
    @Transactional
    public void chageBookingToGone(String sql3) {
        jdbcTemplate.update(sql3);
    }
    //@PostConstruct

    @EventListener(ApplicationReadyEvent.class)
    public void updateTourStatusesOnStartup() {
        String sql = "UPDATE TourParticular tp \n" +
            " SET tp.status = 1 \n" +
            " WHERE tp.daytime_start <= SYSDATE \n" +
            "  AND EXISTS ( \n" +
            "    SELECT 1 \n" +
            "    FROM Tour t \n" +
            "    WHERE t.tour_id = tp.tour_id \n" +
            "      AND tp.daytime_start + t.day_number >= SYSDATE \n" +
            "  )";
        String sql2 = "UPDATE TourParticular tp \n" +
            " SET tp.status = 2 \n" +
            " WHERE EXISTS ( \n" +
            "    SELECT 1 \n" +
            "    FROM Tour t \n" +
            "    WHERE t.tour_id = tp.tour_id \n" +
            "      AND tp.daytime_start + t.day_number < SYSDATE \n" +
            "  )";
        String sql3 = "UPDATE Booking b \n" +
            "SET b.status = 1 \n" +
            "WHERE EXISTS ( \n" +
            "    SELECT 1 \n" +
            "    FROM Tour t \n" +
            "    WHERE t.tour_id = b.tour_id \n" +
            "      AND b.daytime_start + t.day_number < SYSDATE \n" +
            "  )";
        System.out.println("\n\n Đang chạy hàm update database \n\n");

        chageTourToGoing(sql);
        chageTourToGone(sql2);
        chageBookingToGone(sql3);
    }
}
