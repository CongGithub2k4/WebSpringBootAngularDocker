package controller;

import config.ExtractFromSpringSecurityContext;
import config.JwtUtil;
import entity.Booking;
import entity.TourParticular;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.BookingService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking")
public class BookingController {

	private final BookingService bookingService;
	private final JwtUtil jwtUtil;

	@Autowired
	public BookingController(BookingService bookingService, JwtUtil jwtUtil) {
		this.bookingService = bookingService;
		this.jwtUtil = jwtUtil;
	}

	// http://localhost:8080/booking/user/cancel
	@PutMapping("/user/cancel")
	public ResponseEntity<Map<String, Object>> cancelBooking(@RequestBody Booking booking) {
		try {
			Long userId =
				ExtractFromSpringSecurityContext.extractUserIdFromSecurityContext();
			if (!userId.equals(booking.getUserId())) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
			if (ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
			bookingService.cancelBooking(booking);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (RuntimeException ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("message", "Bạn chưa đăng nhập hoặc lỗi thông tin");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}
	@GetMapping("/user/get-booking-by-id")
	public ResponseEntity<Booking> userGetBookingByBookingId(@RequestParam("bookingId") Long booking_id) {
		try {
			Long userId = ExtractFromSpringSecurityContext.extractUserIdFromSecurityContext();
			Booking booking = bookingService.adminGetBookingByBookingId(booking_id);
			return ResponseEntity.ok(booking);
		} catch (RuntimeException ex) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	// http://localhost:8080/booking/user/get-all?status=0&page=1
	@GetMapping("/user/get-all")
	public ResponseEntity<Map<String, Object>> userGetUserBookingsByStatus(
		@RequestParam Integer status,
		@RequestParam(defaultValue = "1") int page) {
		try {
			Long userId = ExtractFromSpringSecurityContext.extractUserIdFromSecurityContext();
			if (ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
			Map<String, Object> bookings = bookingService.getAllBookingByUserIDandStatus(userId, status, page);
			return ResponseEntity.ok(bookings);
		} catch (RuntimeException ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("message", "Bạn chưa đăng nhập hoặc lỗi thông tin ");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}

	// http://localhost:8080/booking/user/buy-new/3
	@PostMapping("/user/buy-new/{slotReserved}")
	public ResponseEntity<Map<String, Object>> bookNewTour(
		@PathVariable("slotReserved") Integer slotReserved,
		@RequestBody TourParticular tour) {
		if(slotReserved <=0 || slotReserved > tour.getSlotRemain())
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		try {
			Long userId = ExtractFromSpringSecurityContext.extractUserIdFromSecurityContext();
			if (ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
			bookingService.userBuyNewTour( tour, userId, slotReserved);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (RuntimeException ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("message", "Bạn chưa đăng nhập hoặc lỗi thông tin ");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}

	// http://localhost:8080/booking/admin/get-booking-by-id?bookingId=1
	@GetMapping("/admin/get-booking-by-id")
	public ResponseEntity<Booking> adminGetBookingByBookingId(@RequestParam("bookingId") Long booking_id) {
		try {
			if (!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			Booking booking = bookingService.adminGetBookingByBookingId(booking_id);
			return ResponseEntity.ok(booking);
		} catch (RuntimeException ex) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	// http://localhost:8080/booking/admin/get-booking-by-status-userphone-tourid?status=1&sdt=0977497116&tour_id=1&page=1
	@GetMapping("/admin/get-booking-by-status-userphone-tourid")
	public ResponseEntity<Map<String, Object>> adminGetBookingsByStatusUserSDTTourID(
		@RequestParam Integer status,
		@RequestParam(required = false) String sdt,
		@RequestParam(required = false) Long tour_id,
		@RequestParam(defaultValue = "1") int page
	) {
		try {
			if (!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			Map<String, Object> bookings = new HashMap<>();
			if(sdt==null && tour_id == null)
				bookings = bookingService.adminGetAllBooking(status,page);
			else if(sdt == null)
				bookings = bookingService.adminGetAllBookingByTourID(status,tour_id,page);
			else if(tour_id == null)
				bookings = bookingService.adminGetAllBookingByUserSDT(status,sdt,page);
			else bookings = bookingService.adminGetAllBookingByUserSDTandTourID(status,sdt,tour_id,page);
			return ResponseEntity.ok(bookings);
		} catch (RuntimeException ex) {
			System.out.println(ex.toString());
			Map<String, Object> error = new HashMap<>();
			error.put("message", "Bạn chưa đăng nhập hoặc lỗi thông tin");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}

	@GetMapping("/admin/get-booking-of-tour-particular")
	public ResponseEntity<Map<String, Object>> adminGetAllBookingOfTourParticular(
		@RequestParam Long tour_id,
		@RequestParam LocalDate daytime_start,
		@RequestParam(defaultValue = "1") int page
	) {
		try {
			if (!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			Map<String, Object> bookings = new HashMap<>();
			bookings = bookingService.adminGetAllBookingOfTourParticular(tour_id, daytime_start, page);
			return ResponseEntity.ok(bookings);
		} catch (RuntimeException ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("message", "Bạn chưa đăng nhập hoặc lỗi thông tin");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}
}