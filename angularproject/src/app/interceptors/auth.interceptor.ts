// auth.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BYPASS_AUTH } from '../http-context.token';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Nếu context yêu cầu bỏ qua Authorization thì không thêm token
    if (req.context.get(BYPASS_AUTH)) {
      return next.handle(req);
    }
    const token = sessionStorage.getItem('token');
    if (token) {
      const cloned = req.clone({
        headers: req.headers.set('Authorization', 'Bearer ' + token)
      });
      console.log('[AuthInterceptor]', req.url, '-> Token:', token);
      return next.handle(cloned); // ✅ PHẢI return
    }
    return next.handle(req); // ✅ nếu không có token vẫn phải return
  }
}


