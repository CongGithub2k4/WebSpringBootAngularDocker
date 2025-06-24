import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TourCardInfoComponent } from "../../components/tour-card-info/tour-card-info.component";
import { HeaderComponent } from "../../components/header/header.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { TourService } from '../../services/tour.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

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

@Component({
  selector: 'app-tourlist',
  standalone: true,
  imports: [CommonModule, TourCardInfoComponent, FormsModule, HeaderComponent, FooterComponent],
  templateUrl: './tourlist.component.html',
  styleUrls: ['./tourlist.component.css']
})
export class TourListComponent implements OnInit, OnDestroy {
  tours: Tour[] = [];
  selectedDestination: string = '';
  selectedDeparture: string = '';
  selectedDate: string = '';
  selectedStatus: number = 0;
  currentPage: number = 1;
  totalPages: number = 1;

  errorMessage: string = '';
  destinations: string[] = [];
  departures: string[] = [];

  private filterSubject = new Subject<void>();
  private queryParamsSubscription?: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private tourService: TourService
  ) { }

  ngOnInit(): void {
    this.loadFilterOptions();

    this.filterSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(() => {
      this.loadTours(this.currentPage, this.selectedType);
    });

    // this.queryParamsSubscription = this.route.queryParams.subscribe(params => {
    //   this.selectedDeparture = params['startDestination'] || '';
    //   this.selectedDestination = params['thoughoutDestination'] || '';
    //   this.selectedDate = params['daytimeStart'] || '';
    //   this.selectedStatus = Number(params['status'] || 0);
    //   this.currentPage = Number(params['page'] || 1);

    //   this.loadTours(this.currentPage, this.selectedType);
    // });
    this.queryParamsSubscription = this.route.queryParams.subscribe(params => {
      const hasQueryParams = Object.keys(params).length > 0;

      this.selectedDeparture = params['startDestination'] || '';
      this.selectedDestination = params['thoughoutDestination'] || '';
      this.selectedDate = params['daytimeStart'] || '';
      this.selectedStatus = Number(params['status'] ?? 0);
      this.currentPage = Number(params['page'] ?? 1);
      this.selectedType = Number(params['type'] ?? 1);

      //if (hasQueryParams) {
      this.loadTours(this.currentPage, this.selectedType);
      // } else {
      //   // Khi mới đến trang này, filter mặc định
      //   this.filterTours(); // sẽ tự đưa về page=1 và selectedType đang giữ nguyên
      // }
    });

  }

  ngOnDestroy(): void {
    this.queryParamsSubscription?.unsubscribe();
  }

  loadFilterOptions(): void {
    this.tourService.getStartDestinations().subscribe({
      next: data => this.departures = data,
      error: err => console.error('Lỗi lấy điểm khởi hành:', err.message)
    });

    this.tourService.getDestinations().subscribe({
      next: data => this.destinations = data,
      error: err => console.error('Lỗi lấy điểm đến:', err.message)
    });
  }

  formatDate(date: string | undefined): string | undefined {
    if (!date) return undefined;
    const d = new Date(date);
    if (isNaN(d.getTime())) return undefined;

    const formattedDate = d.toISOString().split('T')[0];
    const today = new Date().toISOString().split('T')[0];
    return formattedDate < today ? today : formattedDate;
  }

  selectedType = 1; // type 1-> 6 ứng với chỉ mục trong typeList
  typeList = [
    'Ngày khởi hành sớm nhất', 'Ngày khởi hành muộn nhất', 'Tour đang được đặt vé nhiều nhất',
    'Tour đang được đặt vé ít nhất'];
  onTypeChange() {
    console.log('Đã chọn loại:', this.selectedType, this.typeList[this.selectedType - 1]);
    this.filterTours(); // tự xủ lý query params
  }

  loadTours(page: number = 1, type: number = 1): void {
    this.errorMessage = '';
    this.currentPage = page;
    this.selectedType = type;

    const filters = {
      type: this.selectedType,
      startDestination: this.selectedDeparture || undefined,
      thoughoutDestination: this.selectedDestination || undefined,
      daytimeStart: this.formatDate(this.selectedDate),
      status: this.selectedStatus,
      page: this.currentPage
    };

    this.tourService.searchTours(filters).subscribe({
      next: res => {
        this.tours = res.data || [];
        this.totalPages = res.totalPage || 1;
        if (this.tours.length === 0) {
          this.errorMessage = 'Không tìm thấy tour phù hợp.';
        }
      },
      error: err => {
        this.tours = [];
        this.errorMessage = err.message;
      }
    });
  }

  filterTours(): void {
    // Khi có thay đổi filter, reset page về 1 nhưng giữ nguyên selectedType hiện tại
    this.currentPage = 1;

    const queryParams: any = {
      type: this.selectedType,
      startDestination: this.selectedDeparture || undefined,
      thoughoutDestination: this.selectedDestination || undefined,
      daytimeStart: this.formatDate(this.selectedDate),
      status: this.selectedStatus,
      page: this.currentPage
    };

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
      queryParamsHandling: 'merge',
    });
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.router.navigate([], {
        relativeTo: this.route,
        queryParams: { page: page, type: this.selectedType },
        queryParamsHandling: 'merge'
      });
    }
  }
}
