import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface Notice {
  bookingId?:number;
  userId?: number;
  noticeId?: number;
  noticeType?: number; // 0 = đã mua, 1 = đã hủy, 2 = admin hủy
  tourId?: number;
  daytimeStart?: string; // ISO 8601, ex: '2025-05-12'
  status?: number; // 0 = chưa đọc, 1 = đã đọc
}

@Injectable({
  providedIn: 'root'
})
export class NoticeService {
  private readonly baseUrl = 'http://localhost:8080/notice';

  constructor(private http: HttpClient) {}

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Đã xảy ra lỗi không xác định.';
    if (error.status === HttpStatusCode.Unauthorized) {
      errorMessage = 'Chưa đăng nhập hoặc lỗi thông tin.';
    } else {
      errorMessage = 'Không thể kết nối';
    }
    return throwError(() => new Error(errorMessage));
  }
  /**
   * Đánh dấu một thông báo là đã đọc.
   */
  markAsRead(noticeId: number): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/mark-as-read/${noticeId}`, {})
      .pipe(catchError(this.handleError));
  }
 
  /**
   * Lấy danh sách thông báo chưa đọc theo trang.
   */
  getUnreadNotices(page: number = 1): Observable<any> {
    const params = new HttpParams().set('page', page);
    return this.http.get(`${this.baseUrl}/unread`, { params })
      .pipe(catchError(this.handleError));
  }
}

