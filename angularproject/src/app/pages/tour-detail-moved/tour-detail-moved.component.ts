import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BookingService } from '../../services/booking.service';
import { TourService } from '../../services/tour.service';
import { catchError, of } from 'rxjs';
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
  selector: 'app-tour-detail-moved',
  imports: [CommonModule, FormsModule, HeaderComponent],
  templateUrl: './tour-detail-moved.component.html',
  styleUrl: './tour-detail-moved.component.css'
})
export class TourDetailMovedComponent {
  tour ?: Tour;
  booking ?: Booking;
  bookingId !: number;
  errorMessage: string = '';
  loading: boolean = true;
  showConfirmModal = false;
  showAllDestinations: boolean = false; // Mặc định chỉ hiển thị một phần

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
      } else {
        this.errorMessage = 'Lỗi do URL sai';
        this.loading = false;
      }
    });
  }

  fetchBookingAndTour(bookingId: number): void {
    this.loading = true;
    this.bookingService.userGetBookingByBookingId(bookingId).pipe(
      catchError(err => {
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
        if(this.tour.startDestination != dest) {
          queryParams.thoughoutDestination = dest;
          break;
        }
      }
    }
    this.router.navigate(['/tourlist'], { queryParams });
  }
}
