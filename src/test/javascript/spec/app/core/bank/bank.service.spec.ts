import { TestBed } from '@angular/core/testing';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JhiDateUtils } from 'ng-jhipster';

import { BankService } from 'app/core/bank/bank.service';
import { SERVER_API_URL } from 'app/app.constants';

describe('Service Tests', () => {
  describe('Bank Service', () => {
    let service: BankService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [JhiDateUtils],
      });

      service = TestBed.get(BankService);
      httpMock = TestBed.get(HttpTestingController);
    });

    afterEach(() => {
      httpMock.verify();
    });

    describe('Service methods', () => {
      it('should call correct URL', () => {
        service.deposeMoney(0).subscribe();

        const req = httpMock.expectOne({ method: 'POST' });
        const resourceUrl = SERVER_API_URL + 'api/bank';
        expect(req.request.url).toEqual(`${resourceUrl}/deposit-money`);
      });

      it('should return idOperation', () => {
        let expectedResult: string | undefined;

        service.deposeMoney(100).subscribe(receivedId => {
          expectedResult = receivedId;
        });

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush('idOperation');
        expect(expectedResult).toEqual('idOperation');
      });

      it('should propagate not found response', () => {
        let expectedResult = 0;

        service.deposeMoney(-100).subscribe(null, (error: HttpErrorResponse) => {
          expectedResult = error.status;
        });

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush('Invalid request parameters', {
          status: 404,
          statusText: 'Bad Request',
        });
        expect(expectedResult).toEqual(404);
      });
    });
  });
});
