import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';

export interface Statement {
  id: number;
  amount: number;
  date: Date;
}

@Component({
  selector: 'jhi-statements',
  templateUrl: './statements.component.html',
  styleUrls: ['./statements.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class StatementsComponent implements OnInit {
  @Input()
  data: [Statement] | null | undefined;

  constructor() {}

  ngOnInit(): void {}
}
