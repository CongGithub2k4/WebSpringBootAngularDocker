import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourCardCanceledByadminComponent } from './user-booking-canceled-byadmin.component';

describe('TourCardCanceledByadminComponent', () => {
  let component: TourCardCanceledByadminComponent;
  let fixture: ComponentFixture<TourCardCanceledByadminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourCardCanceledByadminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourCardCanceledByadminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
