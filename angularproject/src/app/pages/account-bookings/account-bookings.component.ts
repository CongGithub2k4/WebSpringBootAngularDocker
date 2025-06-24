import { Component } from '@angular/core';
import { BookingService } from '../../services/booking.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TourCardReservedComponent } from "../../components/user-booking-reserved/user-booking-reserved.component";
import { TourCardDoneComponent } from "../../components/user-booking-moved/user-booking-moved.component";
import { TourCardCanceledComponent } from "../../components/user-booking-canceled/user-booking-canceled.component";
import { TourCardCanceledByadminComponent } from "../../components/user-booking-canceled-byadmin/user-booking-canceled-byadmin.component";
import { UserService } from '../../services/user.service';
import { privateDecrypt } from 'crypto';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthServiceService } from '../../services/auth-service.service';
import { HeaderComponent } from "../../components/header/header.component";
import { time } from 'console';

export interface User {
  userId: number;
  userName: string;
  email: string;
  phoneNumber: string;
  userPassword: string;
  userAddress: string;
  isAdmin: number;
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
  selector: 'app-account-bookings',
  standalone: true,
  imports: [CommonModule, FormsModule, TourCardReservedComponent, TourCardDoneComponent, TourCardCanceledComponent, TourCardCanceledByadminComponent, HeaderComponent],
  templateUrl: './account-bookings.component.html',
  styleUrl: './account-bookings.component.css'
})
export class AccountBookingsComponent {
  user!: User;

  currentTab: 'account' | 'reserved' | 'moved' | 'canceled' | 'admincanceled' = 'account';

  filteredBookings: Booking[] = [];
  currentPage: number = 1;
  totalPages: number = 1;
  errorMessage: string = '';
  tourService: any;
  tour: any;

  userPassword = '';
  email: string = '';
  phoneNumber: string = '';
  isConflictEmail: boolean = false;
  isConflictPhone: boolean = false;

  successMessage: string = '';
  isLoading: boolean = false;
  showPassword: boolean = false;
  validPassword: boolean = true;

  constructor(
    private bookingService: BookingService,
    private userService: UserService,
    private authService: AuthServiceService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadUser();
    this.setupOpenTab();
  }
  setupOpenTab(): void {
    const storedTab = sessionStorage.getItem('account-page'); // Lấy tab từ sessionStorage
    const validTabs: ('account' | 'reserved' | 'moved' | 'canceled' | 'admincanceled')[] = [
      'account',
      'reserved',
      'moved',
      'canceled',
      'admincanceled'
    ];
    sessionStorage.removeItem('account-page');
    // Kiểm tra giá trị từ sessionStorage
    if (storedTab && validTabs.includes(storedTab as any)) {
      this.switchTab(storedTab as 'account' | 'reserved' | 'moved' | 'canceled' | 'admincanceled');
    } else {
      this.switchTab('account'); // Giá trị mặc định nếu không hợp lệ
    }
  }

  loadUser(): void {
    this.userService.getCurrentUserInfo().subscribe({
      next: (res) => {
        this.user = res;
        this.email = this.user.email;
        this.phoneNumber = this.user.phoneNumber;
        //this.userPassword = this.user.userPassword;
        this.checkConflictEmail(this.email);
        this.checkConflictPhone(this.phoneNumber);
      },
      error: (err) => {
        this.errorMessage = err.message;
      }
    });
  }

  checkConflictEmail(email: string): void {
    email = email.trim();
    this.authService.checkConflictEmail(email).subscribe({
      next: (result: boolean) => {
        this.isConflictEmail = result;// Chỉ báo conflict nếu email mới trùng và khác email gốc
      },
      error: () => {
        this.isConflictEmail = false; // Hoặc xử lý khác tùy ý
      }
    });
  }
  checkConflictPhone(phone: string): void {
    phone = phone.trim();
    this.authService.checkConflictPhone(phone).subscribe({
      next: (result: boolean) => {
        this.isConflictPhone = result;
      },
      error: () => {
        this.isConflictPhone = false;
      }
    });
  }


  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  checkLengthPassword(userPassword: string): void {
    this.userPassword = this.userPassword.trim();
    if (this.userPassword.length < 6) {
      this.validPassword = false;
    } else {
      this.validPassword = true;
    }
  }

  update(): void {
    this.user.email = this.email.trim();
    this.user.phoneNumber = this.phoneNumber.trim();
    this.user.userName = this.user.userName.trim();
    this.user.userAddress = this.user.userAddress.trim();
    this.userPassword = this.userPassword.trim();

    if (this.validPassword) this.user.userPassword = this.userPassword;
    // Basic validation
    if (!this.email || !this.phoneNumber || !this.user.userName) {
      this.errorMessage = 'Vui lòng nhập đầy đủ họ tên, email và số điện thoại.';
      this.successMessage = '';
      return;
    }
    if (!this.validPassword) {
      this.errorMessage = 'Mật khẩu không thỏa mãn >= 6 ký tự';
      this.successMessage = '';
    }

    // Email format validation
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(this.email)) {
      this.errorMessage = 'Email không hợp lệ.';
      this.successMessage = '';
      return;
    }

    // Phone number format validation (basic example, adjust as needed)
    /*const phonePattern = /^[0-9]{10}$/;
    if (!phonePattern.test(this.phoneNumber)) {
      this.errorMessage = 'Số điện thoại không hợp lệ (phải có 10 chữ số).';
      this.successMessage = '';
      return;
    }*/

    // Check for conflicts
    if (this.isConflictEmail || this.isConflictPhone) {
      this.errorMessage = 'Email hoặc số điện thoại đã được sử dụng.';
      this.successMessage = '';
      return;
    }

    this.isLoading = true;

    this.userService.updateUser(this.user).subscribe({
      next: (response: string) => {
        alert('Cập nhật thông tin thành công!');
        this.successMessage = '';
        this.errorMessage = '';
        this.isLoading = false;
        // Sau khi cập nhật dù được hay lỗi vẫn phải load l
        setTimeout(() => {this.loadUser();
        }, 3000);
        
      },
      error: (err) => {
        alert(err.message || 'Không thể cập nhật thông tin.');
        this.successMessage = '';
        this.errorMessage = '';
        this.isLoading = false;
        // Sau khi cập nhật dù được hay lỗi vẫn phải load l
        setTimeout(() => {this.loadUser();
        }, 3000);
        
      }
    });

  }

  logout(): void {
    //sessionStorage.removeItem('user');
    //sessionStorage.removeItem('token');
    sessionStorage.clear();
    this.router.navigate(['/']);
  }

  switchTab(tab: 'account' | 'reserved' | 'moved' | 'canceled' | 'admincanceled') {
    this.currentTab = tab;
    this.currentPage = 1;
    if (tab !== 'account') {
      this.loadBookingsByStatus(this.getStatusForTab(tab));
    }
  }

  goToPage(page: number) {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      if (this.currentTab !== 'account') {
        this.loadBookingsByStatus(this.getStatusForTab(this.currentTab));
      }
    }
  }

  private getStatusForTab(tab: 'reserved' | 'moved' | 'canceled' | 'admincanceled'): number {
    if (tab === 'reserved') return 0;
    if (tab === 'moved') return 1;
    if (tab === 'canceled') return 2;
    return 3;
  }

  private loadBookingsByStatus(status: number) {
    this.bookingService.getUserBookingsByStatus(status, this.currentPage).subscribe({
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
