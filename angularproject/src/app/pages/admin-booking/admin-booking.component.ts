import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BookingService } from '../../services/booking.service';
import { AdminSideTourCardBillComponent } from "../../components/admin-side-booking/admin-side-booking.component";
import { TourService } from '../../services/tour.service';
import { HeaderComponent } from "../../components/header/header.component";
import { AdminHeaderComponent } from "../../components/admin-header/admin-header.component";

export interface Booking {
  bookingId: number;
  userId: number;
  tourId: number;
  daytimeStart: string;
  slotReserved: number;
  totalPurchase: number;
  daytimeCreate: string;
  status: number;
  userName: string;
  tourName: string;
}
export interface PaginatedResponse<T> {
  data: T[];
  totalPage: number;
  pageSize: number;
  currentPage: number;
}
@Component({
  selector: 'app-admin-booking',
  standalone: true,
  imports: [CommonModule, FormsModule, AdminSideTourCardBillComponent, HeaderComponent, AdminHeaderComponent],
  templateUrl: './admin-booking.component.html',
  styleUrl: './admin-booking.component.css'
})
export class AdminBookingComponent {
  filteredBookings: Booking[] = [];

  phoneNumber: string = '';
  selectedTourId: string = '';
  tourIds: number[] = [];

  currentPage: number = 1;
  totalPages: number = 1;

  errorMessage: string = '';

  currentTab: 'reserved' | 'moved' | 'canceled' | 'admincanceled' = 'reserved';

  constructor(private bookingService: BookingService, private tourService: TourService) { }

  ngOnInit(): void {
    this.filterBookings(); // Load dữ liệu mặc định theo tab đầu tiên
    this.extractTourIds();
  }

  switchTab(tab: 'reserved' | 'moved' | 'canceled' | 'admincanceled') {
    this.currentTab = tab;
    this.currentPage = 1;
    this.phoneNumber = '';
    this.selectedTourId = '';
    this.filterBookings();
  }

  getStatusByTab(): number {
    switch (this.currentTab) {
      case 'reserved': return 0;
      case 'moved': return 1;
      case 'canceled': return 2;
      case 'admincanceled': return 3;
      default: return 0;
    }
  }

  goToPage(page: number) {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.filterBookings();
    }
  }

  extractTourIds() {
    this.tourService.getAllTourId().subscribe({
      next: (res) => {
        this.tourIds = res;
      },
      error: (err) => {
        this.errorMessage = err.message;
      }
    });
  }
  // Chọn mã tour trong tab hiện tại
  onTourIdChange(): void {
    this.selectedTourId=this.selectedTourId.trim();
    this.currentPage = 1;
    this.filterBookings();
  }
  filterBookings() {
    const status = this.getStatusByTab();
    const tourId = this.selectedTourId ? +this.selectedTourId : undefined;
    const phone = this.phoneNumber || undefined;

    this.bookingService.getAdminBookings({
      status: status,
      phoneNumber: phone,
      tourId: tourId,
      page: this.currentPage
    }).subscribe({
      next: (res) => {
        this.filteredBookings = res.data;
        this.totalPages = res.totalPage;
        this.errorMessage = '';
      },
      error: (err) => {
        this.errorMessage = err.message;
      }
    });
  }
}
