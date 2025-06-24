import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminHeaderComponent } from "../../components/admin-header/admin-header.component";
import { TourService } from '../../services/tour.service';
import { Router } from '@angular/router';
export interface NewTourRequest {
  tourName: string,
  startDestination: string,
  dayNumber: number,
  nightNumber: number,
  thoughoutDestination: string[],
  daytimeStart: string,
  totalSlot: number,
  price: number
}
@Component({
  selector: 'app-admin-newtour',
  standalone: true,
  imports: [CommonModule, FormsModule, AdminHeaderComponent],
  templateUrl: './admin-newtour.component.html',
  styleUrls: ['./admin-newtour.component.css']
})
export class AdminNewtourComponent {
  tour = {
    tourName: '',
    startDestination: '',
    dayNumber: 0,
    nightNumber: 0,
    thoughoutDestination: [''],
    daytimeStart: '',
    totalSlot: 0,
    price: 0
  };

  thoughoutInput: string = '';
  imageLink: string = '';
  selectedFile: File | null = null;
  previewUrl: string | ArrayBuffer | null = null;
  isImageUploaded: boolean = false;  // Thêm biến này để theo dõi trạng thái ảnh đã tải lên
  formSubmitted = false;
  dropdownOpen: boolean = false;
  validDayStart: boolean = true;

  constructor(
    private http: HttpClient,
    private tourService: TourService,
    private router: Router
  ) { }

  ngOnInit() {
    this.loadFilterOptions();
  }
  onStartDestinationChange() {
    console.log('Selected start destination:', this.tour.startDestination);
    // Có thể thêm logic kiểm tra nếu cần
  }
  getDisplayDestinations(): string {
    if (this.tour.thoughoutDestination.length > 0) {
      return this.tour.thoughoutDestination
        .filter(item => item && item.trim() !== '')
        .join(', ');
    }
    return 'Chọn điểm đến';
  }
  startList: string[] = ['Hà Nội', 'Đà Nẵng', 'Hồ Chí Minh']
  locationList: string[] = [
    'Hà Nội',
    'Hạ Long',
    'Huế',
    'Đà Nẵng',
    'Hội An',
    'Nha Trang',
    'Đà Lạt',
    'Hồ Chí Minh',
    'Cần Thơ'
  ];
  loadFilterOptions(): void {
    this.tourService.getStartDestinations().subscribe({
      next: data => this.startList = data,
      error: err => console.error('Lỗi lấy điểm khởi hành:', err.message)
    });

    this.tourService.getDestinations().subscribe({
      next: data => this.locationList = data,
      error: err => console.error('Lỗi lấy điểm đến:', err.message)
    });
  }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  onDestinationToggle(location: string) {
    const index = this.tour.thoughoutDestination.indexOf(location);
    if (index > -1) {
      this.tour.thoughoutDestination.splice(index, 1);
    } else {
      this.tour.thoughoutDestination.push(location);
    }
  }

  checkAfterToday(): void {
    if (!this.tour?.daytimeStart) {
      this.validDayStart = false;
      return;
    }

    const inputDate = new Date(this.tour.daytimeStart);
    const today = new Date();

    // So sánh chỉ phần ngày, bỏ qua giờ
    inputDate.setHours(0, 0, 0, 0);
    today.setHours(0, 0, 0, 0);

    this.validDayStart = inputDate > today;
  }

  /*updateDestinations() {
    this.tour.thoughoutDestination = this.thoughoutInput
      .split(',')
      .map(item => item.trim())
      .filter(item => item.length > 0);
  }*/
  onFileSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      const maxSize = 5 * 1024 * 1024; // 5MB
      if (file.size > maxSize) {
        console.log('File too large:', file.size);
        alert('Ảnh quá lớn, vui lòng chọn ảnh dưới 5MB.');
        return;
      }
      this.selectedFile = file;
      console.log('File selected:', file.name, file.size);
      const reader = new FileReader();
      reader.onload = () => this.previewUrl = reader.result;
      reader.readAsDataURL(file);
    } else {
      console.log('No file selected');
    }
  }

  /*uploadImageLink() {
    if (!this.imageLink.trim()) return;
    this.http.post<{ imagePath: string }>('http://localhost:8080/tour/image-from-url', { imageUrl: this.imageLink })
      .subscribe({
        next: res => {
          this.tour.linkImage = res.imagePath;
          this.isImageUploaded = true;  // Đánh dấu ảnh đã được upload
          console.log('Ảnh từ link đã lưu:', res.imagePath);
        },
        error: err => {
          this.isImageUploaded = false;  // Đánh dấu ảnh chưa upload thành công
          console.error('Lỗi gửi link ảnh', err);
        }
      });
  }

  uploadImageFile() {
    if (!this.selectedFile) return;
    const formData = new FormData();
    formData.append('file', this.selectedFile);

    this.http.post<{ imagePath: string }>('http://localhost:8080/tour/upload-image', formData)
      .subscribe({
        next: res => {
          this.tour.linkImage = res.imagePath;
          this.isImageUploaded = true;  // Đánh dấu ảnh đã được upload
          console.log('Ảnh từ máy đã tải lên:', res.imagePath);
        },
        error: err => {
          this.isImageUploaded = false;  // Đánh dấu ảnh chưa upload thành công
          console.error('Lỗi tải ảnh từ máy', err);
        }
      });
  }*/

  submitTour() {
    console.log('submitTour called');
    this.formSubmitted = true;
    const filteredDestinations = this.tour.thoughoutDestination.filter(destination => {
      // Đảm bảo 'destination' không phải là null/undefined VÀ
      // sau khi loại bỏ khoảng trắng ở hai đầu, nó không phải là chuỗi rỗng.
      return destination != null && destination.trim() !== '';
    });
    this.tour.thoughoutDestination = filteredDestinations;
    const { tourName, startDestination, thoughoutDestination, daytimeStart, price, dayNumber, nightNumber, totalSlot } = this.tour;
    console.log('Form data:', this.tour);
    if (
      !tourName || tourName.trim() === '' ||
      !startDestination ||
      !thoughoutDestination || thoughoutDestination.length === 0 ||
      !daytimeStart || daytimeStart.trim() === '' ||
      !price || price <= 0 ||
      !dayNumber || dayNumber <= 0 ||
      !nightNumber || nightNumber <= 0 ||
      !totalSlot || totalSlot <= 0
    ) {
      console.log('Validation failed:', this.tour);
      alert('Vui lòng điền đầy đủ thông tin hợp lệ.');
      return;
    }
    if (!this.selectedFile) {
      console.log('No file selected');
      alert('Vui lòng tải lên ảnh trước khi tạo tour.');
      return;
    }
    const dateObj = new Date(this.tour.daytimeStart);
    const yyyy = dateObj.getFullYear();
    const mm = String(dateObj.getMonth() + 1).padStart(2, '0');
    const dd = String(dateObj.getDate()).padStart(2, '0');
    this.tour.daytimeStart = `${yyyy}-${mm}-${dd}`;

    console.log('Submitting tour:', this.tour);
    console.log('Selected file:', this.selectedFile);
    this.tourService.addNewTour(this.tour, this.selectedFile).subscribe({
      next: res => {
        console.log('Tour created successfully, response:', res);
        alert('Tạo Tour thành công');
        this.router.navigate(['/admin/tourlist']).then(success => {
          console.log('Navigation to /admin/tourlist success:', success);
        });
      },
      error: err => {
        console.error('Submit error:', err);
        alert(`Lỗi: ${err.message}`);
      },
      complete: () => {
        console.log('Request completed');
      }
    });
  }
}
