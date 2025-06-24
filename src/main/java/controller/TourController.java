package controller;

import config.ExtractFromSpringSecurityContext;
import dto.NewTourRequest;
import dto.TimeSlotDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.ImageStorageService;
import service.TourService;
import entity.TourParticular;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/tour")
public class TourController {

	private final TourService tourService;
	private final ImageStorageService imageStorageService;

	@Autowired
	public TourController(TourService tourService, ImageStorageService imageStorageService) {
		this.tourService = tourService;
		this.imageStorageService = imageStorageService;
	}

	// http://localhost:8080/tour/start-destinations
	@GetMapping("/start-destinations")
	public ResponseEntity<List<String>> getAllStartDestinations() {
		List<String> startDestinations = tourService.findAllStartDestination();
		return ResponseEntity.ok(startDestinations);
	}

	// http://localhost:8080/tour/destinations
	@GetMapping("/destinations")
	public ResponseEntity<List<String>> getAllDestinations() {
		List<String> destinations = tourService.findAllDestination();
		return ResponseEntity.ok(destinations);
	}

	// http://localhost:8080/tour/1/destinations
	@GetMapping("/{tourId}/destinations")
	public ResponseEntity<List<String>> getDestinationsByTourId(@PathVariable Long tourId) {
		List<String> destinations = tourService.findAllDestinationByTourId(tourId);
		return ResponseEntity.ok(destinations);
	}

