import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourCardInfoComponent } from './tour-card-info.component';

describe('TourCardInfoComponent', () => {
  let component: TourCardInfoComponent;
  let fixture: ComponentFixture<TourCardInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourCardInfoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TourCardInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
