import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KataSharedModule } from 'app/shared/shared.module';
import { BankStatementComponent } from './bank-statement.component';
import { BankStatementDetailComponent } from './bank-statement-detail.component';
import { bankStatementRoute } from './bank-statement.route';

@NgModule({
  imports: [KataSharedModule, RouterModule.forChild(bankStatementRoute)],
  declarations: [BankStatementComponent, BankStatementDetailComponent],
})
export class KataBankStatementModule {}
