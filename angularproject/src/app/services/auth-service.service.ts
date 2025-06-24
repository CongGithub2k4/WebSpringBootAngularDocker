import { Injectable } from '@angular/core';
import { HttpClient, HttpContext, HttpErrorResponse, HttpHeaders, HttpParams, HttpStatusCode } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../environments/environment';
import { BYPASS_AUTH } from '../http-context.token';

export interface LoginRequest {
  emailOrSDT: string;
  password: string;
}
export interface UserDTO {
  userName: string,
  userId: number,
  email: string,
  phoneNumber: string,
  userAddress: string,
  isAdmin: number
}
export interface LoginResponse {
  token: string;
  user: UserDTO;
}
@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) { }

  private handleError(error: HttpErrorResponse) {
    let message = 'Đã xảy ra lỗi không xác định';
    if (error.status === HttpStatusCode.Forbidden) {
      message = 'Bạn không có quyền Admin truy cập.';
    } else if (error.status === HttpStatusCode.NotFound) {
      message = 'Không tìm thấy nội dung phù hợp';
    } else if (error.status === HttpStatusCode.Unauthorized) {
      message = 'Chưa đăng nhập hoặc thông tin đăng nhập sai';
    }
    else {
      message = 'Lỗi kết nối';
    }
    return throwError(() => new Error(message));
  }

  login(data: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, data,
      { context: new HttpContext().set(BYPASS_AUTH, true) }
    ).pipe(
      catchError(this.handleError)
    );
  }
  checkEmailOrPhoneExists(email: string, phone: string): Observable<boolean> {
    const params = new HttpParams()
      .set('email', email)
      .set('phone', phone);
    return this.http.get<boolean>(`${this.apiUrl}/check-exists`,
      {
        params,
        context: new HttpContext().set(BYPASS_AUTH, true)
      }
    ).pipe(
      catchError(this.handleError)
    );
  }

  forgetPassword(emailOrSDT: string): Observable<string> {
    const params = new HttpParams().set('emailOrSDT', emailOrSDT);
    return this.http.get<string>(`${this.apiUrl}/forgetpass`, {
      params,
      context: new HttpContext().set(BYPASS_AUTH, true)
    }).pipe(
      catchError(this.handleError)
    );
  }

  checkConflictEmail(email: string): Observable<boolean> {
    const params = new HttpParams().set('email', email);
    return this.http.get<boolean>(`${this.apiUrl}/checkConflictEmail`, {params}).pipe(
      catchError(this.handleError)
    );
  }

  checkConflictPhone(phone: string): Observable<boolean> {
    const params = new HttpParams().set('phone', phone);
    return this.http.get<boolean>(`${this.apiUrl}/checkConflictPhone`, {params}).pipe(
      catchError(this.handleError)
    );
  }


  // auth-service.service.ts (đổi tên class nên viết là AuthService thay vì AuthServiceService)
  saveAuthData(reponse: LoginResponse): void {
    //sessionStorage.setItem('token','');
    sessionStorage.setItem('token', reponse.token);
    sessionStorage.setItem('user', JSON.stringify(reponse.user));
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  getUser(): UserDTO | null {
    const userStr = sessionStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  }

  clearAuthData(): void {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('user');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    const user = this.getUser();
    return user?.isAdmin === 1;
  }
}