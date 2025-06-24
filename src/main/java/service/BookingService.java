package service;

import dao.BookingDAO;
import dao.NoticeDAO;
import dao.TourDAO;
import entity.Booking;
import entity.Notice;
import entity.TourParticular;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {

    private final BookingDAO bookingDAO;
    private final TourDAO tourDAO;
    private final NoticeDAO noticeDAO;
    public static final Integer BOOKING_PER_PAGE = 10;

    @Autowired
    public BookingService(BookingDAO bookingDAO, TourDAO tourDAO, NoticeDAO noticeDAO) {
        this.bookingDAO = bookingDAO;
        this.tourDAO = tourDAO;
        this.noticeDAO = noticeDAO;
    }

    @Transactional
    public void cancelBooking(Booking booking) {
        bookingDAO.cancelBooking(booking.getBookingId());
        // In a real application, you might also need to handle refunds or other related logic.
        tourDAO.returnSlot(booking.getTourId(), LocalDate.from(booking.getDaytimeStart()), booking.getSlotReserved());
        Notice notice = new Notice();
        notice.setBookingId(booking.getBookingId());
        notice.setUserId(booking.getUserId());
        notice.setNoticeType(1);
        notice.setTourId(booking.getTourId());
        notice.setDaytimeStart(booking.getDaytimeStart());
        notice.setStatus(0);
        noticeDAO.addNewNotice(notice);
    }
    public Map<String, Object> getAllBookingByUserIDandStatus(Long user_id, Integer status, int currentPage) {
        List<Booking> bookings = bookingDAO.getAllBookingByUserIDandStatus(user_id, status, currentPage);
        Integer totalBooking = bookingDAO.TOTALgetAllBookingByUserIDandStatus(user_id,status);
        int totalPage = (int) Math.ceil((double) totalBooking / BOOKING_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", currentPage);
        response.put("pageSize", BOOKING_PER_PAGE);
        response.put("data", bookings);
        response.put("totalPage", totalPage); // Placeholder
        return response;
    }

    @Transactional
    public void userBuyNewTour(TourParticular tour, Long user_id, Integer slot_reserved) {
        bookingDAO.userBuyNewTour(tour, user_id, slot_reserved);
        tourDAO.subtractSlot(tour.getTourId(), tour.getDaytimeStart(), slot_reserved);
        Booking b = bookingDAO.systemGetBookingByUserIdTourIdDaytimeStart(user_id,
            tour.getTourId(), tour.getDaytimeStart() );
        System.out.println("Mã booking sau khi mua mã tour " + tour.getTourId()+" của user "+ user_id
            + " slot " + slot_reserved +" là "+ b.getBookingId());
        Notice notice = new Notice();
        notice.setBookingId(b.getBookingId());
        notice.setUserId(user_id);
        notice.setNoticeType(0);
        notice.setTourId(tour.getTourId());
        notice.setDaytimeStart(tour.getDaytimeStart());
        notice.setStatus(0);
        noticeDAO.addNewNotice(notice);
    }

    public Booking adminGetBookingByBookingId(Long booking_id) {
        return bookingDAO.adminGetBookingByBookingId(booking_id);
    }

    public Map<String, Object> adminGetAllBooking(Integer status, int currentPage) {
        List<Booking> bookings = bookingDAO.adminGetAllBooking(status,currentPage);
        // In a real application, you would likely have a method in the DAO to count total bookings.
        // For this example, we'll just return the fetched list.
        Integer totalBooking = bookingDAO.TOTALadminGetAllBooking(status);
        int totalPage = (int) Math.ceil((double) totalBooking / BOOKING_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", currentPage);
        response.put("pageSize", BOOKING_PER_PAGE);
        response.put("data", bookings);
        // You'd typically fetch the total count and calculate totalPages here.
        response.put("totalPage", totalPage); // Placeholder
        return response;
    }

    public Map<String, Object> adminGetAllBookingByUserSDT(Integer status, String sdt, int currentPage) {
        List<Booking> bookings = bookingDAO.adminGetAllBookingByUserSDT(status ,sdt, currentPage);
        Integer totalBooking = bookingDAO.TOTALadminGetAllBookingByUserSDT(status,sdt);
        int totalPage = (int) Math.ceil((double) totalBooking / BOOKING_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", currentPage);
        response.put("pageSize", BOOKING_PER_PAGE);
        response.put("data", bookings);
        response.put("totalPage", totalPage); // Placeholder
        return response;
    }

    public Map<String, Object> adminGetAllBookingByTourID(Integer status,Long tour_id, int currentPage) {
        List<Booking> bookings = bookingDAO.adminGetAllBookingByTourID(status,tour_id, currentPage);
        Integer totalBooking = bookingDAO.TOTALadminGetAllBookingByTourID(status,tour_id);
        int totalPage = (int) Math.ceil((double) totalBooking / BOOKING_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", currentPage);
        response.put("pageSize", BOOKING_PER_PAGE);
        response.put("data", bookings);
        response.put("totalPage", totalPage); // Placeholder
        return response;
    }

    public Map<String, Object> adminGetAllBookingByUserSDTandTourID(Integer status,String sdt,Long tour_id, int currentPage) {
        List<Booking> bookings = bookingDAO.adminGetAllBookingByUserSDTandTourID(status,sdt,tour_id, currentPage);
        Integer totalBooking = bookingDAO.TOTALadminGetAllBookingByUserSDTandTourID(status,sdt,tour_id);
        int totalPage = (int) Math.ceil((double) totalBooking / BOOKING_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", currentPage);
        response.put("pageSize", BOOKING_PER_PAGE);
        response.put("data", bookings);
        response.put("totalPage", totalPage); // Placeholder
        return response;
    }

    public Map<String, Object> adminGetAllBookingOfTourParticular(Long tour_id, LocalDate daytime_start,int currentPage) {
        List<Booking> bookings = bookingDAO.adminGetAllBookingOfTourParticular(tour_id, daytime_start, currentPage);
        Integer totalBooking = bookingDAO.TOTALadminGetAllBookingOfTourParticular(tour_id, daytime_start);
        int totalPage = (int) Math.ceil((double) totalBooking / BOOKING_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", currentPage);
        response.put("pageSize", BOOKING_PER_PAGE);
        response.put("data", bookings);
        response.put("totalPage", totalPage); // Placeholder
        return response;
    }
}