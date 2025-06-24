import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSideUserCardComponent } from './admin-side-user-card.component';

describe('AdminSideUserCardComponent', () => {
  let component: AdminSideUserCardComponent;
  let fixture: ComponentFixture<AdminSideUserCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminSideUserCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSideUserCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
