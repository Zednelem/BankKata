import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { FormBuilder } from '@angular/forms';
import { Account } from 'app/core/user/account.model';
import { BehaviorSubject, Subject, Subscription } from 'rxjs';
import { BankService } from 'app/core/bank/bank.service';
import { takeUntil } from 'rxjs/operators';

export enum State {
  INIT = 'INIT',
  LOADING = 'LOADING',
  ERROR = 'ERROR',
  SUCCESS = 'SUCCESS',
}

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent implements OnInit, OnDestroy {
  destroy$: Subject<boolean> = new Subject<boolean>();

  depositState$: BehaviorSubject<State> = new BehaviorSubject<State>(State.INIT);
  depositState: State = State.INIT;

  withdrawState$: BehaviorSubject<State> = new BehaviorSubject<State>(State.INIT);
  withdrawState: State = State.INIT;

  account: Account | null = null;
  authSubscription?: Subscription;

  constructor(public accountService: AccountService, private formBuilder: FormBuilder, private bankService: BankService) {
    this.depositState$.next(State.INIT);
    this.withdrawState$.next(State.INIT);
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.unsubscribe();
  }

  onUserHasValidatedDeposit($event: number): void {
    this.depositStateHasChanged(State.LOADING);
    this.bankService
      .deposeMoney($event)
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        () => this.depositStateHasChanged(State.SUCCESS),
        () => this.depositStateHasChanged(State.ERROR)
      );
  }

  private depositStateHasChanged(newState: State): void {
    this.depositState = newState;
    this.depositState$.next(this.depositState);
  }

  onUserHasValidatedWithdraw($event: number): void {
    this.withdrawStateHasChanged(State.LOADING);
    this.bankService
      .withdrawMoney($event)
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        () => this.withdrawStateHasChanged(State.SUCCESS),
        () => this.withdrawStateHasChanged(State.ERROR)
      );
  }

  private withdrawStateHasChanged(newState: State): void {
    this.withdrawState = newState;
    this.withdrawState$.next(this.withdrawState);
  }
}
