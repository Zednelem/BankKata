import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KataTestModule } from '../../../test.module';
import { BankStatementDetailComponent } from 'app/entities/bank-statement/bank-statement-detail.component';
import { BankStatement } from 'app/shared/model/bank-statement.model';

describe('Component Tests', () => {
  describe('BankStatement Management Detail Component', () => {
    let comp: BankStatementDetailComponent;
    let fixture: ComponentFixture<BankStatementDetailComponent>;
    const route = ({ data: of({ bankStatement: new BankStatement(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KataTestModule],
        declarations: [BankStatementDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(BankStatementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BankStatementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load bankStatement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bankStatement).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
