import { CommonModule, DatePipe } from '@angular/common';
import { Component, Input } from '@angular/core';

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
@Component({
  selector: 'app-admin-side-user-card',
  imports: [CommonModule],
  templateUrl: './admin-side-user-card.component.html',
  styleUrl: './admin-side-user-card.component.css'
})
export class AdminSideUserCardComponent {
  @Input() user: AdminUserDTO | undefined;
}
