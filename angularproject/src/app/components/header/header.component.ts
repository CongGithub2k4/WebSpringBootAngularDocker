import { CommonModule } from '@angular/common';
import { Component, HostListener, OnInit, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { NoticeService } from '../../services/notice.service';
import { AuthServiceService } from '../../services/auth-service.service';
import { Location } from '@angular/common';

export interface Notice {
  bookingId?: number;
  userId?: number;
  noticeId?: number;
  noticeType?: number; // 0 = đã mua, 1 = đã hủy, 2 = admin hủy
  tourId?: number;
  daytimeStart?: string; // ISO 8601, ex: '2025-05-12'
  status?: number; // 0 = chưa đọc, 1 = đã đọc
}

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, AfterViewInit {
  isMenuOpen = false;
  screenWidth: number = 0;
  isNotificationOpen = false;
  notices: Notice[] = [];
  currentNoticePage = 1;
  totalPages = 1;
  unreadCount = 0;

  constructor(
    private router: Router,
    private authService: AuthServiceService,
    private location: Location,
    private cdr: ChangeDetectorRef,
    private noticeService: NoticeService
  ) { }

  ngOnInit(): void {
    if (typeof window !== 'undefined') {
      this.screenWidth = window.innerWidth;
      // Chỉ tải thông báo nếu đã đăng nhập
      if (this.authService.isLoggedIn()) {
        this.loadNotifications(this.currentNoticePage);
      }
    }
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      if (typeof window !== 'undefined') {
        this.screenWidth = window.innerWidth;
        this.cdr.detectChanges();
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
    if (this.isMenuOpen) {
      this.isNotificationOpen = false; // Đóng dropdown thông báo khi mở menu
    }
  }

  closeMenu(): void {
    this.isMenuOpen = false;
  }

  toggleNotifications(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/loginsignin']);
      return;
    }
    this.isNotificationOpen = !this.isNotificationOpen;
    if (this.isNotificationOpen) {
      this.isMenuOpen = false;
      this.loadNotifications(this.currentNoticePage); // Tải lại thông báo khi mở dropdown
    }
  }

  loadNotifications(page: number): void {
    if (!this.authService.isLoggedIn()) {
      this.notices = [];
      this.unreadCount = 0;
      this.cdr.detectChanges();
      return;
    }
    this.currentNoticePage = page;
    this.noticeService.getUnreadNotices(page).subscribe({
      next: (res: any) => {
        this.notices = res.data || [];
        this.totalPages = res.totalPage || 1;
        this.unreadCount = this.notices.filter(notice => notice.status === 0).length;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Lỗi khi tải thông báo:', err);
        this.notices = [];
        this.unreadCount = 0;
        this.cdr.detectChanges();
      }
    });
  }

  markNoticeAsRead(noticeId: number | undefined): void {
    if (noticeId) {
      this.noticeService.markAsRead(noticeId).subscribe({
        next: () => {
          this.loadNotifications(this.currentNoticePage);
        },
        error: (err) => {
          console.error('Lỗi khi đánh dấu thông báo:', err);
        }
      });
    }
  }

  changeNoticePage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.loadNotifications(page);
    }
  }

  getNoticeMessage(notice: Notice): string {
    switch (notice.noticeType) {
      case 0:
        return `Bạn đã đặt Hóa đơn: ${notice.bookingId}, Mã tour: ${notice.tourId}, ngày khởi hành: ${notice.daytimeStart} thành công`;
      case 1:
        return `Bạn đã hủy Hóa đơn: ${notice.bookingId}, Tour mã: ${notice.tourId}, ngày khởi hành: ${notice.daytimeStart}`;
      case 2:
        return `Hóa đơn: ${notice.bookingId} bạn từng đặt có mã Tour: ${notice.tourId}, ngày khởi hành: ${notice.daytimeStart} đã bị hủy Đột xuất vì TOUR bị hoãn hoặc hủy`;
      default:
        return 'Thông báo không xác định';
    }
  }
  goToDetail(notice: Notice): void {
    console.log('goToDetail called', notice);
    if (notice.noticeType == 0) {
      console.log('goTo reserved called', notice);
      this.router.navigate(['tourdetail/reserved'], {
        queryParams: {
          booking_id: notice.bookingId
        }
      });
    }
    else if (notice.noticeType == 1) {
      console.log('goTo canceled called', notice);
      this.router.navigate(['tourdetail/canceled'], {
        queryParams: {
          booking_id: notice.bookingId
        }
      });
    }
    else if (notice.noticeType == 2) {
      console.log('goto admincanceled called', notice);
      this.router.navigate(['tourdetail/canceled'], {
        queryParams: {
          booking_id: notice.bookingId
        }
      });
    }
    else {
      this.router.navigate(['/notfound']);
    }
  }

  formatDate(date: string | undefined): string {
    if (!date) return '';
    const d = new Date(date);
    return isNaN(d.getTime()) ? '' : d.toLocaleDateString('vi-VN');
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

  goToAccountReserved(): void {
    sessionStorage.setItem('account-page', 'reserved');
    this.router.navigate(['/account']);
  }

  goToAccount(): void {
    this.router.navigate(['/account']);
  }
}