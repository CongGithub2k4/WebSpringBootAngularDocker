import { Injectable } from '@angular/core';
import { HttpClient, HttpContext, HttpErrorResponse, HttpParams, HttpStatusCode } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { BYPASS_AUTH } from '../http-context.token';

export interface User {
  userId: number;
  userName: string;
  email: string;
  phoneNumber: string;
  userPassword: string;
  userAddress: string;
  isAdmin: number;
}
export interface CreateUserRequest {
  userName: string;
  email: string;
  phoneNumber: string;
  userPassword: string;
}
 
export interface UserDTO {
  userId: number;
  userName: string;
  email: string;
  phoneNumber: string;
  userAddress: string;
  isAdmin: number;
}
export interface AdminUserDTO {
  userId: number;
  userName: string;
  email: string;
  phoneNumber: string;
  userAddress: string;
  isAdmin: number;
  bookingCount: number;
	totalSpent: number;
}

export interface PaginatedResponse<T> {
  data: T[];
  totalPages: number;
  pageSize: number;
  currentPage: number;
}

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'http://localhost:8080/users';

  constructor(private http: HttpClient) {}

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Đã xảy ra lỗi không xác định.';
    if (error.status === HttpStatusCode.NotFound) {
      errorMessage = 'Không tìm thấy User nào thỏa mãn.';
    } else if (error.status === HttpStatusCode.Unauthorized) {
      errorMessage = 'Bạn chưa đăng nhập hoặc thông tin gửi về lỗi.';
    } else if (error.status === HttpStatusCode.Conflict) {
      errorMessage = 'Số điện thoại hoặc Email đã được dùng cho tài khoản khác';
    }
    return throwError(() => new Error(errorMessage));
  }

  createUser(user: CreateUserRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/user-create`, 
      user, 
      { responseType: 'text' ,
        context: new HttpContext().set(BYPASS_AUTH,true)
      }, 
    ).pipe(
      catchError(this.handleError)
    );
  }

  getUserById(id: number): Observable<AdminUserDTO> {
    const params = new HttpParams().set('id', id);
    return this.http.get<AdminUserDTO>(`${this.apiUrl}/admin-get-user-by-id`, { params }).pipe(
      catchError(this.handleError)
    );
  } 

  getUserByPhone(phone: string): Observable<AdminUserDTO> {
    const params = new HttpParams().set('sdt', phone);
    return this.http.get<AdminUserDTO>(`${this.apiUrl}/admin-get-user-by-phone`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  getUserByEmail(email: string): Observable<AdminUserDTO> {
    const params = new HttpParams().set('email', email);
    return this.http.get<AdminUserDTO>(`${this.apiUrl}/admin-get-user-by-email`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  getAllUsers(page: number = 1, type: number = 1): Observable<PaginatedResponse<AdminUserDTO>> {
    const params = new HttpParams()
                  .set('page', page.toString())
                  .set('type', type.toString());
    return this.http.get<any>(`${this.apiUrl}/admin-get-user-all`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  updateUser(user: User): Observable<string> {
    return this.http.put(`${this.apiUrl}/user-update`, user, { responseType: 'text' }).pipe(
      catchError(this.handleError)
    );
  }

  getCurrentUserInfo(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/user-get-info`).pipe(
      catchError(this.handleError)
    );
  }
}