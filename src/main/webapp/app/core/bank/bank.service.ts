import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Statement } from 'app/core/bank/statement.model';

@Injectable({ providedIn: 'root' })
export class BankService {
  url = SERVER_API_URL + 'api/bank';
  depositEndpoint = 'actions/deposit-money';
  withdrawalEndpoint = 'actions/withdraw-money';
  fetchStatementsEndpoint = 'statements';

  statements: [Statement] | undefined;
  statements$: BehaviorSubject<[Statement]> = new BehaviorSubject<[Statement]>([]);

  constructor(private http: HttpClient) {}

  deposeMoney(statement: Statement): Observable<Statement> {
    return this.http.post<Statement>(`${this.url}/${this.depositEndpoint}`, statement);
  }

  withdrawMoney(statement: Statement): Observable<Statement> {
    return this.http.post<Statement>(`${this.url}/${this.withdrawalEndpoint}`, statement);
  }

  fetchOperations(): Observable<[Statement]> {
    return this.http.get<[Statement]>(`${this.url}/${this.fetchStatementsEndpoint}`);
  }
}
