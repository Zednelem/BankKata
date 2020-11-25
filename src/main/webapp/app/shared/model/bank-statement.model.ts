import { Moment } from 'moment';
import { StatementType } from 'app/shared/model/enumerations/statement-type.model';

export interface IBankStatement {
  id?: number;
  amount?: number;
  label?: string;
  validatedDate?: Moment;
  statementType?: StatementType;
  createdBy?: string;
  createdDate?: Moment;
  accountName?: string;
  accountId?: number;
}

export class BankStatement implements IBankStatement {
  constructor(
    public id?: number,
    public amount?: number,
    public label?: string,
    public validatedDate?: Moment,
    public statementType?: StatementType,
    public createdBy?: string,
    public createdDate?: Moment,
    public accountName?: string,
    public accountId?: number
  ) {}
}
