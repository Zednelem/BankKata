export interface Statement {
  id?: any;
  createdBy: string;
  createdDate: Date;
  validatedDate?: Date;
  label: string;
  statementType: StatementType;
  amount: number;
}

export enum StatementType {
  DEPOSIT = 'DEPOSIT',
  WITHDRAW = 'WITHDRAW',
}

export class StatementModel {
  constructor(
    public createdBy: string,
    public createdDate: Date,
    public label: string,
    public statementType: StatementType,
    public amount: number
  ) {}
}
