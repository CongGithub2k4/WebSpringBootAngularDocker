import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
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
@Component({
  selector: 'app-tour-card-info',
  imports: [  CommonModule],
  templateUrl: './tour-card-info.component.html',
  styleUrl: './tour-card-info.component.css'
})
export class TourCardInfoComponent {
  @Input() tour: Tour | undefined;
  constructor(
    private router: Router
  ) {}
  goToDetail() {
    this.router.navigate(['/tourdetail/info'], {
      queryParams: {
        tour_id: this.tour?.tourId,
        daytime_start: this.tour?.daytimeStart
      }
    });
  }
}
