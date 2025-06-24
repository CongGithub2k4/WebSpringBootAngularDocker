import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminNewtourComponent } from './admin-newtour.component';

describe('AdminNewtourComponent', () => {
  let component: AdminNewtourComponent;
  let fixture: ComponentFixture<AdminNewtourComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminNewtourComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminNewtourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
