import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Account } from 'app/core/user/account.model';
import { Subscription } from 'rxjs';
import { BankService } from 'app/core/bank/bank.service';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent implements OnInit {
  account: Account | null = null;
  authSubscription?: Subscription;

  // depositAmount = new FormControl('');

  formGroup: FormGroup;
  messageToDisplay = 'Message to display';
  depositAmountvalue: number;

  constructor(private accountService: AccountService, private formBuilder: FormBuilder, private bankService: BankService) {
    this.formGroup = this.formBuilder.group({
      depositAmount: ['101', [Validators.required, Validators.min(0)]],
    });
    this.depositAmountvalue = 0;
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  deposit(): void {
    const result: number = this.formGroup.value.depositAmount;
    this.messageToDisplay = 'deposing to the bank ...';
    this.bankService.deposeMoney(result).subscribe(
      value => (this.messageToDisplay = `Successfully deposed to the bank transactionId = ${value}`),
      value => (this.messageToDisplay = `Error: Could not transfert the money: ${JSON.stringify(value)}`)
    );
    this.depositAmountvalue = result;
  }
}
