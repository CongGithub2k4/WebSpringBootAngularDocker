import { Component, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookingService } from '../../services/booking.service';
import { TourService } from '../../services/tour.service';
import { catchError, of } from 'rxjs';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "../../components/header/header.component";
export interface Booking {
  bookingId: number,
  userId: number,
  tourId: number,
  daytimeStart: string,
  slotReserved: number,
  totalPurchase: number,
  daytimeCreate: string,
  status: number,     //0 la da dat va chưa đi hoạc đnag đi, 1 la da đi xong, 2 là user hủy, 3 là admin hủy 
  userName: string,
  tourName: string
}
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
  status: number; // 0: mở, 1: đang đi, 2: đã xong, 3: admin huỷ
  adminId: number;
  daytimeCreate: string;
}
@Component({
  selector: 'app-tour-detail-reserved',
  imports: [CommonModule, HeaderComponent],
  templateUrl: './tour-detail-reserved.component.html',
  styleUrl: './tour-detail-reserved.component.css'
})
export class TourDetailReservedComponent {
  tour?: Tour;
  booking?: Booking;
  bookingId !: number;
  errorMessage: string = '';
  loading: boolean = true;
  showConfirmModal = false;
  showAllDestinations: boolean = false; // Mặc định chỉ hiển thị một phần
  intervalId !: NodeJS.Timeout;

  // Hàm để thay đổi trạng thái hiển thị
  toggleShowAllDestinations(): void {
    this.showAllDestinations = !this.showAllDestinations;
  }

  constructor(
    private router: Router,
    private bookingService: BookingService,
    private tourService: TourService,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit() {
    this.activatedRoute.queryParamMap.subscribe(params => {
      const idParam = params.get('booking_id');
      if (idParam) {
        this.bookingId = Number(idParam);
        this.fetchBookingAndTour(this.bookingId);

        // đợi 3s rồi check xem booking nay đã bị hủy chưa. Do nhảy đến trang reserved này bằng notice.
        setTimeout(() => {
          this.moveToCanceled();
        }, 2000);

        // Tự reload sau mỗi 60 giây
        this.intervalId = setInterval(() => {
          this.fetchBookingAndTour(this.bookingId);
        }, 60000); // 60000 ms = 60s
      } else {
        this.errorMessage = 'Lỗi do URL sai';
        this.loading = false;
      }
    });
  }
  ngOnDestroy() {
    // Hủy interval khi component bị destroy
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }


  moveToCanceled(): void {
    // ✅ Kiểm tra trạng thái ở đây SAU khi đã có dữ liệu
    console.log(this.booking?.status)
    if (this.booking?.status === 2 || this.booking?.status === 3) {
      this.router.navigate(['tourdetail/canceled'], {
        queryParams: { booking_id: this.bookingId }
      });
      return;
    }
  }

  fetchBookingAndTour(bookingId: number): void {
    this.loading = true;
    this.bookingService.userGetBookingByBookingId(bookingId).pipe(
      catchError(err => {
        console.error('Lỗi khi gọi API booking:', err); // 👈 thêm log lỗi
        this.errorMessage = err.message || 'Không thể tải thông tin đặt tour.';
        this.loading = false;
        return of(null);
      })
    ).subscribe(booking => {
      if (!booking) return;

      this.booking = booking;

      this.tourService.getTourDetail(booking.tourId, booking.daytimeStart).pipe(
        catchError(err => {
          this.errorMessage = err.message || 'Không thể tải thông tin tour.';
          this.loading = false;
          return of(null);
        })
      ).subscribe(tour => {
        if (!tour) return;

        this.tour = tour;
        this.loading = false;
      });
    });
  }

  gotoSearch() {
    if (!this.booking || !this.tour) return;
    const queryParams: any = {
      status: 0,
      page: 1
    };
    if (this.tour.startDestination)
      queryParams.startDestination = this.tour.startDestination;
    if (this.tour.thoughoutDestination?.length) {
      for (const dest of this.tour.thoughoutDestination) {
        if (this.tour.startDestination != dest) {
          queryParams.thoughoutDestination = dest;
          break;
        }
      }
    }
    this.router.navigate(['/tourlist'], { queryParams });
  }

  gotoCancel() {
    if (!this.booking) return;
    this.showConfirmModal = true;
  }

  cancelBooking(): void {
    if (this.booking == null) return;
    this.bookingService.cancelBooking(this.booking).subscribe({
      next: () => {
        alert('Huỷ đặt tour thành công.');
        // Lưu tag vào account-page. Sau đó nhảy đến /account và trang đó sẽ lấy tag nào cần mở =true
        sessionStorage.setItem('account-page', 'canceled');
        this.router.navigate(['/account']);
      },
      error: (err) => {
        this.errorMessage = err.message || 'Huỷ tour thất bại';
      }
    });
    this.showConfirmModal = false;
  }
  closeCancelModal(): void {
    this.showConfirmModal = false;
  }
}
