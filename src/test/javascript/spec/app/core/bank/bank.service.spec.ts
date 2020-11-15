import { TestBed } from '@angular/core/testing';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JhiDateUtils } from 'ng-jhipster';

import { BankService } from 'app/core/bank/bank.service';
import { SERVER_API_URL } from 'app/app.constants';
import { Statement } from 'app/core/bank/statement.model';
import { depositStatement, genericFetchResponse, genericStatement, withdrawStatement } from '../../../helpers/mock-bank.service';

describe('Service Tests', () => {
  describe('Bank Service', () => {
    let service: BankService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [JhiDateUtils],
      });

      service = TestBed.inject(BankService);
      httpMock = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
      httpMock.verify();
    });

    describe('Service deposit method', () => {
      it('should call correct URL', () => {
        service.deposeMoney(depositStatement).subscribe();

        const req = httpMock.expectOne({ method: 'POST' });
        const resourceUrl = SERVER_API_URL + 'api/bank';
        expect(req.request.url).toEqual(`${resourceUrl}/actions/deposit-money`);
      });

      it('should return deposit Statement Object', () => {
        let actualStatement: Statement | null = null;

        const expectedStatement = depositStatement;
        // type is an attribute for the the front display only and should be overriden by the back
        service.deposeMoney(withdrawStatement).subscribe(response => {
          actualStatement = response;
        });

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(expectedStatement);
        expect(actualStatement).toEqual(expectedStatement);
      });

      it('should propagate not found response', () => {
        let status: number | null = null;
        service.deposeMoney(genericStatement).subscribe(null, (error: HttpErrorResponse) => {
          status = error.status;
        });

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush('Invalid request parameters', {
          status: 404,
          statusText: 'Bad Request',
        });
        expect(status).toEqual(404);
      });
    });

    describe('Service withdraw method', () => {
      it('should call correct URL', () => {
        service.withdrawMoney(withdrawStatement).subscribe();

        const req = httpMock.expectOne({ method: 'POST' });
        const resourceUrl = SERVER_API_URL + 'api/bank';
        expect(req.request.url).toEqual(`${resourceUrl}/actions/withdraw-money`);
      });

      it('should return a witdraw statement', () => {
        const expectedResult = withdrawStatement;
        let actualStatement: Statement | null = null;
        service.withdrawMoney(depositStatement).subscribe(response => {
          actualStatement = response;
        });

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(withdrawStatement);
        expect(actualStatement).toEqual(expectedResult);
      });

      it('should propagate not found response', () => {
        let actualStatus: number | null = null;
        service.withdrawMoney(genericStatement).subscribe(undefined, (error: HttpErrorResponse) => {
          actualStatus = error.status;
        });

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush('Invalid request parameters', {
          status: 404,
          statusText: 'Bad Request',
        });
        expect(actualStatus).toEqual(404);
      });
    });
    describe('Service Fecth method ', () => {
      it('should call correct URL', () => {
        service.fetchOperations();

        const req = httpMock.expectOne({ method: 'GET' });
        const resourceUrl = SERVER_API_URL + 'api/bank';
        expect(req.request.url).toEqual(`${resourceUrl}/statements`);
      });
      it('should return a list of statement Objects', () => {
        let actualStatement: Statement[] | null = null;

        const expectedStatements = [...genericFetchResponse];
        service.statements$.subscribe(response => {
          actualStatement = response;
        });
        // type is an attribute for the the front display only and should be overriden by the back
        service.fetchOperations();

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(expectedStatements);
        expect(actualStatement).toEqual(expectedStatements);
      });
    });
  });
});
