import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { Statement, StatementType } from 'app/core/bank/statement.model';
import { newArray } from '@angular/compiler/src/util';

@Component({
  selector: 'jhi-statements',
  templateUrl: './statements.component.html',
  styleUrls: ['./statements.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class StatementsComponent implements OnInit {
  @Input()
  data: Statement[] | null | undefined = newArray(0);

  StatementType = StatementType;

  constructor() {}

  ngOnInit(): void {}
}
