import { Injectable } from '@angular/core';
import { OperationComponentWording } from './operation/operation.component';
import { Statement, StatementModel, StatementType } from 'app/core/bank/statement.model';
import { BehaviorSubject } from 'rxjs';

export enum State {
  INIT = 'INIT',
  LOADING = 'LOADING',
  ERROR = 'ERROR',
  SUCCESS = 'SUCCESS',
}

@Injectable({
  providedIn: 'root',
})
export class BankStateService {
  private depositState: State = State.INIT;
  private withdrawState: State = State.INIT;

  private readonly _depositState = new BehaviorSubject<State>(State.INIT);
  readonly depositState$ = this._depositState.asObservable();
  private readonly _withdrawState = new BehaviorSubject<State>(State.INIT);
  readonly withdrawState$ = this._withdrawState.asObservable();

  constructor() {}

  public getDepositWording(): OperationComponentWording {
    return {
      title: 'How much would you like to deposit?',
      inputLabel: 'Please set the amount to deposit',
      submitButton: 'Validate',
      successMessagePrefix: 'Your deposit of ',
      successMessageSuffix: ' $ has been taken in account.',
      loadingMessage: 'The transfer is currently Loading ...',
      errorMessagePrefix: 'Error while deposing ',
      errorMessageSuffix: ' $ into your account. Try again later',
    };
  }
  public geWithdrawWording(): OperationComponentWording {
    return {
      title: 'How much would you like to withdraw?',
      inputLabel: 'Please set the amount to withdraw',
      submitButton: 'Validate',
      successMessagePrefix: 'Your withdrawal of ',
      successMessageSuffix: ' $ has been taken in account.',
      loadingMessage: 'The transfer is currently Loading ...',
      errorMessagePrefix: 'Error while withdrawing ',
      errorMessageSuffix: ' $ from your account. Try again later',
    };
  }

  depositStateHasChangedTo(newState: State): void {
    this.depositState = newState;
    this._depositState.next(this.depositState);
  }

  withdrawStateHasChangedTo(newState: State): void {
    this.withdrawState = newState;
    this._withdrawState.next(this.withdrawState);
  }

  createNewStatement(amount: number, type: StatementType): Statement {
    return new StatementModel('CONNECETED USER', new Date(), 'LABEL', type, amount);
  }
}
