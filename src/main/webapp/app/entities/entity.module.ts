import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'bank-account',
        loadChildren: () => import('./bank-account/bank-account.module').then(m => m.KataBankAccountModule),
      },
      {
        path: 'bank-statement',
        loadChildren: () => import('./bank-statement/bank-statement.module').then(m => m.KataBankStatementModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class KataEntityModule {}
