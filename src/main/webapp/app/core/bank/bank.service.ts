import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Statement } from 'app/bank/statements/statements.component';

@Injectable({ providedIn: 'root' })
export class BankService {
  url = SERVER_API_URL + 'api/bank';
  depositEndpoint = 'deposit-money';
  withdrawalEndpoint = 'withdraw-money';
  fetchStatementsEndpoint = 'fetch-statements';
  constructor(private http: HttpClient) {}

  deposeMoney(amount: number): Observable<string> {
    return this.http.post<string>(`${this.url}/${this.depositEndpoint}`, `${amount}`);
  }

  withdrawMoney(amount: number): Observable<string> {
    return this.http.post<string>(`${this.url}/${this.withdrawalEndpoint}`, `${amount}`);
  }

  fetchOperations(): Observable<[Statement]> {
    return this.http.get<[Statement]>(`${this.url}/${this.fetchStatementsEndpoint}`);
  }
}
