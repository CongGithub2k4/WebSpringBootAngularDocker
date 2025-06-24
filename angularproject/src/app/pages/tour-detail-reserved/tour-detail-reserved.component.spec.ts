import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourDetailReservedComponent } from './tour-detail-reserved.component';

describe('TourDetailReservedComponent', () => {
  let component: TourDetailReservedComponent;
  let fixture: ComponentFixture<TourDetailReservedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourDetailReservedComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourDetailReservedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
