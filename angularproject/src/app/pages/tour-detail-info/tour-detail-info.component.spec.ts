import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourDetailInfoComponent } from './tour-detail-info.component';

describe('TourDetailInfoComponent', () => {
  let component: TourDetailInfoComponent;
  let fixture: ComponentFixture<TourDetailInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourDetailInfoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourDetailInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
