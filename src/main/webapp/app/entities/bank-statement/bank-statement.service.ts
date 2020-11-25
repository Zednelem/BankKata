import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBankStatement } from 'app/shared/model/bank-statement.model';

type EntityResponseType = HttpResponse<IBankStatement>;
type EntityArrayResponseType = HttpResponse<IBankStatement[]>;

@Injectable({ providedIn: 'root' })
export class BankStatementService {
  public resourceUrl = SERVER_API_URL + 'api/bank-statements';

  constructor(protected http: HttpClient) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBankStatement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBankStatement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(bankStatement: IBankStatement): IBankStatement {
    const copy: IBankStatement = Object.assign({}, bankStatement, {
      validatedDate:
        bankStatement.validatedDate && bankStatement.validatedDate.isValid() ? bankStatement.validatedDate.toJSON() : undefined,
      createdDate: bankStatement.createdDate && bankStatement.createdDate.isValid() ? bankStatement.createdDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.validatedDate = res.body.validatedDate ? moment(res.body.validatedDate) : undefined;
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((bankStatement: IBankStatement) => {
        bankStatement.validatedDate = bankStatement.validatedDate ? moment(bankStatement.validatedDate) : undefined;
        bankStatement.createdDate = bankStatement.createdDate ? moment(bankStatement.createdDate) : undefined;
      });
    }
    return res;
  }
}
