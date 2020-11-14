import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { Subject, Subscription } from 'rxjs';
import { BankService } from 'app/core/bank/bank.service';
import { takeUntil } from 'rxjs/operators';
import { OperationComponentWording } from 'app/bank/operation/operation.component';
import { BankStateService, State } from 'app/bank/bank-state.service';
import { StatementType } from 'app/core/bank/statement.model';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent implements OnInit, OnDestroy {
  destroy$: Subject<boolean> = new Subject<boolean>();

  account: Account | null = null;
  authSubscription?: Subscription;
  depositWording: OperationComponentWording;
  withdrawWording: OperationComponentWording;

  constructor(public accountService: AccountService, public bankService: BankService, public stateService: BankStateService) {
    this.depositWording = stateService.getDepositWording();
    this.withdrawWording = stateService.getWithdrawWording();
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    this.bankService.fetchOperations();
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.unsubscribe();
  }

  onUserHasValidatedDeposit($event: number): void {
    this.stateService.depositStateHasChangedTo(State.LOADING);
    this.bankService
      .deposeMoney(this.stateService.createNewStatement($event, StatementType.DEPOSIT))
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        () => this.stateService.depositStateHasChangedTo(State.SUCCESS),
        () => this.stateService.depositStateHasChangedTo(State.ERROR)
      );
  }

  onUserHasValidatedWithdraw($event: number): void {
    this.stateService.withdrawStateHasChangedTo(State.LOADING);
    this.bankService
      .withdrawMoney(this.stateService.createNewStatement($event, StatementType.WITHDRAW))
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        () => this.stateService.withdrawStateHasChangedTo(State.SUCCESS),
        () => this.stateService.withdrawStateHasChangedTo(State.ERROR)
      );
  }
}
