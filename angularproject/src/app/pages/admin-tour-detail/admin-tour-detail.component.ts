import { ChangeDetectorRef, Component } from '@angular/core';
import { TourService } from '../../services/tour.service';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginRouteLinkService } from '../../services/login-route-link.service';
import { AuthServiceService } from '../../services/auth-service.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AdminHeaderComponent } from "../../components/admin-header/admin-header.component";
import { BookingService } from '../../services/booking.service';
import { AdminSideTourCardBillComponent } from "../../components/admin-side-booking/admin-side-booking.component";
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
export interface TimeSlotDTO {
  tourId: number;
  daytimeStart: string; // dạng 'YYYY-MM-DD'
  slot: number;
  price: number;
}
export interface PaginatedResponse<T> {
  data: T[];
  totalPage: number;
  pageSize: number;
  currentPage: number;
}
@Component({
  selector: 'app-admin-tour-detail',
  imports: [FormsModule, CommonModule, AdminHeaderComponent, AdminSideTourCardBillComponent],
  templateUrl: './admin-tour-detail.component.html',
  styleUrl: './admin-tour-detail.component.css'
})
export class AdminTourDetailComponent {
  tour: Tour | undefined;
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
    private tourService: TourService,
    private bookingService: BookingService,
    private route: ActivatedRoute,
    private authService: AuthServiceService,
    private loginRoute: LoginRouteLinkService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadTourFromQueryParams();
    this.filterBookings();
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

  proceedBooking(): void {
    this.showConfirmModal = false;
    // Check if tour is defined before accessing its properties
    if (this.tour) {
      const tourId = this.tour.tourId; // No need for Number() if it's already a number
      const daytime = this.tour.daytimeStart; // No need for String() if it's already a string
      this.tourService.deleteTourParticular(tourId, daytime).subscribe({
        next: (data) => {
          alert('Xóa xong');
          this.router.navigate(['/admin/tourlist']);
        },
        error: (err) => {
          alert('Lỗi khi lấy xóa tour:');
        }
      });
      // You might want to add a success message or navigate elsewhere after deletion
    } else {
      alert('Tour data is not available for booking.');
      this.errorMessage = 'Không thể tiến hành đặt chỗ vì dữ liệu tour không có sẵn.';
    }
  }

  currentPage: number = 1;
  totalPages: number = 1;
  filteredBookings: Booking[] = [];

  goToPage(page: number) {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.filterBookings();
    }
  }

  filterBookings() {
    const tourId = this.tour_id;
    const d = new Date(this.daytime_start);
    const formattedDate = d.toISOString().split('T')[0];

    if (tourId && formattedDate) {
      this.bookingService.adminGetAllBookingOfTourParticular(
        tourId, formattedDate, this.currentPage
      ).subscribe({
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
    else {
      this.errorMessage = 'Thiếu tham số tour_id hoặc daytime_start.';
    }
  }
  formatDate(date: string): string {
    const d = new Date(date);
    const formattedDate = d.toISOString().split('T')[0];
    const today = new Date().toISOString().split('T')[0];
    return formattedDate < today ? today : formattedDate;
  }

  selectedDate: string = '';
  showAddNewday: boolean = false;
  allStartDay: string[] = [];
  newDayerror: string = '';
  totalSlot: number | null = null;
  price: number | null = null;
  //tour_id: number | null = this.loadtour_id;
  //errorMessage: string = '';
  formSubmitted: boolean = false;
  tourIdError: string = '';
  dateError: string = '';
  slotError: string = '';
  priceError: string = '';

  openAddBox() {
    this.showAddNewday = true;
    this.formSubmitted = false;
    this.newDayerror = '';
    this.errorMessage = '';
    this.tourIdError = '';
    this.dateError = '';
    this.slotError = '';
    this.priceError = '';
    this.getAllStartDay();
  }
  getAllStartDay(): void {
    if (this.tour_id) {
      this.tourService.getDaytimeStartOfTourId(this.tour_id).subscribe({
        next: (res) => {
          this.allStartDay = res;
          this.newDayerror = '';
        },
        error: (err) => {
          this.newDayerror = err.message;
        }
      });
    } else {
      this.newDayerror = 'Chưa có mã tour (tour_id)';
    }
  }

  closeAddBox() {
    this.showAddNewday = false;
    this.resetForm();
  }

  resetForm() {
    this.selectedDate = '';
    this.totalSlot = null;
    this.price = null;
    //this.tour_id = null;
    this.newDayerror = '';
    this.errorMessage = '';
    this.tourIdError = '';
    this.dateError = '';
    this.slotError = '';
    this.priceError = '';
    this.formSubmitted = false;
  }

  checkTourId(): void {
    this.tourIdError = this.tour_id ? '' : 'Vui lòng nhập mã tour';
  }

  checkday(): void {
      this.dateError = '';
      if (!this.selectedDate) {
        this.dateError = 'Vui lòng chọn ngày khởi hành';
        return;
      }
      const selected = new Date(this.selectedDate);
      const today = new Date();
      const tomorrow = new Date(today);
      tomorrow.setDate(today.getDate() + 1); // lấy ngày mai
      const selectedDateStr = selected.toISOString().split('T')[0];
      const allStartDayFormatted = this.allStartDay.map(day => new Date(day).toISOString().split('T')[0]);
      if (allStartDayFormatted.includes(selectedDateStr)) {
        this.dateError = 'Ngày khởi hành này bị trùng';
        return;
      }
      if (selected < tomorrow) {
        this.dateError = 'Ngày khởi hành cần bắt đầu từ ngày mai trở đi';
        return;
      }
  }

  checkSlot(): void {
    this.slotError = (!this.totalSlot || this.totalSlot <= 0) ? 'Số chỗ phải lớn hơn 0' : '';
  }

  checkPrice(): void {
    this.priceError = (!this.price || this.price <= 0) ? 'Giá phải lớn hơn 0' : '';
  }

  isFormValid(): boolean {
    if (!this.selectedDate) return false;

    const selected = new Date(this.selectedDate);
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(today.getDate() + 1);

    const selectedDateStr = selected.toISOString().split('T')[0];
    const allStartDayFormatted = this.allStartDay.map(day => new Date(day).toISOString().split('T')[0]);

    return (
      !!this.tour_id &&
      selected >= tomorrow &&
      !allStartDayFormatted.includes(selectedDateStr) &&
      !!this.totalSlot &&
      this.totalSlot > 0 &&
      !!this.price &&
      this.price > 0
    );
  }

  createNewDaytimeStart(): void {
    this.formSubmitted = true;
    this.newDayerror = '';
    this.errorMessage = '';

    if (!this.isFormValid()) {
      return; // Không gửi nếu form không hợp lệ
    }

    const formattedDate = new Date(this.selectedDate!).toISOString().split('T')[0];
    const timeSlot: TimeSlotDTO = {
      tourId: this.tour_id!,
      daytimeStart: formattedDate,
      slot: this.totalSlot!,
      price: this.price!
    };

    this.tourService.addNewDaytimeStart(timeSlot).subscribe({
      next: (res) => {
        this.showAddNewday = false;
        this.resetForm();
        alert('Tạo ngày khởi hành mới thành công');
      },
      error: (err) => {
        this.errorMessage = err.message;
      }
    });
  }
}

