import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { Subject } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { State } from 'app/bank/bank-state.service';

export interface OperationComponentWording {
  title: string;
  inputLabel: string;
  submitButton: string;
  successMessagePrefix: string;
  successMessageSuffix: string;
  loadingMessage: string;
  errorMessagePrefix: string;
  errorMessageSuffix: string;
}

@Component({
  selector: 'jhi-operation',
  templateUrl: './operation.component.html',
  styleUrls: ['./operation.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OperationComponent {
  destroy$: Subject<boolean> = new Subject<boolean>();

  @Input('state')
  set setState(newValue: State | null) {
    this.state = newValue ? newValue : State.INIT;
  }

  @Output()
  userHasValidated: EventEmitter<number> = new EventEmitter();

  @Input()
  wording: OperationComponentWording | undefined;

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

  run(): void {
    this.amount = this.formGroup.value.amount;
    this.userHasValidated.emit(this.amount);
  }
}
