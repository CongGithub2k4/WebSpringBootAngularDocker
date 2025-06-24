import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthServiceService, LoginRequest } from '../../services/auth-service.service';
import { HeaderComponent } from '../../components/header/header.component';
import { CreateUserRequest, UserService } from '../../services/user.service';
import { LoginRouteLinkService } from '../../services/login-route-link.service';

export interface User {
  userId?: number;
  userName: string;
  email: string;
  phoneNumber: string;
  userPassword?: string;
  userAddress: string;
  isAdmin: number;
}
@Component({
  selector: 'app-loginsignin',
  standalone: true,
  imports: [FormsModule, CommonModule, HeaderComponent],
  templateUrl: './loginsignin.component.html',
  styleUrl: './loginsignin.component.css'
})
export class LoginsigninComponent {
  isLoginMode = true;

  errorMessage = '';

  loginData = { emailOrSDT: '', password: '' };

  registerData = {
    userName: '',
    email: '',
    phoneNumber: '',
    userPassword: '',
    confirmPassword: ''
  };

  constructor(
    private authService: AuthServiceService,
    private router: Router,
    private userService: UserService,
    private loginRouteLink: LoginRouteLinkService
  ) { }

  switchToLogin() {
    this.isLoginMode = true;
  }

  switchToRegister() {
    this.isLoginMode = false;
  }

  onLogin() {
    const loginRequest: LoginRequest = {
      emailOrSDT: this.loginData.emailOrSDT,
      password: this.loginData.password
    };

    this.authService.login(loginRequest).subscribe({
      next: (response) => {
        this.authService.saveAuthData(response);
        this.loginRouteLink.redirectAfterLogin().then(() => {
          // Có thể hiển thị toast, console.log, v.v.
        });
        // Delay 1000ms giúp đảm bảo interceptor đọc được token khi gọi API tiếp theo
        // Lấy thông tin user chỉ dành cho user acc, admin accd đăng nhập thì ko gọi đoạn này
        /*setTimeout(() => {
          this.userService.getCurrentUserInfo().subscribe({
            next: (user) => {
              alert(JSON.stringify(user, null, 2));
              // lưu user vào service, hoặc redirect, v.v.
            },
            error: (err) => {
              this.errorMessage = 'Không lấy được thông tin người dùng: ' + err.message;
            }
          });
        }, 1000);*/
      },
      error: (error) => {
        this.errorMessage = error.message;
        alert(this.errorMessage);
      }
    });
  }

  onRegister() {
    const userData: CreateUserRequest = {
      userName: this.registerData.userName,
      email: this.registerData.email,
      phoneNumber: this.registerData.phoneNumber,
      userPassword: this.registerData.userPassword
    };

    this.userService.createUser(userData).subscribe({
      next: (res) => {
        if (res) {
          alert('Đăng ký thành công!');
          this.switchToLogin(); // Chuyển sang tab login sau khi đăng ký
        }
      },
      error: (err) => {
        // Hiển thị thông báo lỗi
        alert(err.message || 'Đã xảy ra lỗi khi đăng ký. Vui lòng thử lại.');

        // Xóa nội dung registerData
        this.registerData = {
          userName: '',
          email: '',
          phoneNumber: '',
          userPassword: '',
          confirmPassword: ''
        };
      }
    });
  }

  showForgotPasswordModal = false;
  forgotPasswordError = '';
  forgotPasswordData = { emailOrSDT: '' };

  openForgotPasswordModal() {
    this.showForgotPasswordModal = true;
    this.forgotPasswordError = '';
    this.forgotPasswordData.emailOrSDT = '';
  }

  closeForgotPasswordModal() {
    this.showForgotPasswordModal = false;
    this.forgotPasswordError = '';
    this.forgotPasswordData.emailOrSDT = '';
  }
  onForgotPassword() {
    const input = this.forgotPasswordData.emailOrSDT;
    
    // Determine if input is email or phone
    const isEmail = input.includes('@');
    const emailCheck = isEmail ? input : '';
    const phoneCheck = isEmail ? '' : input;

    // Check if email or phone exists
    this.authService.checkEmailOrPhoneExists(emailCheck, phoneCheck).subscribe({
      next: (exists) => {
        if (exists) {
          // If exists, proceed with password reset
          this.authService.forgetPassword(input).subscribe({
            next: (response) => {
              alert(`Mật khẩu của bạn là: ${response}`);
              this.closeForgotPasswordModal();
            },
            error: (err) => {
              this.forgotPasswordError = err.message || 'Đã xảy ra lỗi khi gửi yêu cầu đặt lại mật khẩu.';
            }
          });
        } else {
          this.forgotPasswordError = 'Không tìm thấy tài khoản với email hoặc số điện thoại này.';
        }
      },
      error: (err) => {
        this.forgotPasswordError = err.message || 'Đã xảy ra lỗi khi kiểm tra thông tin.';
      }
    });
  }
}
