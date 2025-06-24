import { Component, OnInit } from '@angular/core';
import { AdminSideUserCardComponent } from "../../components/admin-side-user-card/admin-side-user-card.component";
import { UserService } from '../../services/user.service'; // tùy vị trí thực tế
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AdminHeaderComponent } from "../../components/admin-header/admin-header.component";
export interface AdminUserDTO {
  userId: number;
  userName: string;
  email: string;
  phoneNumber: string;
  userAddress: string;
  isAdmin: number;
  bookingCount: number;
	totalSpent: number;
}
export interface PaginatedResponse<T> {
  data: T[];
  totalPage: number;
  pageSize: number;
  currentPage: number;
}
@Component({
  selector: 'app-admin-userlist',
  standalone: true,
  imports: [AdminSideUserCardComponent, FormsModule, CommonModule, AdminHeaderComponent],
  templateUrl: './admin-userlist.component.html',
  styleUrl: './admin-userlist.component.css'
})
export class AdminUserlistComponent implements OnInit {
  filteredUsers: AdminUserDTO[] = [];
  userId: string = '';
  email: string = '';
  userPhone: string = '';
  errorMessage: string = '';
  totalPages: number = 1;
  currentPage: number = 1;
  selectedType = 1; // type 1-> 6 ứng với chỉ mục trong typeList
  typeList = [
    'Khách hàng mới nhất', 'Khách hàng lâu đời nhất', 'Tổng số hóa đơn đã mua ít nhất',
    'Tổng số hóa đơn đã mua nhiều nhất', 'Tổng số tiền đã chi ít nhất', 'Tổng số tiền đã chi nhiều nhất'
  ];
  onTypeChange() {
    this.email='';
    this.userId='';
    this.userPhone='';
    console.log('Đã chọn loại:', this.selectedType, this.typeList[this.selectedType - 1]);
    this.getAllUsers(1,this.selectedType);
  }


  constructor(private userService: UserService) { }

  ngOnInit() {
    this.getAllUsers();
  }

  onUserIdChange(value: string) {
    this.userId = value;
    this.email = '';
    this.userPhone = '';
    this.currentPage = 1; // Reset về trang 1
    this.totalPages=1;
    this.selectedType = 1;
    if (value.trim()) {
      const id = +value;
      if (!isNaN(id)) {
        this.userService.getUserById(id).subscribe({
          next: (user) => {
            this.filteredUsers = [user];
            this.totalPages = 1;
            this.errorMessage = '';
          },
          error: (err: HttpErrorResponse) => {
            this.filteredUsers = [];
            this.errorMessage = err.error?.message || 'Không tìm thấy người dùng.';
          }
        });
      } else {
        this.filteredUsers = [];
        this.errorMessage = 'ID phải là số.';
      }
    } else {
      this.getAllUsers(this.currentPage, this.selectedType);
    }
  }

  onPhoneChange(value: string) {
    this.userPhone = value;
    this.userId = '';
    this.email = '';
    this.totalPages=1;
    this.currentPage = 1; // Reset về trang 1
    this.selectedType = 1;
    if (value.trim()) {
      this.userService.getUserByPhone(value).subscribe({
        next: (user) => {
          this.filteredUsers = [user];
          this.totalPages = 1;
          this.errorMessage = '';
        },
        error: (err: HttpErrorResponse) => {
          this.filteredUsers = [];
          this.errorMessage = err.error?.message || 'Không tìm thấy người dùng.';
        }
      });
    } else {
      this.getAllUsers(this.currentPage, this.selectedType);
    }
  }

  onEmailChange(value: string) {
    this.email = value;
    this.userId = '';
    this.userPhone = '';
    this.totalPages=1;
    this.currentPage = 1; // Reset về trang 1
    this.selectedType = 1;

    if (value.trim()) {
      this.userService.getUserByEmail(value).subscribe({
        next: (user) => {
          this.filteredUsers = [user];
          this.totalPages = 1;
          this.errorMessage = '';
        },
        error: (err: HttpErrorResponse) => {
          this.filteredUsers = [];
          this.errorMessage = err.error?.message || 'Không tìm thấy người dùng.';
        }
      });
    } else {
      this.getAllUsers(this.currentPage, this.selectedType);
    }
  }


  getAllUsers(page: number = 1, type: number=1) {
    this.currentPage = page;
    this.userService.getAllUsers(page, type).subscribe({
      next: (res) => {
        this.filteredUsers = res.data;
        this.totalPages = res.totalPages;
        this.errorMessage = '';
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage = err.error?.message || 'Lỗi khi tải danh sách người dùng.';
      }
    });
  }

  goToPage(page: number) {
    if (page >= 1 && page <= this.totalPages) {
      this.getAllUsers(page, this.selectedType);
    }
  }
}

