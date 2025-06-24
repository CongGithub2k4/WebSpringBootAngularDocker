import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourDetailCanceledComponent } from './tour-detail-canceled.component';

describe('TourDetailCanceledComponent', () => {
  let component: TourDetailCanceledComponent;
  let fixture: ComponentFixture<TourDetailCanceledComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourDetailCanceledComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourDetailCanceledComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
