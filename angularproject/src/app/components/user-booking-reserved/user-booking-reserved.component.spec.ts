import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourCardReservedComponent } from './user-booking-reserved.component';

describe('TourCardReservedComponent', () => {
  let component: TourCardReservedComponent;
  let fixture: ComponentFixture<TourCardReservedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourCardReservedComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourCardReservedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
