import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // ✅ import cái này để dùng *ngFor
import { TourCardInfoComponent } from '../tour-card-info/tour-card-info.component';

export interface Booking {
  bookingId: number,
  userId: number,
  tourId: number,
  daytimeStart: string,
  slotReserved: number,
  totalPurchase: number,
  daytimeCreate: string,
  status: 0,
  userName: string,
  tourName: string
}
export interface Tour {
  tourId: number,
  startDestination: string,
  tourName: string,
  dayNumber: number,
  nightNumber: number,
  linkImage: string,
  adminIdCreateTour: number,
  daytimeCreateTour: string,
  thoughoutDestination: string[],
  daytimeStart: string,
  totalSlot: number,
  slotRemain: number, 
  price: number,
  status: number, // 0: mở, 1: đang đi, 2: đã xong, 3: admin huỷ
  adminId: number,
  daytimeCreate: string
}

export const TOURS: Tour[] = [
  {
    tourId: 1,
    startDestination: 'Hà Nội',
    tourName: 'Vịnh Hạ Long 3N2Đ',
    dayNumber: 3,
    nightNumber: 2,
    linkImage: 'https://example.com/images/halong.jpg',
    adminIdCreateTour: 101,
    daytimeCreateTour: '2024-12-01',
    thoughoutDestination: ['Hạ Long', 'Đảo Titop', 'Hang Sửng Sốt'],
    daytimeStart: '2025-06-10T08:00:00',
    totalSlot: 40,
    slotRemain: 18,
    price: 3500000,
    status: 0,
    adminId: 101,
    daytimeCreate: '2024-12-01',
  }
];

@Component({
  selector: 'app-tour-container',
  standalone: true,
  imports: [CommonModule, TourCardInfoComponent], // ✅ thêm CommonModule ở đây
  templateUrl: './tour-container.component.html',
  styleUrls: ['./tour-container.component.css']
})
export class TourContainerComponent {
  tours: Tour[] = TOURS;
}
