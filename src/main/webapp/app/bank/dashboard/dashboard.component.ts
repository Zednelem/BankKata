import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Account } from 'app/core/user/account.model';
import { ReplaySubject, Subject, Subscription } from 'rxjs';
import { BankService } from 'app/core/bank/bank.service';
import { takeUntil } from 'rxjs/operators';

enum State {
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

  account: Account | null = null;
  authSubscription?: Subscription;

  formGroup: FormGroup;

  depositAmountvalue: number;

  public States = State;
  private state: State = State.INIT;
  public state$: ReplaySubject<State> = new ReplaySubject<State>();

  constructor(public accountService: AccountService, private formBuilder: FormBuilder, private bankService: BankService) {
    this.formGroup = this.formBuilder.group({
      depositAmount: ['0.00', [Validators.required, Validators.min(0)]],
    });
    this.depositAmountvalue = 0;
    this.state$.next(State.INIT);
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  deposit(): void {
    this.stateHasChanged(State.LOADING);
    this.depositAmountvalue = this.formGroup.value.depositAmount;
    this.bankService
      .deposeMoney(this.formGroup.value.depositAmount)
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        () => this.stateHasChanged(State.SUCCESS),
        () => this.stateHasChanged(State.ERROR)
      );
  }

  stateHasChanged(newState: State): void {
    this.state = newState;
    this.state$.next(this.state);
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.unsubscribe();
  }
}
