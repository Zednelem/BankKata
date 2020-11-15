import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BankRoutingModule } from './bank-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { OperationComponent } from './operation/operation.component';
import { StatementsComponent } from './statements/statements.component';

@NgModule({
  declarations: [DashboardComponent, OperationComponent, StatementsComponent],
  imports: [CommonModule, BankRoutingModule, FormsModule, ReactiveFormsModule],
})
export class BankModule {}
