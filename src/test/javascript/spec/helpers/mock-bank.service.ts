import Spy = jasmine.Spy;
import { Observable, of, throwError } from 'rxjs';

import { SpyObject } from './spyobject';
import { BankService } from 'app/core/bank/bank.service';
import { Statement } from '../../../../main/webapp/app/bank/statements/statements.component';

export class MockBankService extends SpyObject {
  getSpy: Spy;
  deposeMoneySpy: Spy;
  withdrawMoneySpy: Spy;
  fetchOperationsSpy: Spy;

  constructor() {
    super(BankService);

    this.getSpy = this.spy('get').andReturn(this);
    this.deposeMoneySpy = this.spy('deposeMoney').andReturn(of(null));
    this.withdrawMoneySpy = this.spy('withdrawMoney').andReturn(of(null));
    this.fetchOperationsSpy = this.spy('fetchOperations').andReturn(of(null));
  }

  setDepositMoneyResponse(idOperation: string | null): void {
    this.deposeMoneySpy = this.spy('deposeMoney').andReturn(of(of(idOperation)));
  }

  setWithdrawMoneyResponse(idOperation: string | null): void {
    this.withdrawMoneySpy = this.spy('withdrawMoney').andReturn(of(of(idOperation)));
  }

  setDepositMoneyDoThrow(error: Error): void {
    const obs = new Observable(() => {
      throw throwError(error);
    });

    this.deposeMoneySpy = this.spy('deposeMoney').andReturn(obs);
  }

  setWithdrawMoneyDoThrow(error: Error): void {
    const obs = new Observable(() => {
      throw throwError(error);
    });

    this.withdrawMoneySpy = this.spy('withdrawMoney').andReturn(obs);
  }

  setFetchOperationsResponse(statements: Array<Statement>): void {
    this.fetchOperationsSpy = this.spy('fetchOperations').andReturn(of(of(statements)));
  }

  setFetchOperationsDoThrow(error: Error): void {
    const obs = new Observable(() => {
      throw throwError(error);
    });

    this.fetchOperationsSpy = this.spy('fetchOperations').andReturn(obs);
  }
}
