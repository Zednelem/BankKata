import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KataTestModule } from '../../test.module';
import { AccountService } from 'app/core/auth/account.service';
import { DashboardComponent } from 'app/bank/dashboard/dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Statement } from 'app/core/bank/statement.model';
import { BankService } from 'app/core/bank/bank.service';
import { depositStatement, MockBankService, withdrawStatement } from '../../helpers/mock-bank.service';
import { TimeProviderService } from 'app/core/system/time-provider.service';
import { MockTimeProviderService } from '../../helpers/mock-time-provider.service';

describe('Component Tests', () => {
  describe('Dashboard Component', () => {
    let dashboardComponent: DashboardComponent;
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
      TestBed.configureTestingModule({
        imports: [],
        providers: [
          {
            provide: BankService,
            useClass: MockBankService,
          },
          {
            provide: TimeProviderService,
            useClass: MockTimeProviderService,
          },
        ],
      });
      fixture = TestBed.createComponent(DashboardComponent);
      dashboardComponent = fixture.componentInstance;
      accountService = TestBed.inject(AccountService);
      bankService = TestBed.get(BankService);
    });

    it('Should call accountService.getAuthenticationState on init', () => {
      // WHEN
      dashboardComponent.ngOnInit();

      // THEN
      expect(accountService.getAuthenticationState).toHaveBeenCalled();
      expect(bankService.fetchOperationsSpy).toHaveBeenCalled();
    });

    it('Should call bankService.getOperations on init', () => {
      dashboardComponent.ngOnInit();

      // THEN
      expect(bankService.fetchOperationsSpy).toBeCalled();
      expect(bankService.fetchOperationsSpy).toBeCalledTimes(1);
    });

    it('Should call bankService.deposeMoney when user has validated deposit is called', () => {
      // GIVEN
      const expectedCall: Statement = { ...depositStatement };
      expectedCall.createdBy = 'CONNECTED USER';
      expectedCall.label = 'LABEL';

      bankService.setDepositMoneyResponse(depositStatement);

      // WHEN
      dashboardComponent.onUserHasValidatedDeposit(1000);

      // THEN
      expect(bankService.deposeMoneySpy).toBeCalledWith(expectedCall);
      // TODO make this line to run
      //  expect(bankService.deposeMoneySpy).toReturnWith(depositStatement)
      expect(bankService.deposeMoneySpy).toBeCalledTimes(1);
    });

    it('Should call bankService.withdrawMoney when user has validated deposit is called', () => {
      // GIVEN
      const expectedCall: Statement = { ...withdrawStatement };
      expectedCall.createdBy = 'CONNECTED USER';
      expectedCall.label = 'LABEL';

      bankService.setWithdrawMoneyResponse(withdrawStatement);

      // WHEN

      dashboardComponent.onUserHasValidatedWithdraw(1000);

      // THEN
      expect(bankService.withdrawMoneySpy).toBeCalledWith(expectedCall);
      // TODO make this line to run
      //  expect(bankService.withdrawMoneySpy).toReturnWith(withdrawStatement)
      expect(bankService.withdrawMoneySpy).toBeCalledTimes(1);
    });

    it('Should call bankService.deposeMoney and set error state', done => {
      // GIVEN
      const expectedCall: Statement = { ...depositStatement };
      expectedCall.createdBy = 'CONNECTED USER';
      expectedCall.label = 'LABEL';
      bankService.setDepositMoneyDoThrow(new Error('Error mock'));

      // ASYNC SHOULD

      let counter = 0;
      dashboardComponent.stateService.depositState$.subscribe(data => {
        if (counter === 0) {
          expect(data).toEqual('INIT');
        }
        if (counter === 1) {
          expect(data).toEqual('LOADING');
        }
        if (counter === 2) {
          expect(data).toEqual('ERROR');
          // set a timeout to be sure that the code below has ran
          setTimeout(done(), 100);
        }
        counter++;
      });

      // WHEN
      dashboardComponent.onUserHasValidatedDeposit(1000);

      // THEN
      expect(bankService.deposeMoneySpy).toBeCalledWith(expectedCall);
      expect(bankService.deposeMoneySpy).toBeCalledTimes(1);
    }, 300);

    it('Should call bankService.withdrawMoney and set error state', done => {
      // GIVEN
      const expectedCall: Statement = { ...withdrawStatement };
      expectedCall.createdBy = 'CONNECTED USER';
      expectedCall.label = 'LABEL';
      bankService.setWithdrawMoneyDoThrow(new Error('Error mock'));

      // ASYNC SHOULD

      let counter = 0;
      dashboardComponent.stateService.withdrawState$.subscribe(data => {
        if (counter === 0) {
          expect(data).toEqual('INIT');
        }
        if (counter === 1) {
          expect(data).toEqual('LOADING');
        }
        if (counter === 2) {
          expect(data).toEqual('ERROR');
          // set a timeout to be sure that the code below has ran
          setTimeout(done(), 100);
        }
        counter++;
      });

      // WHEN
      dashboardComponent.onUserHasValidatedWithdraw(1000);

      // THEN
      expect(bankService.withdrawMoneySpy).toBeCalledWith(expectedCall);
      expect(bankService.withdrawMoneySpy).toBeCalledTimes(1);
    }, 300);
  });
});
