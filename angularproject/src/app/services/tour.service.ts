import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { HttpClient, HttpContext, HttpContextToken, HttpErrorResponse, HttpHeaders, HttpParams, HttpStatusCode } from '@angular/common/http';
import { BYPASS_AUTH } from '../http-context.token';
import { AuthServiceService } from './auth-service.service';

export interface TimeSlotDTO {
  tourId: number;
  daytimeStart: string; // dạng 'YYYY-MM-DD'
  slot: number;
  price: number;
}

export interface User {
  userName: string,
  userId: number,
  email: string,
  phoneNumber: string,
  userAddress: string,
  userPassword: string,
  isAdmin: number
}
export interface userDTO {
  userName: string,
  userId: number,
  email: string,
  phoneNumber: string,
  userAddress: string,
  isAdmin: number
}

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
export interface NewTourRequest {
  startDestination: string,
  tourName: string,
  dayNumber: number,
  nightNumber: number,
  thoughoutDestination: string[],
  daytimeStart: string,
  totalSlot: number,
  price: number
}
export interface PaginatedResponse<T> {
  data: T[];
  totalPage: number;
  pageSize: number;
  currentPage: number;
}

@Injectable({ providedIn: 'root' })
export class TourService {
  private apiUrl = 'http://localhost:8080/tour'; // URL của Spring Boot API

  constructor(
    private http: HttpClient,
    private authService: AuthServiceService
  ) { }

  // ======== 9. Xử lý lỗi chung ========
  private handleError(error: HttpErrorResponse) {
    let message = 'Đã xảy ra lỗi không xác định';
    if (error.status === HttpStatusCode.Forbidden) {
      message = 'Bạn không có quyền Admin truy cập.';
    } else if (error.status === HttpStatusCode.NotFound) {
      message = 'Không tìm thấy Tour phù hợp';
    } else if (error.status === HttpStatusCode.Unauthorized) {
      message = 'Chưa đăng nhập hoặc lỗi thông tin';
    } else if (error.status === HttpStatusCode.Conflict) {
      message = 'Xung đột dữ liệu khi thêm thông tin';
    }
    else {
      message = 'Lỗi kết nối';
    }
    return throwError(() => new Error(message));
  }

  // ======== 1. Lấy điểm đi / điểm đến ========
  getStartDestinations(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/start-destinations`, {
      context: new HttpContext().set(BYPASS_AUTH, true)
    });
  }

  getDestinations(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/destinations`, {
      context: new HttpContext().set(BYPASS_AUTH, true)
    });
  }

  getDestinationsByTourId(tourId: number): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/${tourId}/destinations`, {
      context: new HttpContext().set(BYPASS_AUTH, true)
    });
  }

  // hàm admin xem tour chi tiết thì xem các ngày tour sắp khởi hành.
  getDaytimeStartOfTourId(tourId: number): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/${tourId}/daytimeStart`).pipe(
      catchError(this.handleError)
    );
  }

  //hàm admin lấy danh sách các tourId
  getAllTourId(): Observable<number[]> {
    return this.http.get<number[]>(`${this.apiUrl}/all-tourId`).pipe(
      catchError(this.handleError)
    );
  }

  // ======== 2. Thêm tour, thêm lịch khởi hành ========
  addNewTour(tour: NewTourRequest, selectedFile: File): Observable<any> {
    const formData = new FormData();
    formData.append('data', new Blob([JSON.stringify(tour)], { type: 'application/json' }));
    formData.append('linkImage', selectedFile);
    return this.http.post<any>(`${this.apiUrl}/add-new-tour`, formData).pipe(
      catchError(this.handleError)
    );
  }


  addNewDaytimeStart(timeSlot: TimeSlotDTO): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/add-new-daytimeStart-for-Tour`, timeSlot).pipe(
      catchError(this.handleError)
    );
  }

  // ======== 3. Xóa một lịch khởi hành của tour ========
  deleteTourParticular(tourId: number, daytimeStart: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete-tour/${tourId}/${daytimeStart}`).pipe(
      catchError(this.handleError)
    );
  }

  // ======== 4. Lấy danh sách tour theo điều kiện ========
  getToursByStatus(status: number, page: number = 1, type: number=1): Observable<PaginatedResponse<Tour>> {
    return this.http.get<any>(`${this.apiUrl}/get-tour-by-status/${status}`,
      { params: { page: page, type: type } }
    ).pipe(catchError(this.handleError));
  }

  getTourByIdAndStatus(tourId: number, status: number, page: number = 1, type: number=1): Observable<PaginatedResponse<Tour>> {
    return this.http.get<any>(`${this.apiUrl}/get-tour-by-id-status`, {
      params: new HttpParams()
        .set('tourId', tourId)
        .set('status', status)
        .set('page', page)
        .set('type', type)
    }).pipe(catchError(this.handleError));
  }

  searchTours(filters: {
    type: number;
    startDestination?: string;
    thoughoutDestination?: string;
    daytimeStart?: string;
    status: number;
    page?: number;
  }): Observable<PaginatedResponse<Tour>> {
    let params = new HttpParams();

    Object.entries(filters).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        params = params.set(key, value.toString());
      }
    });

    return this.http.get<any>(`${this.apiUrl}/search`, {
      params,
      context: new HttpContext().set(BYPASS_AUTH, true)
    }).pipe(
      catchError(this.handleError)
    );
  }


  // ======== 5. Lấy top 3 tour ========
  getTop3Tours(): Observable<Tour[]> {
    return this.http.get<Tour[]>(`${this.apiUrl}/top3`, {
      context: new HttpContext().set(BYPASS_AUTH, true)
    });
  }

  // ======== 6. Lấy chi tiết tour cụ thể ========
  getTourDetail(tourId: number, daytimeStart: string): Observable<Tour> {
    return this.http.get<Tour>(`${this.apiUrl}/tour-detail`, {
      params: new HttpParams()
        .set('tour_id', tourId)
        .set('daytime_start', daytimeStart),
      context: new HttpContext().set(BYPASS_AUTH, true)
    }).pipe(catchError(this.handleError));
  }

  // ======== 7. Gửi ảnh từ URL ========
  uploadImageFromUrl(imageUrl: string): Observable<{ imagePath: string }> {
    return this.http.post<{ imagePath: string }>(`${this.apiUrl}/image-from-url`, { imageUrl }).pipe(
      catchError(this.handleError)
    );
  }

  // ======== 8. Upload ảnh từ máy ========
  uploadImageFile(file: File): Observable<{ imagePath: string }> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{ imagePath: string }>(`${this.apiUrl}/upload-image`, formData).pipe(
      catchError(this.handleError)
    );
  }
}