import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KataTestModule } from '../../test.module';
import { AccountService } from 'app/core/auth/account.service';
import { DashboardComponent } from 'app/bank/dashboard/dashboard.component';
import { BankService } from 'app/core/bank/bank.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MockBankService } from '../../helpers/mock-bank.service';
import { Statement } from '../../../../../main/webapp/app/bank/statements/statements.component';

describe('Component Tests', () => {
  describe('Dashboard Component', () => {
    let comp: DashboardComponent;
    let fixture: ComponentFixture<DashboardComponent>;
    let accountService: AccountService;
    let bankService: MockBankService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [KataTestModule, ReactiveFormsModule, FormsModule],
        declarations: [DashboardComponent],
      })
        .overrideTemplate(DashboardComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(DashboardComponent);
      comp = fixture.componentInstance;
      accountService = TestBed.get(AccountService);
      bankService = TestBed.get(BankService);
    });

    it('Should call accountService.getAuthenticationState on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(accountService.getAuthenticationState).toHaveBeenCalled();
    });

    it('Should call bankService.getOperations on init', () => {
      // WHEN
      bankService.setFetchOperationsResponse(
        new Array<Statement>(
          {
            id: 0,
            amount: 100,
            date: new Date(1234567890),
          },
          {
            id: 1,
            amount: -100,
            date: new Date(9876543210),
          }
        )
      );

      comp.ngOnInit();

      // THEN
      expect(bankService.fetchOperationsSpy).toBeCalled();
      expect(bankService.fetchOperationsSpy).toBeCalledTimes(1);
    });

    it('Should call bankService.deposeMoney when user has validated deposit is called', () => {
      // WHEN

      bankService.setDepositMoneyResponse('SuccessOperationId');
      comp.onUserHasValidatedDeposit(123);

      // THEN
      expect(bankService.deposeMoneySpy).toBeCalledWith(123);
      expect(bankService.deposeMoneySpy).toBeCalledTimes(1);
    });

    it('Should call bankService.withdrawMoney when user has validated deposit is called', () => {
      // WHEN

      bankService.setWithdrawMoneyResponse('SuccessOperationId');
      comp.onUserHasValidatedWithdraw(123);

      // THEN
      expect(bankService.withdrawMoneySpy).toBeCalledWith(123);
      expect(bankService.withdrawMoneySpy).toBeCalledTimes(1);
    });

    it('Should call bankService.deposeMoney and set error state', done => {
      // WHEN

      let counter = 0;
      comp.depositState$.subscribe(data => {
        if (counter === 0) {
          expect(data).toEqual('INIT');
        }
        if (counter === 1) {
          expect(data).toEqual('LOADING');
        }
        if (counter === 2) {
          expect(data).toEqual('ERROR');
          done();
        }
        counter++;
      });

      bankService.setDepositMoneyDoThrow(new Error('Error mock'));

      comp.onUserHasValidatedDeposit(-123);

      // THEN
      expect(bankService.deposeMoneySpy).toBeCalledWith(-123);
      expect(bankService.deposeMoneySpy).toBeCalledTimes(1);
    }, 1500);

    it('Should call bankService.withdrawMoney and set error state', done => {
      // WHEN

      let counter = 0;
      comp.withdrawState$.subscribe(data => {
        if (counter === 0) {
          expect(data).toEqual('INIT');
        }
        if (counter === 1) {
          expect(data).toEqual('LOADING');
        }
        if (counter === 2) {
          expect(data).toEqual('ERROR');
          done();
        }
        counter++;
      });

      bankService.setWithdrawMoneyDoThrow(new Error('Error mock'));

      comp.onUserHasValidatedWithdraw(-123);

      // THEN
      expect(bankService.withdrawMoneySpy).toBeCalledWith(-123);
      expect(bankService.withdrawMoneySpy).toBeCalledTimes(1);
    }, 1500);
  });
});
