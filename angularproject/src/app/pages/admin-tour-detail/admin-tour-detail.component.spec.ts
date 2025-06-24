import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTourDetailComponent } from './admin-tour-detail.component';

describe('AdminTourDetailComponent', () => {
  let component: AdminTourDetailComponent;
  let fixture: ComponentFixture<AdminTourDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminTourDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminTourDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
