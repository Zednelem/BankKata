import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NgModule } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { JhiAlertService, JhiDataUtils, JhiDateUtils, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { AccountService } from 'app/core/auth/account.service';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { MockLoginModalService } from './helpers/mock-login-modal.service';
import { MockAccountService } from './helpers/mock-account.service';
import { MockActivatedRoute, MockRouter } from './helpers/mock-route.service';
import { MockActiveModal } from './helpers/mock-active-modal.service';
import { MockAlertService } from './helpers/mock-alert.service';
import { MockEventManager } from './helpers/mock-event-manager.service';
import { BankService } from 'app/core/bank/bank.service';
import { MockBankService } from './helpers/mock-bank.service';
import { TimeProviderService } from 'app/core/system/time-provider.service';
import { MockTimeProviderService } from './helpers/mock-time-provider.service';

@NgModule({
  providers: [
    DatePipe,
    JhiDataUtils,
    JhiDateUtils,
    JhiParseLinks,
    {
      provide: JhiEventManager,
      useClass: MockEventManager,
    },
    {
      provide: NgbActiveModal,
      useClass: MockActiveModal,
    },
    {
      provide: ActivatedRoute,
      useValue: new MockActivatedRoute({ id: 123 }),
    },
    {
      provide: Router,
      useClass: MockRouter,
    },
    {
      provide: AccountService,
      useClass: MockAccountService,
    },
    {
      provide: LoginModalService,
      useClass: MockLoginModalService,
    },
    {
      provide: JhiAlertService,
      useClass: MockAlertService,
    },
    {
      provide: NgbModal,
      useValue: null,
    },
    {
      provide: SessionStorageService,
      useValue: null,
    },
    {
      provide: LocalStorageService,
      useValue: null,
    },
    {
      provide: BankService,
      useClass: MockBankService,
    },
    {
      provide: TimeProviderService,
      useClass: MockTimeProviderService,
    },
  ],
  imports: [HttpClientTestingModule],
})
export class KataTestModule {}
