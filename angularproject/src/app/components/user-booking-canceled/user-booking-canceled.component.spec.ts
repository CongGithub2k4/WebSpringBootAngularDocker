import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourCardCanceledComponent } from './user-booking-canceled.component';

describe('TourCardCanceledComponent', () => {
  let component: TourCardCanceledComponent;
  let fixture: ComponentFixture<TourCardCanceledComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourCardCanceledComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourCardCanceledComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
