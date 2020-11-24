import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBankStatement, BankStatement } from 'app/shared/model/bank-statement.model';
import { BankStatementService } from './bank-statement.service';
import { BankStatementComponent } from './bank-statement.component';
import { BankStatementDetailComponent } from './bank-statement-detail.component';

@Injectable({ providedIn: 'root' })
export class BankStatementResolve implements Resolve<IBankStatement> {
  constructor(private service: BankStatementService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBankStatement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((bankStatement: HttpResponse<BankStatement>) => {
          if (bankStatement.body) {
            return of(bankStatement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BankStatement());
  }
}

export const bankStatementRoute: Routes = [
  {
    path: '',
    component: BankStatementComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'BankStatements',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BankStatementDetailComponent,
    resolve: {
      bankStatement: BankStatementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'BankStatements',
    },
    canActivate: [UserRouteAccessService],
  },
];
