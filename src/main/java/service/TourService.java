package service;

import dao.BookingDAO;
import dao.NoticeDAO;
import dao.TourDAO;
import entity.TourParticular;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class TourService {

    private final TourDAO tourDAO;
    private final BookingDAO bookingDAO;
    private final NoticeDAO noticeDAO;
    private final ImageStorageService imageStorageService;

    @Autowired
    public TourService(TourDAO tourDAO, BookingDAO bookingDAO, NoticeDAO noticeDAO, ImageStorageService imageStorageService) {
        this.tourDAO = tourDAO;
        this.bookingDAO = bookingDAO;
        this.noticeDAO = noticeDAO;
        this.imageStorageService = imageStorageService;
    }

    public List<String> findAllStartDestination() {
        return tourDAO.findAllStartDestination();
    }

    public List<String> findAllDestination() {
        return tourDAO.findAllDestination();
    }

    public List<String> findAllDestinationByTourId(Long tour_id) {
        return tourDAO.findAllDestinationByTourId(tour_id);
    }

    public List<LocalDate> findAllDaytimeStartOfTourId(Long tourId) {
        return tourDAO.findAllDaytimeStartOfTourId(tourId);
    }

    public List<Long> findAllTourId() {
        return tourDAO.findAllTourId();
    }
    @Transactional
    public void addNewTour(TourParticular tour, Long user_id) {
        tourDAO.addNewTour(tour,user_id);
        System.out.println("Tour serrvice ddax ok");
    }
    @Transactional
    public void addNewDaytimeStart(Long tour_id, LocalDate daytime_start, Integer slot, Integer price, Long user_id) {
        tourDAO.addNewDaytimeStart(tour_id,daytime_start,slot,price,user_id);
    }

    @Transactional
    public void deleteTourParticular(Long tour_id, java.time.LocalDate daytime_start) {
        tourDAO.deleteTourParticular(tour_id, daytime_start);
        bookingDAO.updateStatus(tour_id,daytime_start);
        noticeDAO.updateAfterAdminCancelTour(tour_id,daytime_start);
    }

    public Map<String,Object> findAllTourParticularByIdAndStatus(Long tour_id, Integer status, int currentPage, String orderByClause) {
        Integer totalTours = Optional.ofNullable(
                        tourDAO.TOTALfindAllTourParticularByIdAndStatus(tour_id,status))
                .orElse(0);

        List<TourParticular> tours = Optional.ofNullable(
                        tourDAO.findAllTourParticularByIdAndStatus(tour_id, status, currentPage, orderByClause))
                .orElse(Collections.emptyList());

        Integer totalPages = (int) Math.ceil((double) totalTours / TourDAO.MAX_TOUR_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage",currentPage);
        response.put("totalPage",totalPages);
        response.put("pageSize", TourDAO.MAX_TOUR_PER_PAGE);
        response.put("data",tours);
        return response;
    }

    public Map<String, Object> findAllTourParticularBy_Start_End_DaytimeStart_Status(String start_destination, String thoughout_destination, Date daytime_start, Integer status, int currentPage,String orderByClause) {
        //Integer totalTours = tourDAO.TOTALfindAllTourParticularBy_Start_End_DaytimeStart_Status(start_destination,thoughout_destination,daytime_start.toLocalDate(),status);
        //List<TourParticular> tours= tourDAO.findAllTourParticularBy_Start_End_DaytimeStart_Status(start_destination,thoughout_destination,daytime_start.toLocalDate(),status,currentPage);
        Integer totalTours = Optional.ofNullable(
                tourDAO.TOTALfindAllTourParticularBy_Start_End_DaytimeStart_Status(start_destination,thoughout_destination,daytime_start.toLocalDate(),status))
        .orElse(0);

        List<TourParticular> tours = Optional.ofNullable(
                tourDAO.findAllTourParticularBy_Start_End_DaytimeStart_Status(start_destination,thoughout_destination,daytime_start.toLocalDate(),status, currentPage, orderByClause))
        .orElse(Collections.emptyList());

        Integer totalPages = (int) Math.ceil((double) totalTours / TourDAO.MAX_TOUR_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage",currentPage);
        response.put("totalPage",totalPages);
        response.put("pageSize", TourDAO.MAX_TOUR_PER_PAGE);
        response.put("data",tours);
        return response;
    }
    public Map<String, Object> findAllTourParticularBy_Start_Status(String start_destination, Integer status, int currentPage, String orderByClause) {
        //Integer totalTours = tourDAO.TOTALfindAllTourParticularBy_Start_Status(start_destination,status);
        //List<TourParticular> tours= tourDAO.findAllTourParticularBy_Start_Status(start_destination,status,currentPage);
        Integer totalTours = Optional.ofNullable(
                        tourDAO.TOTALfindAllTourParticularBy_Start_Status(start_destination,status))
                .orElse(0);

        List<TourParticular> tours = Optional.ofNullable(
                        tourDAO.findAllTourParticularBy_Start_Status(start_destination,status, currentPage, orderByClause))
                .orElse(Collections.emptyList());

        Integer totalPages = (int) Math.ceil((double) totalTours / TourDAO.MAX_TOUR_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage",currentPage);
        response.put("totalPage",totalPages);
        response.put("pageSize", TourDAO.MAX_TOUR_PER_PAGE);
        response.put("data",tours);
        return response;
    }
    public Map<String, Object> findAllTourParticularBy_End_Status(String thoughout_destination, Integer status, int currentPage, String orderByClause) {
        //Integer totalTours = tourDAO.TOTALfindAllTourParticularBy_End_Status(thoughout_destination,status);
        //List<TourParticular> tours= tourDAO.findAllTourParticularBy_End_Status(thoughout_destination,status,currentPage);
        Integer totalTours = Optional.ofNullable(
                        tourDAO.TOTALfindAllTourParticularBy_End_Status(thoughout_destination,status))
                .orElse(0);

        List<TourParticular> tours = Optional.ofNullable(
                        tourDAO.findAllTourParticularBy_End_Status(thoughout_destination,status, currentPage, orderByClause))
                .orElse(Collections.emptyList());

        Integer totalPages = (int) Math.ceil((double) totalTours / TourDAO.MAX_TOUR_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage",currentPage);
        response.put("totalPage",totalPages);
        response.put("pageSize", TourDAO.MAX_TOUR_PER_PAGE);
        response.put("data",tours);
        return response;
    }
    public Map<String, Object> findAllTourParticularBy_DaytimeStart_Status(Date daytime_start, Integer status, int currentPage, String orderByClause) {
        //Integer totalTours = tourDAO.TOTALfindAllTourParticularBy_DaytimeStart_Status(daytime_start.toLocalDate(),status);
        //List<TourParticular> tours= tourDAO.findAllTourParticularBy_DaytimeStart_Status(daytime_start.toLocalDate(),status,currentPage);
        Integer totalTours = Optional.ofNullable(
                        tourDAO.TOTALfindAllTourParticularBy_DaytimeStart_Status(daytime_start.toLocalDate(),status))
                .orElse(0);

        List<TourParticular> tours = Optional.ofNullable(
                        tourDAO.findAllTourParticularBy_DaytimeStart_Status(daytime_start.toLocalDate(),status, currentPage, orderByClause))
                .orElse(Collections.emptyList());

        Integer totalPages = (int) Math.ceil((double) totalTours / TourDAO.MAX_TOUR_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage",currentPage);
        response.put("totalPage",totalPages);
        response.put("pageSize", TourDAO.MAX_TOUR_PER_PAGE);
        response.put("data",tours);
        return response;
    }
    public Map<String, Object> findAllTourParticularBy_Start_DaytimeStart_Status(String start_destination, Date daytime_start, Integer status, int currentPage, String orderByClause) {
        //Integer totalTours = tourDAO.TOTALfindAllTourParticularBy_Start_DaytimeStart_Status(start_destination,daytime_start.toLocalDate(),status);
        //List<TourParticular> tours= tourDAO.findAllTourParticularBy_Start_DaytimeStart_Status(start_destination,daytime_start.toLocalDate(),status,currentPage);
        Integer totalTours = Optional.ofNullable(
                        tourDAO.TOTALfindAllTourParticularBy_Start_DaytimeStart_Status(start_destination,daytime_start.toLocalDate(),status))
                .orElse(0);

        List<TourParticular> tours = Optional.ofNullable(
                        tourDAO.findAllTourParticularBy_Start_DaytimeStart_Status(start_destination,daytime_start.toLocalDate(),status, currentPage, orderByClause))
                .orElse(Collections.emptyList());

        Integer totalPages = (int) Math.ceil((double) totalTours / TourDAO.MAX_TOUR_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage",currentPage);
        response.put("totalPage",totalPages);
        response.put("pageSize", TourDAO.MAX_TOUR_PER_PAGE);
        response.put("data",tours);
        return response;
    }
    public Map<String, Object> findAllTourParticularBy_End_DaytimeStart_Status(String thoughout_destination, Date daytime_start, Integer status, int currentPage, String orderByClause) {
        //Integer totalTours = tourDAO.TOTALfindAllTourParticularBy_End_DaytimeStart_Status(thoughout_destination,daytime_start.toLocalDate(),status);
        //List<TourParticular> tours= tourDAO.findAllTourParticularBy_End_DaytimeStart_Status(thoughout_destination,daytime_start.toLocalDate(),status,currentPage);
        Integer totalTours = Optional.ofNullable(
                        tourDAO.TOTALfindAllTourParticularBy_End_DaytimeStart_Status(thoughout_destination,daytime_start.toLocalDate(),status))
                .orElse(0);

        List<TourParticular> tours = Optional.ofNullable(
                        tourDAO.findAllTourParticularBy_End_DaytimeStart_Status(thoughout_destination,daytime_start.toLocalDate(),status, currentPage, orderByClause))
                .orElse(Collections.emptyList());

        Integer totalPages = (int) Math.ceil((double) totalTours / TourDAO.MAX_TOUR_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage",currentPage);
        response.put("totalPage",totalPages);
        response.put("pageSize", TourDAO.MAX_TOUR_PER_PAGE);
        response.put("data",tours);
        return response;
    }
    public Map<String, Object> findAllTourParticularBy_Start_End_Status(String start_destination, String thoughout_destination, Integer status, int currentPage, String orderByClause) {
        //Integer totalTours = tourDAO.TOTALfindAllTourParticularBy_Start_End_Status(start_destination,thoughout_destination,status);
        //List<TourParticular> tours= tourDAO.findAllTourParticularBy_Start_End_Status(start_destination,thoughout_destination,status,currentPage);
        Integer totalTours = Optional.ofNullable(
                        tourDAO.TOTALfindAllTourParticularBy_Start_End_Status(start_destination,thoughout_destination,status))
                .orElse(0);

        List<TourParticular> tours = Optional.ofNullable(
                        tourDAO.findAllTourParticularBy_Start_End_Status(start_destination,thoughout_destination, status, currentPage, orderByClause))
                .orElse(Collections.emptyList());

        Integer totalPages = (int) Math.ceil((double) totalTours / TourDAO.MAX_TOUR_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage",currentPage);
        response.put("totalPage",totalPages);
        response.put("pageSize", TourDAO.MAX_TOUR_PER_PAGE);
        response.put("data",tours);
        return response;
    }

    public Map<String, Object> findAllTourByStatus(Integer status, int currentPage, String orderByClause) {
        //Integer totalTours = tourDAO.TOTALfindAllTourByStatus(status);
        //List<TourParticular> tours= tourDAO.findAllTourByStatus(status,currentPage);
        Integer totalTours = Optional.ofNullable(
                        tourDAO.TOTALfindAllTourByStatus(status))
                .orElse(0);

        List<TourParticular> tours = Optional.ofNullable(
                        tourDAO.findAllTourByStatus(status,currentPage, orderByClause))
                .orElse(Collections.emptyList());

        Integer totalPages = (int) Math.ceil((double) totalTours / TourDAO.MAX_TOUR_PER_PAGE);
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage",currentPage);
        response.put("totalPage",totalPages);
        response.put("pageSize", TourDAO.MAX_TOUR_PER_PAGE);
        response.put("data",tours);
        return response;
    }

    public List<TourParticular> findTop3() {
        return tourDAO.findTop3();
    }

    public TourParticular getTour(Long tour_id, LocalDate daytime_start) {
        //return tourDAO.getTour(tour_id,daytime_start);
        Optional<TourParticular> tourParticularOptional = tourDAO.getTour(tour_id, daytime_start);
        return tourParticularOptional.orElse(null);
    }
}
