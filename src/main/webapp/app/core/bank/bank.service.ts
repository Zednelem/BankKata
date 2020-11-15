import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Statement } from 'app/core/bank/statement.model';
import { tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class BankService {
  private readonly url = SERVER_API_URL + 'api/bank';
  private readonly depositEndpoint = 'actions/deposit-money';
  private readonly withdrawalEndpoint = 'actions/withdraw-money';
  private readonly fetchStatementsEndpoint = 'statements';

  private statements: Array<Statement> = new Array<Statement>();
  private readonly _statements: Subject<Statement[]> = new Subject<Statement[]>();
  readonly statements$ = this._statements.asObservable();

  constructor(private http: HttpClient) {}

  deposeMoney(statement: Statement): Observable<Statement> {
    return this.http.post<Statement>(`${this.url}/${this.depositEndpoint}`, statement).pipe(
      tap(newValue => {
        this.statements = [newValue, ...this.statements];
        this._statements.next(this.statements);
      })
    );
  }

  withdrawMoney(statement: Statement): Observable<Statement> {
    return this.http.post<Statement>(`${this.url}/${this.withdrawalEndpoint}`, statement).pipe(
      tap(newValue => {
        this.statements = [newValue, ...this.statements];
        this._statements.next(this.statements);
      })
    );
  }

  fetchOperations(): void {
    this.http.get<Statement[]>(`${this.url}/${this.fetchStatementsEndpoint}`).subscribe(statements => {
      this.statements = [...statements];
      this._statements.next(this.statements);
    });
  }
}
