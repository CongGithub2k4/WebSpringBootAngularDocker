import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourCardDoneComponent } from './user-booking-moved.component';

describe('TourCardDoneComponent', () => {
  let component: TourCardDoneComponent;
  let fixture: ComponentFixture<TourCardDoneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourCardDoneComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourCardDoneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
