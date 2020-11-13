import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Subject } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { State } from 'app/bank/dashboard/dashboard.component';

@Component({
  selector: 'jhi-operation',
  templateUrl: './operation.component.html',
  styleUrls: ['./operation.component.scss'],
  //changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OperationComponent implements OnInit, OnChanges {
  destroy$: Subject<boolean> = new Subject<boolean>();

  @Input('state')
  set setState(newValue: State | null) {
    this.state = newValue ? newValue : State.INIT;
  }

  @Output()
  userHasValidated: EventEmitter<number> = new EventEmitter();

  state: State = State.INIT;

  formGroup: FormGroup;
  amount: number;

  States = State;

  constructor(private formBuilder: FormBuilder) {
    this.formGroup = this.formBuilder.group({
      amount: ['0.00', [Validators.required, Validators.min(0)]],
    });
    this.amount = 0;
  }

  ngOnInit(): void {}

  ngOnChanges(changes: SimpleChanges): void {
    //alert(changes);
  }

  run(): void {
    this.amount = this.formGroup.value.amount;
    this.userHasValidated.emit(this.amount);
  }
}