	//http://localhost:8080/tour/1/daytimeStart
	@GetMapping("/{tourId}/daytimeStart")
	public ResponseEntity<List<LocalDate>> findAllDaytimeStartOfTourId(@PathVariable Long tourId) {
		try {
			if (!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			List<LocalDate> daytimeStarts= tourService.findAllDaytimeStartOfTourId(tourId);
			return ResponseEntity.ok(daytimeStarts);
		} catch (RuntimeException ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("message", "Bạn chưa đăng nhập với quyền Admin, hoặc lỗi thông tin ");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	// http://localhost:8080/tour/all-tourId
	@GetMapping("/all-tourId")
	public ResponseEntity<List<Long>> findAllTourId() {
		try {
			if (!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			List<Long> tourIds= tourService.findAllTourId();
			return ResponseEntity.ok(tourIds);
		} catch (RuntimeException ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("message", "Bạn chưa đăng nhập với quyền Admin, hoặc lỗi thông tin ");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	// http://localhost:8080/tour/add-new-tour
	/*@PostMapping("/add-new-tour")
	public ResponseEntity<Map<String, Object>> addTour(@RequestBody TourParticular tour) {
		try {
            if (!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
			Long userId = ExtractFromSpringSecurityContext.extractUserIdFromSecurityContext();
            tourService.addNewTour(tour, userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Bạn không có quyền Admin, hoặc chưa đăng nhập với quyền Admin, hoặc lỗi thông tin ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
	}*/

	//http://localhost:8080/tour/add-new-tour
	@PostMapping(value = "/add-new-tour", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createTour(
		@RequestPart("data") NewTourRequest request,
		@RequestPart("linkImage") MultipartFile file
	) {
		try {
			if (!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Bạn không có quyền admin"));
			}
			Long user_id = ExtractFromSpringSecurityContext.extractUserIdFromSecurityContext();
			System.out.println(user_id + "Đây là mã user");
			if(imageStorageService.isValidImageFile(file)) {
				System.out.println("File ảnh hợp leej");
				String imagePath = imageStorageService.store(file);
				System.out.println("Ddã lwuu linka rnh");

				TourParticular tourParticular = new TourParticular();
				tourParticular.setStartDestination(request.getStartDestination());
				tourParticular.setTourName(request.getTourName());
				tourParticular.setDayNumber(request.getDayNumber());
				tourParticular.setNightNumber(request.getNightNumber());

				tourParticular.setThoughoutDestination(request.getThoughoutDestination());

				tourParticular.setDaytimeStart(request.getDaytimeStart());
				tourParticular.setTotalSlot(request.getTotalSlot());
				tourParticular.setSlotRemain(request.getTotalSlot());
				tourParticular.setPrice(request.getPrice());
				tourParticular.setLinkImage(imagePath);
				tourService.addNewTour(tourParticular, user_id);
				System.out.println("\n \n To tour ổn \n \n");
				return ResponseEntity.ok().build();
			}
			else return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} catch (RuntimeException ex) {
			Map<String, String> error = new HashMap<>();
			error.put("message", "Bạn chưa đăng nhập, hoặc lỗi thông tin ");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	// http://localhost:8080/tour/add-new-daytimeStart-for-Tour
	@PostMapping("/add-new-daytimeStart-for-Tour")
	public ResponseEntity<Map<String, Object>> addNewDaytimeStart(@RequestBody TimeSlotDTO timeSlotDTO) {
		try {
			if (!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			LocalDate date = timeSlotDTO.getDaytimeStart();
			List<LocalDate> lst = tourService.findAllDaytimeStartOfTourId(timeSlotDTO.getTourId());
			if(lst.contains(date)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
			Long userId = ExtractFromSpringSecurityContext.extractUserIdFromSecurityContext();
			tourService.addNewDaytimeStart(timeSlotDTO.getTourId(), timeSlotDTO.getDaytimeStart(),
				timeSlotDTO.getSlot(), timeSlotDTO.getPrice(), userId);
			return ResponseEntity.ok().build();
		} catch (RuntimeException ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("message", "Bạn chưa đăng nhập với quyền Admin, hoặc lỗi thông tin ");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}

	// http://localhost:8080/tour/delete-tour/1/2025-05-05
	@DeleteMapping("/delete-tour/{tourId}/{daytimeStart}")
	public ResponseEntity<Map<String, Object>> deleteTourParticular(
		@PathVariable("tourId") Long tourId,
		@PathVariable("daytimeStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate daytimeStart) {
		try {
			if (!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
				Map<String, Object> error = new HashMap<>();
				error.put("message", "Bạn không có quyền Admin.");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
			}

			tourService.deleteTourParticular(tourId, daytimeStart);
			return ResponseEntity.ok().build();

		} catch (RuntimeException ex) {
			ex.printStackTrace(); // để log nếu đang debug
			Map<String, Object> error = new HashMap<>();
			error.put("message", "Lỗi: " + ex.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}

	// admin lọc theo từng tour_id  hoặc khi user xem booking và muốn đặt lại
	// tour thì sẽ nhảy sang trang search và tìm với tour_id và status chỉ =0
	// http://localhost:8080/tour/get-tour-by-id-status?tourId=1&status=0&page=1&type=1
	@GetMapping("/get-tour-by-id-status")
	public ResponseEntity<Map<String, Object>> getTourParticularsByIdAndStatus(
		@RequestParam Long tourId, @RequestParam Integer status,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "1") int type
	) {
		String orderByClause;
		if(type==1) orderByClause= "tp.daytime_start ASC";
		else if(type==2) orderByClause= "tp.daytime_start DESC";
		else if(type==3) orderByClause= "(tp.total_slot - tp.slot_remain) DESC";
		else orderByClause= "(tp.total_slot - tp.slot_remain) ASC";
		if(status > 0) {
			try {
				if (!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
					Map<String, Object> error = new HashMap<>();
					error.put("message", "Bạn không có quyền Admin");
					return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
				}
			} catch (RuntimeException ex) {
				Map<String, Object> error = new HashMap<>();
				error.put("message", "Bạn chưa đăng nhập, hoặc lỗi thông tin ");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
			}
		}
		Map<String, Object> tourParticulars = tourService.findAllTourParticularByIdAndStatus(tourId, status, page, orderByClause);
		List<?> data = (List<?>) tourParticulars.getOrDefault("data", Collections.emptyList());
		if (data.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
				"message", "Không tìm thấy tour phù hợp với điều kiện tìm kiếm"
			));
		}
		return ResponseEntity.ok(tourParticulars);
	}

	// http://localhost:8080/tour/search?startDestination=Hà Nội&thoughoutDestination=Hạ Long&daytimeStart=2025-05-15&status=0&page=1
	@GetMapping("/search")
	public ResponseEntity<Map<String, Object>> searchTours(
		@RequestParam(required = false) String startDestination,
		@RequestParam(required = false) String thoughoutDestination,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate daytimeStart,
		@RequestParam(defaultValue = "0") Integer status,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "1") int type) {


		String orderByClause;
//		= switch (type) {
//			case 1 -> "(tp.total_slot - tp.slot_remain) DESC"; // được đặt nhiều nhất
//			case 2 -> "(tp.total_slot - tp.slot_remain) ASC";  // ít được đặt nhất
//			case 3 -> "tp.daytime_start ASC";                 // sớm nhất
//			case 4 -> "tp.daytime_start DESC";                // muộn nhất
//			default -> "tp.daytime_start ASC";                // mặc định
//		};
		if(type==1) orderByClause= "tp.daytime_start ASC";
		else if(type==2) orderByClause= "tp.daytime_start DESC";
		else if(type==3) orderByClause= "(tp.total_slot - tp.slot_remain) DESC";
		else orderByClause= "(tp.total_slot - tp.slot_remain) ASC";

		System.out.println("Search request: startDestination=" + startDestination +
			", thoughoutDestination=" + thoughoutDestination + ", daytimeStart=" +
			daytimeStart + ", status=" + status + ", page=" + page);
		java.sql.Date daytime_start = null;
		if (daytimeStart != null) {
			daytime_start = java.sql.Date.valueOf(daytimeStart);
			// Đảm bảo ngày không trước ngày hiện tại
			LocalDate today = LocalDate.now();
			if (daytimeStart.isBefore(today)) {
				System.out.println("Warning: daytimeStart is in the past: " + daytimeStart);
				daytime_start = java.sql.Date.valueOf(today);
			}
		}

		Map<String, Object> tourParticulars = new HashMap<>();
		if (startDestination != null && thoughoutDestination != null && daytime_start != null)
			tourParticulars = tourService.findAllTourParticularBy_Start_End_DaytimeStart_Status(startDestination, thoughoutDestination, daytime_start, status, page, orderByClause);

		else if (startDestination != null && thoughoutDestination == null && daytime_start == null)
			tourParticulars = tourService.findAllTourParticularBy_Start_Status(startDestination, status, page, orderByClause);

		else if (startDestination == null && thoughoutDestination != null && daytime_start == null)
			tourParticulars = tourService.findAllTourParticularBy_End_Status(thoughoutDestination, status, page, orderByClause);

		else if (startDestination == null && thoughoutDestination == null && daytime_start != null)
			tourParticulars = tourService.findAllTourParticularBy_DaytimeStart_Status(daytime_start, status, page, orderByClause);

		else if (startDestination != null && thoughoutDestination == null && daytime_start != null)
			tourParticulars = tourService.findAllTourParticularBy_Start_DaytimeStart_Status(startDestination, daytime_start, status, page, orderByClause);

		else if (startDestination == null && thoughoutDestination != null && daytime_start != null)
			tourParticulars = tourService.findAllTourParticularBy_End_DaytimeStart_Status(thoughoutDestination, daytime_start, status, page, orderByClause);

		else if (startDestination != null && thoughoutDestination != null && daytime_start == null)
			tourParticulars = tourService.findAllTourParticularBy_Start_End_Status(startDestination, thoughoutDestination, status, page, orderByClause);
		else
			tourParticulars = tourService.findAllTourByStatus(0,page, orderByClause);

		// Kiểm tra null, thiếu key "data", hoặc danh sách rỗng
		List<?> data = (List<?>) tourParticulars.getOrDefault("data", Collections.emptyList());
		if (data.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
				"message", "Không tìm thấy tour phù hợp với điều kiện tìm kiếm"
			));
		}

		return ResponseEntity.ok(tourParticulars);
	}

	//User ở giao diện chỉ được dùng status =0
	// http://localhost:8080/tour/get-tour-by-status/0?page=1
	@GetMapping("/get-tour-by-status/{status}")
	public ResponseEntity<Map<String, Object>> getToursByStatus(
		@PathVariable Integer status,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "1") int type
	) {
		String orderByClause;
		if(type==1) orderByClause= "tp.daytime_start ASC";
		else if(type==2) orderByClause= "tp.daytime_start DESC";
		else if(type==3) orderByClause= "(tp.total_slot - tp.slot_remain) DESC";
		else orderByClause= "(tp.total_slot - tp.slot_remain) ASC";

		if(status > 0) {
			try {
				if (!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
					Map<String, Object> error = new HashMap<>();
					error.put("message", "Bạn không có quyền Admin");
					return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
				}
			} catch (RuntimeException ex) {
				Map<String, Object> error = new HashMap<>();
				error.put("message", "Bạn chưa đăng nhập, hoặc lỗi thông tin ");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
			}
		}
		Map<String, Object> tours = tourService.findAllTourByStatus(status, page, orderByClause);
		List<?> data = (List<?>) tours.getOrDefault("data", Collections.emptyList());
		if (data.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
				"message", "Không tìm thấy tour phù hợp với điều kiện tìm kiếm"
			));
		}
		return ResponseEntity.ok(tours);
	}

	// http://localhost:8080/tour/top3
	@GetMapping("/top3")
	public ResponseEntity<List<TourParticular>> getTop3Tours() {
		List<TourParticular> top3Tours = tourService.findTop3();
		return ResponseEntity.ok(top3Tours);
	}

	// http://localhost:8080/tour/tour-detail?tour_id=1&daytime_start=2025-05-05
	@GetMapping("/tour-detail")
	public ResponseEntity<Object> getTour
	(@RequestParam Long tour_id,
	 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate daytime_start) {
		Optional<TourParticular> tourOptional = Optional.ofNullable(tourService.getTour(tour_id, daytime_start));

		return tourOptional
			.<ResponseEntity<Object>>map(ResponseEntity::ok)
			.orElseGet(() -> {
				String message = "Không tìm thấy tour với ID: " + tour_id + " và ngày: " + daytime_start;
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
			});
	}

	// http://localhost:8080/tour/image-from-url
	@PostMapping("/image-from-url")
	public ResponseEntity<Map<String, String>> uploadImageFromUrl(@RequestBody Map<String, String> body) {
		try {
			if (!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Bạn không có quyền admin"));
			}
			String imageUrl = body.get("imageUrl");
			if(imageStorageService.isValidImageUrl(imageUrl)) {
				String imagePath = imageStorageService.downloadImageFromUrl(imageUrl);
				return ResponseEntity.ok(Map.of("imagePath", imagePath));
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "URL không hợp lệ"));
		} catch (RuntimeException ex) {
			Map<String, String> error = new HashMap<>();
			error.put("message", "Bạn chưa đăng nhập với quyền Admin, hoặc lỗi thông tin ");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}

	// http://localhost:8080/tour/upload-image
	@PostMapping("/upload-image")
	public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
		try {
			if (!ExtractFromSpringSecurityContext.extractIsAdminFromSecurityContext()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Bạn không có quyền admin"));
			}
			if(imageStorageService.isValidImageFile(file)) {
				String imagePath = imageStorageService.store(file);
				return ResponseEntity.ok(Map.of("imagePath", imagePath));
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Ảnh không hợp lệ"));
		} catch (RuntimeException ex) {
			Map<String, String> error = new HashMap<>();
			error.put("message", "Bạn chưa đăng nhập, hoặc lỗi thông tin ");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}
}
