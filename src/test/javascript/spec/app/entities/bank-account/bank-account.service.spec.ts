import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { BankAccountService } from 'app/entities/bank-account/bank-account.service';
import { IBankAccount, BankAccount } from 'app/shared/model/bank-account.model';

describe('Service Tests', () => {
  describe('BankAccount Service', () => {
    let injector: TestBed;
    let service: BankAccountService;
    let httpMock: HttpTestingController;
    let elemDefault: IBankAccount;
    let expectedResult: IBankAccount | IBankAccount[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(BankAccountService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new BankAccount(0, 0, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should return a list of BankAccount', () => {
        const returnedFromService = Object.assign(
          {
            balance: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

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
