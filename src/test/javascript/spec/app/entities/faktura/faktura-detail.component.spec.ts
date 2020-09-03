import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Todo1TestModule } from '../../../test.module';
import { FakturaDetailComponent } from 'app/entities/faktura/faktura-detail.component';
import { Faktura } from 'app/shared/model/faktura.model';

describe('Component Tests', () => {
  describe('Faktura Management Detail Component', () => {
    let comp: FakturaDetailComponent;
    let fixture: ComponentFixture<FakturaDetailComponent>;
    const route = ({ data: of({ faktura: new Faktura(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Todo1TestModule],
        declarations: [FakturaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FakturaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FakturaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load faktura on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.faktura).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
