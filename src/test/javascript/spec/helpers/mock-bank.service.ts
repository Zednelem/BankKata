import Spy = jasmine.Spy;
import { Observable, of, throwError } from 'rxjs';

import { SpyObject } from './spyobject';
import { BankService } from 'app/core/bank/bank.service';
import { Statement, StatementModel, StatementType } from 'app/core/bank/statement.model';
import { newArray } from '@angular/compiler/src/util';
import { DEFAULT_NOW_TIME } from './mock-time-provider.service';

export const defaultCreatedBy = 'DEFAULT_CREATED_BY';
export const defaultlabel = 'DEFAULT_LABEL';
export const defaultCreatedDate = DEFAULT_NOW_TIME;
export const defaultAmount = 1000;
export const defaultType = StatementType.DEPOSIT;
export const genericStatement = new StatementModel(defaultCreatedBy, defaultCreatedDate, defaultlabel, defaultType, defaultAmount);
const tmpArray: Statement[] = newArray<Statement>(20);
for (let i = 0; i < 20; i++) {
  const statement: Statement = { ...genericStatement };
  statement.id = i;
  statement.statementType = i % 2 === 0 ? StatementType.DEPOSIT : StatementType.WITHDRAW;
  statement.validatedDate = new Date(Date.now() + 1000000000);
  tmpArray.push(statement);
}

export const genericFetchResponse = [...tmpArray];

let tmp: Statement = { ...genericStatement };
// type is an attribute for the the front display only and should be overriden by the back
tmp.statementType = StatementType.DEPOSIT;
export const depositStatement = tmp;
tmp = { ...genericStatement };
// type is an attribute for the the front display only and should be overriden by the back
tmp.statementType = StatementType.WITHDRAW;
export const withdrawStatement = tmp;

export class MockBankService extends SpyObject {
  getSpy: Spy;
  deposeMoneySpy: Spy;
  withdrawMoneySpy: Spy;
  fetchOperationsSpy: Spy;
  statements$Spy: Spy;

  constructor() {
    super(BankService);

    this.getSpy = this.spy('get').andReturn(this);
    this.deposeMoneySpy = this.spy('deposeMoney').andReturn(of(null));
    this.withdrawMoneySpy = this.spy('withdrawMoney').andReturn(of(null));
    this.fetchOperationsSpy = this.spy('fetchOperations');
    this.statements$Spy = this.spy('statements$').andReturn(of(null));
  }

  setDepositMoneyResponse(statement: Statement | null): void {
    this.deposeMoneySpy = this.spy('deposeMoney').andReturn(of(statement));
  }

  setWithdrawMoneyResponse(statement: Statement | null): void {
    this.withdrawMoneySpy = this.spy('withdrawMoney').andReturn(of(statement));
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

  setStatements$Response(statements: Array<Statement>): void {
    this.statements$Spy = this.spy('statements$').andReturn(of(of(statements)));
  }

  setFetchOperationsDoThrow(error: Error): void {
    const obs = new Observable(() => {
      throw throwError(error);
    });

    this.fetchOperationsSpy = this.spy('fetchOperations').andReturn(obs);
  }
}
