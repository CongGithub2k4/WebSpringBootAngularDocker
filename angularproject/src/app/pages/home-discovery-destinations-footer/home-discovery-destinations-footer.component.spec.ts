import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeDiscoveryDestinationsFooterComponent } from './home-discovery-destinations-footer.component';

describe('HomeDiscoveryDestinationsFooterComponent', () => {
  let component: HomeDiscoveryDestinationsFooterComponent;
  let fixture: ComponentFixture<HomeDiscoveryDestinationsFooterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeDiscoveryDestinationsFooterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeDiscoveryDestinationsFooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
