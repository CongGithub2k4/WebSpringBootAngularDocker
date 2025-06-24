import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSideTourCardInfoComponent } from './admin-side-tour-card-info.component';

describe('AdminSideTourCardInfoComponent', () => {
  let component: AdminSideTourCardInfoComponent;
  let fixture: ComponentFixture<AdminSideTourCardInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminSideTourCardInfoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSideTourCardInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
