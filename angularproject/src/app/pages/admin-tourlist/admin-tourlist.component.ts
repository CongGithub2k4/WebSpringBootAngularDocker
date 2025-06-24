import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AdminSideTourCardInfoComponent } from "../../components/admin-side-tour-card-info/admin-side-tour-card-info.component";
import { debounceTime, distinctUntilChanged, Subject, Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { TourService } from '../../services/tour.service';
import { AdminHeaderComponent } from "../../components/admin-header/admin-header.component";

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
export interface PaginatedResponse<T> {
  data: T[];
  totalPage: number;
  pageSize: number;
  currentPage: number;
}

@Component({
  selector: 'app-admin-tourlist',
  imports: [FormsModule, CommonModule, AdminSideTourCardInfoComponent, AdminHeaderComponent],
  templateUrl: './admin-tourlist.component.html',
  styleUrl: './admin-tourlist.component.css'
})
export class AdminTourlistComponent {
  currentTab: 'opening' | 'moving' | 'moved' | 'admincanceled' = 'opening';
  selectedTourId: string = '';
  tourIds: number[] = [];
  tours: Tour[] = [];
  //selectedDestination: string = '';
  //selectedDeparture: string = '';
  //selectedDate: string = '';
  selectedStatus: number = 0;
  currentPage: number = 1;
  totalPages: number = 1;

  errorMessage: string = '';
  //destinations: string[] = [];
  //departures: string[] = [];

  private filterSubject = new Subject<void>();
  private queryParamsSubscription?: Subscription;

  selectedType = 1; // type 1-> 6 ứng với chỉ mục trong typeList
  typeList = [
    'Ngày khởi hành sớm nhất', 'Ngày khởi hành muộn nhất', 'Tour đang được đặt vé nhiều nhất',
    'Tour đang được đặt vé ít nhất'];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private tourService: TourService
  ) { }

  goTonewTour() {
    this.router.navigate(['/admin/newtour']);
  }
  ngOnInit(): void {
    this.queryParamsSubscription = this.route.queryParams.subscribe(params => {
      // Nếu không có param nào → gán mặc định
      const page = Number(params['page'] || 1);
      const tourId = params['tourId'] || '';
      const status = params['status'] !== undefined ? +params['status'] : 0;
      const type = params['type'] !== undefined ? +params['type'] : 1;

      this.currentPage = page;
      this.selectedTourId = tourId;
      this.selectedStatus = status;
      this.selectedType = type; // ✅ thêm dòng này
      this.currentTab = this.getTabByStatus(this.selectedStatus);

      this.loadTours();
      this.extractTourIds();
    });
  }

  ngOnDestroy(): void {
    this.queryParamsSubscription?.unsubscribe();
  }


  onTypeChange() {
    console.log('Đã chọn loại:', this.selectedType, this.typeList[this.selectedType - 1]);
    this.navigateWithQuery(1, this.selectedTourId, this.selectedStatus, this.selectedType);
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

  // Gọi API chính
  loadTours(): void {
    this.errorMessage = '';
    const tourId = this.selectedTourId.trim();
    const status = this.selectedStatus;
    const page = this.currentPage;
    const type = this.selectedType;

    const tourObservable = !tourId
      ? this.tourService.getToursByStatus(status, page, type)
      : this.tourService.getTourByIdAndStatus(Number(tourId), status, page, type);

    tourObservable.subscribe({
      next: res => {
        this.tours = res.data || [];
        this.totalPages = this.tours.length === 0 ? 1 : res.totalPage || 1;
        if (this.tours.length === 0) {
          this.errorMessage = 'Không tìm thấy tour phù hợp.';
        }
      },
      error: err => {
        this.tours = [];
        this.totalPages = 1;
        this.errorMessage = err.message;
      }
    });
  }


  /*loadFilterOptions(): void {
    this.tourService.getStartDestinations().subscribe({
      next: data => this.departures = data,
      error: err => console.error('Lỗi lấy điểm khởi hành:', err.message)
    });

    this.tourService.getDestinations().subscribe({
      next: data => this.destinations = data,
      error: err => console.error('Lỗi lấy điểm đến:', err.message)
    });
  }*/

  formatDate(date: string | undefined): string | undefined {
    if (!date) return undefined;
    const d = new Date(date);
    if (isNaN(d.getTime())) return undefined;

    const formattedDate = d.toISOString().split('T')[0];
    const today = new Date().toISOString().split('T')[0];
    return formattedDate < today ? today : formattedDate;
  }
  getStatusByTab(): number {
    switch (this.currentTab) {
      case 'opening': return 0;
      case 'moving': return 1;
      case 'moved': return 2;
      case 'admincanceled': return 3;
      default: return 0;
    }
  }
  getTabByStatus(status: number): 'opening' | 'moving' | 'moved' | 'admincanceled' {
    switch (status) {
      case 0: return 'opening';
      case 1: return 'moving';
      case 2: return 'moved';
      case 3: return 'admincanceled';
      default: return 'opening';
    }
  }
  // Chuyển tab → đổi status, reset tourId
  switchTab(tab: 'opening' | 'moving' | 'moved' | 'admincanceled'): void {
    this.currentTab = tab;
    this.selectedTourId = '';
    this.selectedStatus = this.getStatusByTab();
    this.selectedType = 1;
    this.navigateWithQuery(1, '', this.selectedStatus, this.selectedType);
  }
  // Chọn mã tour trong tab hiện tại
  onTourIdChange(): void {
    this.navigateWithQuery(1, this.selectedTourId, this.selectedStatus, this.selectedType);
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.navigateWithQuery(page, this.selectedTourId, this.selectedStatus, this.selectedType);
    }
  }

  navigateWithQuery(page: number, tourId: string, status: number, type: number): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page,
        tourId: tourId || undefined,
        status,
        type
      },
      queryParamsHandling: 'merge'
    });
  }
}
