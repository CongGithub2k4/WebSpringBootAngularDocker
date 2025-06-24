import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
export interface Booking {
  bookingId: number,
  userId: number,
  tourId: number,
  daytimeStart: string,
  slotReserved: number,
  totalPurchase: number,
  daytimeCreate: string,
  status: number,
  userName: string,
  tourName: string
}
@Component({
  selector: 'app-admin-side-tour-card-bill',
  imports: [CommonModule],
  templateUrl: './admin-side-booking.component.html',
  styleUrl: './admin-side-booking.component.css'
})
export class AdminSideTourCardBillComponent {
  @Input() booking ?: Booking;
}
