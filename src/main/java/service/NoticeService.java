package service;

import dao.NoticeDAO;
import entity.Booking;
import entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoticeService {
    private final NoticeDAO noticeDAO;
    private static int NOTICE_PER_PAGE=10;

    @Autowired
    public NoticeService(NoticeDAO noticeDAO) {
        this.noticeDAO = noticeDAO;
    }
    @Transactional
    public void addNewNotice(Notice notice) {
        noticeDAO.addNewNotice(notice);
    }
    @Transactional
    public void changeNoticeStatus(Integer noticeId) {
        noticeDAO.changeNoticeStatus(noticeId);
    }
    public Map<String, Object> getAllNotice(Long user_id, int currentPage) {
        List<Notice> notices = noticeDAO.getAllNotice(user_id,currentPage);
        Integer totalNotice = noticeDAO.TOTALgetAllNotice(user_id);
        int totalPage = (int) Math.ceil((double) totalNotice/NOTICE_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", currentPage);
        response.put("pageSize", NOTICE_PER_PAGE);
        response.put("data", notices);
        // You'd typically fetch the total count and calculate totalPages here.
        response.put("totalPage", totalPage); // Placeholder
        return response;
    }
}
