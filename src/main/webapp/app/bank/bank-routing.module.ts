import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { Authority } from '../shared/constants/authority.constants';

const routes: Routes = [
  {
    path: 'bank/dashboard',
    component: DashboardComponent,
    data: {
      authorities: [Authority.USER],
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class BankRoutingModule {}
