package controller;

import config.ExtractFromSpringSecurityContext;
import entity.CustomUserDetails;
import entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import service.NoticeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // http://localhost:8080/notice/add-new
    @PostMapping("/add-new")
    public ResponseEntity<Void> addNewNotice(@RequestBody Notice notice) {
        noticeService.addNewNotice(notice);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // http://localhost:8080/notice/mark-as-read/3
    @PutMapping("/mark-as-read/{noticeId}")
    public ResponseEntity<Void> markNoticeAsRead(@PathVariable Integer noticeId) {
        try {
            Long user_id = ExtractFromSpringSecurityContext.extractUserIdFromSecurityContext();
            noticeService.changeNoticeStatus(noticeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (RuntimeException ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Bạn chưa đăng nhập");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // http://localhost:8080/notice/unread?page=1
    @GetMapping("/unread")
    public ResponseEntity<Map<String, Object>> getNotices(@RequestParam(defaultValue = "1") int page) {
        try {
            Long user_id = ExtractFromSpringSecurityContext.extractUserIdFromSecurityContext();
            Map<String, Object> notices = noticeService.getAllNotice(user_id, page);
            return ResponseEntity.ok(notices);
        } catch (RuntimeException ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Bạn chưa đăng nhập");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

}
