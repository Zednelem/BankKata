import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KataTestModule } from '../../test.module';
import { AccountService } from 'app/core/auth/account.service';
import { DashboardComponent } from 'app/bank/dashboard/dashboard.component';
import { BankService } from 'app/core/bank/bank.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MockBankService } from '../../helpers/mock-bank.service';

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

    it('Should call bankService.deposeMoney when it deposit is called', () => {
      // WHEN

      comp.formGroup.value.depositAmount = 123;
      bankService.setDepositMoneyResponse('SuccessOperationId');
      comp.deposit();

      // THEN
      expect(bankService.deposeMoneySpy).toBeCalledWith(123);
      expect(bankService.deposeMoneySpy).toBeCalledTimes(1);
    });

    it('Should call bankService.deposeMoney and set error state', done => {
      // WHEN

      comp.formGroup.value.depositAmount = 123;
      bankService.setDepositMoneyDoThrow(new Error('Error mock'));

      comp.deposit();

      // THEN
      expect(bankService.deposeMoneySpy).toBeCalledWith(123);
      expect(bankService.deposeMoneySpy).toBeCalledTimes(1);

      let counter = 0;
      comp.state$.subscribe(data => {
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
    }, 1500);
  });
});
