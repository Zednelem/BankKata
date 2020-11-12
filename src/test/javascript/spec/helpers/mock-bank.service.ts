import Spy = jasmine.Spy;
import { Observable, of, throwError } from 'rxjs';

import { SpyObject } from './spyobject';
import { BankService } from 'app/core/bank/bank.service';

export class MockBankService extends SpyObject {
  getSpy: Spy;
  deposeMoneySpy: Spy;

  constructor() {
    super(BankService);

    this.getSpy = this.spy('get').andReturn(this);
    this.deposeMoneySpy = this.spy('deposeMoney').andReturn(of(null));
  }

  setDepositMoneyResponse(idOperation: string | null): void {
    this.deposeMoneySpy = this.spy('deposeMoney').andReturn(of(of(idOperation)));
  }

  setDepositMoneyDoThrow(error: Error): void {
    const obs = new Observable(() => {
      throw throwError(error);
    });

    this.deposeMoneySpy = this.spy('deposeMoney').andReturn(obs);
  }
}
