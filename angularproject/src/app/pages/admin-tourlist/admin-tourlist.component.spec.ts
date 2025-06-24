import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTourlistComponent } from './admin-tourlist.component';

describe('AdminTourlistComponent', () => {
  let component: AdminTourlistComponent;
  let fixture: ComponentFixture<AdminTourlistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminTourlistComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminTourlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
