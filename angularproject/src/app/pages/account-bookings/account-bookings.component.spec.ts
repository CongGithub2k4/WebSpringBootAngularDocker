import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountBookingsComponent } from './account-bookings.component';

describe('AccountBookingsComponent', () => {
  let component: AccountBookingsComponent;
  let fixture: ComponentFixture<AccountBookingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountBookingsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountBookingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
