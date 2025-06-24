import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BookingService } from '../../services/booking.service';
import { TourService } from '../../services/tour.service';
import { AuthServiceService } from '../../services/auth-service.service';
import { LoginRouteLinkService } from '../../services/login-route-link.service';
import { HeaderComponent } from "../../components/header/header.component";

export interface Tour {
  tourId: number;
  startDestination: string;
  tourName: string;
  dayNumber: number;
  nightNumber: number;
  linkImage: string;
  adminIdCreateTour: number;
  daytimeCreateTour: string;
  thoughoutDestination: string[];
  daytimeStart: string;
  totalSlot: number;
  slotRemain: number;
  price: number;
  status: number;
  adminId: number;
  daytimeCreate: string;
}

@Component({
  selector: 'app-tour-detail-info',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent],
  templateUrl: './tour-detail-info.component.html',
  styleUrls: ['./tour-detail-info.component.css']
})
export class TourDetailInfoComponent implements OnInit {
  tour?: Tour;
  tour_id = 0;
  daytime_start = '';
  daytimeStartDate?: Date;

  userName = '';
  userId = '';

  seatCount = 1;
  remainingSeats = 0;

  showConfirmModal = false;
  showPaymentModal = false;
  errorMessage = '';
  loading = true;
  showAllDestinations = false;

  constructor(
    private bookingService: BookingService,
    private tourService: TourService,
    private route: ActivatedRoute,
    private authService: AuthServiceService,
    private loginRoute: LoginRouteLinkService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadUserFromSession();
    this.loadTourFromQueryParams();
  }

  loadUserFromSession(): void {
    const userData = sessionStorage.getItem('user');
    if (userData) {
      try {
        const parsed = JSON.parse(userData);
        this.userName = parsed.userName;
        this.userId = parsed.userId;
      } catch (e) {
        console.warn('Lỗi khi đọc user từ sessionStorage:', e);
      }
    }
  }

  loadTourFromQueryParams(): void {
    this.route.queryParamMap.subscribe(params => {
      const idParam = params.get('tour_id');
      const startParam = params.get('daytime_start');

      if (idParam && startParam) {
        this.tour_id = +idParam;
        this.daytime_start = startParam;
        this.daytimeStartDate = new Date(this.daytime_start); // <-- chuyển về kiểu Date
        this.fetchTourDetail();
      } else {
        this.errorMessage = 'Thiếu tham số tour_id hoặc daytime_start.';
        this.loading = false;
      }
    });
  }

  fetchTourDetail(): void {
    this.tourService.getTourDetail(this.tour_id, this.daytime_start).subscribe({
      next: (data) => {
        this.tour = data;
        this.remainingSeats = data.slotRemain;
        this.loading = false;
      },
      error: (err) => {
        console.error('Lỗi khi lấy dữ liệu tour:', err);
        this.errorMessage = 'Không thể tải chi tiết tour.';
        this.loading = false;
      }
    });
  }

  toggleShowAllDestinations(): void {
    this.showAllDestinations = !this.showAllDestinations;
  }

  openConfirmModal(): void {
    if (this.remainingSeats <= 0) {
      alert('Không còn chỗ trống!');
      return;
    }

    if (this.authService.isLoggedIn()) {
      this.seatCount = 1;
      this.showConfirmModal = true;
      this.cdr.detectChanges();
    } else {
      this.loginRoute.saveLastUrlIfNeeded();
      this.router.navigate(['/loginsignin']);
    }
  }

  closeConfirmModal(): void {
    this.showConfirmModal = false;
    this.cdr.detectChanges();
  }

  validateSeats(): boolean {
    return this.seatCount > 0 && this.seatCount <= this.remainingSeats;
  }

  proceedBooking(): void {
    if (!this.validateSeats()) {
      alert('Số lượng chỗ không hợp lệ!');
      return;
    }
    this.showConfirmModal = false;
    this.showPaymentModal = true;
    this.cdr.detectChanges();
  }

  confirmPayment(): void {
    if (!this.tour) {
      alert('Không có thông tin tour!');
      return;
    }

    this.bookingService.bookNewTour(this.tour, this.seatCount).subscribe({
      next: () => {
        alert('Đặt tour thành công!');
        this.showPaymentModal = false;
        sessionStorage.setItem('account-page', 'reserved');
        this.router.navigate(['/account']);
      },
      error: (err) => {
        console.error('Đặt tour thất bại:', err);
        alert('Lỗi khi đặt tour, vui lòng thử lại!');
      }
    });
  }

  closeConfirmPayment(): void {
    this.showPaymentModal = false;
    this.cdr.detectChanges();
  }
}
