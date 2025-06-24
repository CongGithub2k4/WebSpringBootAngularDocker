import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourDetailDoneComponent } from './tour-detail-moved.component';

describe('TourDetailDoneComponent', () => {
  let component: TourDetailDoneComponent;
  let fixture: ComponentFixture<TourDetailDoneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourDetailDoneComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourDetailDoneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
