import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { BookingService } from '../../services/booking.service';
import { TourService } from '../../services/tour.service';
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
  selector: 'app-tour-card-reserved',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-booking-reserved.component.html',
  styleUrl: './user-booking-reserved.component.css'
})
export class TourCardReservedComponent {
  private _booking!: Booking;

  @Input()
  set booking(value: Booking) {
    this._booking = value;
    this.loadTour(); // Gọi khi booking được gán
  }

  get booking(): Booking {
    return this._booking;
  }

  tour?: Tour;
  errorMessage = '';
  loading = true;

  constructor(
    private tourService: TourService, 
    private router: Router
  ) {}

  private loadTour(): void {
    this.loading = true;
    this.tourService.getTourDetail(this.booking.tourId, this.booking.daytimeStart).subscribe({
      next: (tour: Tour) => {
        this.tour = tour;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Không thể tải tour';
        this.loading = false;
      }
    });
  }
  goToDetail() {
    this.router.navigate(['tourdetail/reserved'], {
      queryParams: {
        booking_id: this._booking.bookingId
      }
    });
  }
}