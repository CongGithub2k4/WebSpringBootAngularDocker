import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthServiceService } from './auth-service.service';

@Injectable({
  providedIn: 'root'
})
export class LoginRouteLinkService {

  constructor(private router: Router, private authService: AuthServiceService) { }

  setRedirectUrl(url: string) {
    // Link trang web tiếp theo sẽ nhảy đến sau khi đăng nhập xong. Dùng vs LoginGuard.ts
    sessionStorage.setItem('lastUrl', url);
  }

  saveLastUrlIfNeeded(): void {
    const currentUrl = this.router.url; 
    //Link trang web ở hiện tại, ví dụ như cần mua hàng thì đăng nhập sau đó tải lại chính trang này.
    if (!currentUrl.includes('/loginsignin')) {
      sessionStorage.setItem('lastUrl', currentUrl);
    }
  }

  redirectAfterLogin(): Promise<boolean> {
    const lastUrl = sessionStorage.getItem('lastUrl');
    if (this.authService.isAdmin()) {
      return this.router.navigate(['admin/booking']);
    } else {
      if (lastUrl) {
        sessionStorage.removeItem('lastUrl');
        return this.router.navigateByUrl(lastUrl);
      } else {
        return this.router.navigateByUrl('/');
      }
    }
  }
}
