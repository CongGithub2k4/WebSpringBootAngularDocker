import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthServiceService } from '../services/auth-service.service';
import { LoginRouteLinkService } from '../services/login-route-link.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthServiceService);
  const loginRouteLink = inject(LoginRouteLinkService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  } else {
    loginRouteLink.setRedirectUrl(state.url);  // <== lưu đường dẫn user định nhảy đến
    router.navigate(['/loginsignin']);
    return false;
  }
};


