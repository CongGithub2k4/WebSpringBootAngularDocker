import { Component, AfterViewInit, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HeaderComponent } from "../../components/header/header.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { catchError, of } from 'rxjs';
import { CommonModule, DatePipe } from '@angular/common';
import { TourService } from '../../services/tour.service';
import { AuthServiceService } from '../../services/auth-service.service';


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
  selector: 'app-home-discovery-destinations-footer',
  standalone: true,
  imports: [HeaderComponent, FooterComponent, DatePipe, CommonModule],
  templateUrl: './home-discovery-destinations-footer.component.html',
  styleUrls: ['./home-discovery-destinations-footer.component.css']
})
export class HomeDiscoveryDestinationsFooterComponent implements OnInit, AfterViewInit {
  allDestinations: string[] = [];
  tours: Tour[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthServiceService,
    private tourService: TourService // <-- THÊM VÀO ĐÂY
  ) {}

  ngOnInit(): void {
    this.loadAllDestinations();
    this.loadTop3Tours();
    if(this.authService.isAdmin()) {
      this.router.navigate(['admin/booking']);
    }
  }

  loadAllDestinations(): void {
    this.tourService.getDestinations().pipe(
      catchError(err => {
        console.error(err.message || 'Không thể tải thông tin các điểm đến.');
        return of([]);
      })
    ).subscribe((destinations: string[]) => {
      this.allDestinations = destinations || [];
    });
  }

  loadTop3Tours(): void {
    this.tourService.getTop3Tours().pipe(
      catchError(err => {
        console.error(err.message || 'Không thể tải thông tin top3 tour.');
        return of([]);
      })
    ).subscribe((tours: Tour[]) => {
      this.tours = tours || [];
    });
  }

  ngAfterViewInit() {
    this.route.fragment.subscribe(fragment => {
      if (fragment) {
        const element = document.getElementById(fragment);
        if (element) {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
      }
    });
  }

  goToDetail(tourId: number | undefined, daytimeStart: string | undefined) {
    this.router.navigate(['/tourdetail/info'], {
      queryParams: {
        tour_id : tourId,
        daytime_start: daytimeStart
      }
    });
  }

  private normalizeString(str: string): string {
    return (str || '')
      .toLowerCase()
      .replace(/\s+/g, ' ')
      .trim();
  }

  goToTourList(destinationNameFromClick: string): void {
    let finalDestinationParam: string | undefined = destinationNameFromClick;
    if(finalDestinationParam.trim() == '') {
      finalDestinationParam = undefined;
    }
    this.router.navigate(['/tourlist'], {
      queryParams: {
        status: 0,
        page: 1,
        thoughoutDestination: finalDestinationParam
      },
    });
  }
}
