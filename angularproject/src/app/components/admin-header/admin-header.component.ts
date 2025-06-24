import { ChangeDetectorRef, Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginRouteLinkService } from '../../services/login-route-link.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-admin-header',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-header.component.html',
  styleUrl: './admin-header.component.css'
})
export class AdminHeaderComponent {
  isMenuOpen = false;
  screenWidth: number = 0;

  constructor(
    private router: Router,
    private loginRouteLink: LoginRouteLinkService,
    private location: Location,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    if (typeof window !== 'undefined') {
      this.screenWidth = window.innerWidth;
    }
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      if (typeof window !== 'undefined') {
        this.screenWidth = window.innerWidth;
        this.cdr.detectChanges(); // cập nhật lại giao diện
      }
    });
  }

  @HostListener('window:resize')
  onResize(): void {
    if (typeof window !== 'undefined') {
      this.screenWidth = window.innerWidth;
      if (this.screenWidth > 800 && this.isMenuOpen) {
        this.isMenuOpen = false;
      }
    }
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  closeMenu(): void {
    this.isMenuOpen = false;
  }

  scrollToSection(fragment: string): void {
    this.closeMenu();
    const currentPath = this.location.path().split('#')[0];

    if (currentPath === '/' || currentPath === '') {
      this.router.navigate([], { fragment, skipLocationChange: true });
    } else {
      this.router.navigate(['/'], { fragment });
    }
  }

  goToBooking(): void {
    this.router.navigate(['admin/booking']);
  }
  goToTourlist(): void {
    this.router.navigate(['admin/tourlist']);
  }
  goToUserList(): void {
    this.router.navigate(['admin/users']);
  }

  isAccountMenuOpen = false;

  toggleAccountMenu() {
    this.isAccountMenuOpen = !this.isAccountMenuOpen;
  }

  logout() {
    sessionStorage.clear();
    this.router.navigate(['/']); // điều hướng về trang đăng nhập
  }

}
