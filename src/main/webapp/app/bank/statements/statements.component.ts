import { Component, OnInit } from '@angular/core';

export interface Statement {
  amount: number;
  date: Date;
}

@Component({
  selector: 'jhi-statements',
  templateUrl: './statements.component.html',
  styleUrls: ['./statements.component.scss'],
})
export class StatementsComponent implements OnInit {
  statements: Array<Statement> = new Array<Statement>(
    {
      amount: 42,
      date: new Date(),
    },
    {
      amount: 123,
      date: new Date(213),
    },
    {
      amount: 23423,
      date: new Date(1000),
    },
    {
      amount: 63,
      date: new Date(),
    },
    {
      amount: -42,
      date: new Date(),
    },
    {
      amount: +42,
      date: new Date(),
    },
    {
      amount: 42,
      date: new Date(),
    }
  );
  constructor() {}

  ngOnInit(): void {}
}
