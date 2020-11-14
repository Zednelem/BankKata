import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { Statement, StatementType } from 'app/core/bank/statement.model';

@Component({
  selector: 'jhi-statements',
  templateUrl: './statements.component.html',
  styleUrls: ['./statements.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class StatementsComponent implements OnInit {
  @Input()
  data: [Statement] | null | undefined;

  StatementType = StatementType;

  constructor() {}

  ngOnInit(): void {}
}
