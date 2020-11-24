import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBankStatement } from 'app/shared/model/bank-statement.model';

@Component({
  selector: 'jhi-bank-statement-detail',
  templateUrl: './bank-statement-detail.component.html',
})
export class BankStatementDetailComponent implements OnInit {
  bankStatement: IBankStatement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankStatement }) => (this.bankStatement = bankStatement));
  }

  previousState(): void {
    window.history.back();
  }
}
