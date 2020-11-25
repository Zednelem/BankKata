import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { BankStatementService } from 'app/entities/bank-statement/bank-statement.service';
import { IBankStatement, BankStatement } from 'app/shared/model/bank-statement.model';
import { StatementType } from 'app/shared/model/enumerations/statement-type.model';

describe('Service Tests', () => {
  describe('BankStatement Service', () => {
    let injector: TestBed;
    let service: BankStatementService;
    let httpMock: HttpTestingController;
    let elemDefault: IBankStatement;
    let expectedResult: IBankStatement | IBankStatement[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(BankStatementService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new BankStatement(0, 0, 'AAAAAAA', currentDate, StatementType.DEPOSIT, 'AAAAAAA', currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            validatedDate: currentDate.format(DATE_TIME_FORMAT),
            createdDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should return a list of BankStatement', () => {
        const returnedFromService = Object.assign(
          {
            amount: 1,
            label: 'BBBBBB',
            validatedDate: currentDate.format(DATE_TIME_FORMAT),
            statementType: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            validatedDate: currentDate,
            createdDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
