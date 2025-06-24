import { TestBed } from '@angular/core/testing';

import { LoginRouteLinkService } from './login-route-link.service';

describe('LoginRouteLinkService', () => {
  let service: LoginRouteLinkService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoginRouteLinkService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
