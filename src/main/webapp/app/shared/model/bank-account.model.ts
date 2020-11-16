export interface IBankAccount {
  id?: number;
  balance?: number;
  name?: string;
  userLogin?: string;
  userId?: number;
}

export class BankAccount implements IBankAccount {
  constructor(public id?: number, public balance?: number, public name?: string, public userLogin?: string, public userId?: number) {}
}
