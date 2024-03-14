import { TestBed } from '@angular/core/testing';

import { EcommFormService } from './ecomm-form.service';

describe('EcommFormService', () => {
  let service: EcommFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EcommFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
