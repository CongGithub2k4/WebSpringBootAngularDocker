import { HttpClient, HttpHeaders, HttpParams, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface Tour {
  tourId: number,
  startDestination: string,
  tourName: string,
  dayNumber: number,
  nightNumber: number,
  linkImage: string,
  adminIdCreateTour: number,
  daytimeCreateTour: string,
  thoughoutDestination: string[],
  daytimeStart: string,
  totalSlot: number,
  slotRemain: number,
  price: number,
  status: number, // 0: mở, 1: đang đi, 2: đã xong, 3: admin huỷ
  adminId: number,
  daytimeCreate: string
}
export interface Booking {
  bookingId: number;
  userId: number;
  tourId: number;
  daytimeStart: string;
  slotReserved: number;
  totalPurchase: number;
  daytimeCreate: string;
  status: number;
  userName: string;
  tourName: string;
}
export interface PaginatedResponse<T> {
  data: T[];
  totalPage: number;
  pageSize: number;
  currentPage: number;
}


@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private readonly baseUrl = 'http://localhost:8080/booking';

  constructor(private http: HttpClient) { }

  private handleError(error: any) {
    let errorMessage = 'Đã xảy ra lỗi không xác định.';
    if (error.status === HttpStatusCode.Forbidden) {
      errorMessage = 'Bạn không có quyền Admin.';
    } else if (error.status === HttpStatusCode.Unauthorized) {
      errorMessage = 'Bạn chưa đăng nhập hoặc lỗi thông tin.';
    } else if (error.status === HttpStatusCode.BadRequest) {
      errorMessage = 'Dữ liệu nhập vào không hợp lệ';
    } else {
      errorMessage = 'Lỗi kết nối';
    }
    return throwError(() => new Error(errorMessage));
  }

  // ===== User APIs =====

  getUserBookingsByStatus(status: number, page: number = 1): Observable<PaginatedResponse<Booking>> {
    const params = new HttpParams().set('status', status).set('page', page);
    return this.http.get<PaginatedResponse<Booking>>(`${this.baseUrl}/user/get-all`, { params })
      .pipe(catchError(this.handleError));
  }

  cancelBooking(booking: Booking): Observable<any> {
    return this.http.put(`${this.baseUrl}/user/cancel`, booking)
      .pipe(catchError(this.handleError));
  }

  bookNewTour(tour: Tour, slotReserved: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/user/buy-new/${slotReserved}`, tour)
      .pipe(catchError(this.handleError));
  }
  userGetBookingByBookingId(bookingId: number): Observable<Booking> {
    const params = new HttpParams().set('bookingId', bookingId);
    return this.http.get<Booking>(`${this.baseUrl}/user/get-booking-by-id`, { params })
      .pipe(catchError(this.handleError));
  }

  // ===== Admin APIs =====

  getBookingByBookingId(bookingId: number): Observable<Booking> {
    const params = new HttpParams().set('bookingId', bookingId);
    return this.http.get<Booking>(`${this.baseUrl}/admin/get-booking-by-id`, { params })
      .pipe(catchError(this.handleError));
  }

  getAdminBookings(
    filters: {status: number; phoneNumber?: string; tourId?: number; page?: number;}
  ): Observable<PaginatedResponse<Booking>> {
    let params = new HttpParams()
      .set('status', filters.status)
      .set('page', filters.page?.toString() || '1');
    if (filters.phoneNumber) {
      params = params.set('sdt', filters.phoneNumber);
    }
    if (filters.tourId !== undefined && filters.tourId !== null) {
      params = params.set('tour_id', filters.tourId.toString());
    }
    return this.http
      .get<PaginatedResponse<Booking>>(`${this.baseUrl}/admin/get-booking-by-status-userphone-tourid`, { params })
      .pipe(catchError(this.handleError));
  }

  adminGetAllBookingOfTourParticular(
    tourId: number, daytimeStart: string, page: number = 1
  ): Observable<PaginatedResponse<Booking>> { // Using Map<string, any> to match backend's Map<String, Object>
    let params = new HttpParams()
      .set('tour_id', tourId.toString())
      .set('daytime_start', daytimeStart)
      .set('page', page.toString());
    // Make the GET request to the backend endpoint
    return this.http.get<PaginatedResponse<Booking>>(`${this.baseUrl}/admin/get-booking-of-tour-particular`, { params })
      .pipe(
        // Catch and handle any HTTP errors
        catchError(this.handleError)
      );
  }
}
