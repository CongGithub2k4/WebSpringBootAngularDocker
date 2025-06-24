import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSideTourCardBillComponent } from './admin-side-booking.component';

describe('AdminSideTourCardBillComponent', () => {
  let component: AdminSideTourCardBillComponent;
  let fixture: ComponentFixture<AdminSideTourCardBillComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminSideTourCardBillComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSideTourCardBillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
