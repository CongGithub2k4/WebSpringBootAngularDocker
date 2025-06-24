import { Routes } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { HomeDiscoveryDestinationsFooterComponent } from './pages/home-discovery-destinations-footer/home-discovery-destinations-footer.component';
import { TourListComponent } from './pages/tourlist/tourlist.component';
import { TourDetailInfoComponent } from './pages/tour-detail-info/tour-detail-info.component';
import { TourDetailReservedComponent } from './pages/tour-detail-reserved/tour-detail-reserved.component';
import { TourDetailMovedComponent } from './pages/tour-detail-moved/tour-detail-moved.component';
import { TourDetailCanceledComponent } from './pages/tour-detail-canceled/tour-detail-canceled.component';
import { AccountBookingsComponent } from './pages/account-bookings/account-bookings.component';
import { AdminBookingComponent } from './pages/admin-booking/admin-booking.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { AdminUserlistComponent } from './pages/admin-userlist/admin-userlist.component';
import { AdminNewtourComponent } from './pages/admin-newtour/admin-newtour.component';
import { AdminTourlistComponent } from './pages/admin-tourlist/admin-tourlist.component';
import { LoginsigninComponent } from './pages/loginsignin/loginsignin.component';
import { authGuard } from './guards/loginguard.guard';
import { AdminTourDetailComponent } from './pages/admin-tour-detail/admin-tour-detail.component';

export const routes: Routes = [
    {path: '', component: HomeDiscoveryDestinationsFooterComponent }, // Trang chủ
    {path: 'loginsignin', component: LoginsigninComponent},
    {path: 'tourlist', component: TourListComponent }, // Khám phá các Tour
    {path: 'tourdetail/info', component: TourDetailInfoComponent },
    {path: 'tourdetail/reserved', component: TourDetailReservedComponent},
    {path: 'tourdetail/moved', component: TourDetailMovedComponent},
    {path: 'tourdetail/canceled', component: TourDetailCanceledComponent},
    {path: 'account', component: AccountBookingsComponent, canActivate: [authGuard]},
    {path: 'notfound', component: NotFoundComponent},
    {path: 'admin/users', component: AdminUserlistComponent},
    {path: 'admin/booking', component: AdminBookingComponent},
    {path: 'admin/newtour', component: AdminNewtourComponent},
    {path: 'admin/tourlist', component: AdminTourlistComponent},
    {path: 'admin/tourdetail/info', component: AdminTourDetailComponent}
  ];