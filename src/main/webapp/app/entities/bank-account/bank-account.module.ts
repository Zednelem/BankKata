import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KataSharedModule } from 'app/shared/shared.module';
import { BankAccountComponent } from './bank-account.component';
import { BankAccountDetailComponent } from './bank-account-detail.component';
import { bankAccountRoute } from './bank-account.route';

@NgModule({
  imports: [KataSharedModule, RouterModule.forChild(bankAccountRoute)],
  declarations: [BankAccountComponent, BankAccountDetailComponent],
})
export class KataBankAccountModule {}
