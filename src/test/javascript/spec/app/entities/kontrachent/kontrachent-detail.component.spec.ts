import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Todo1TestModule } from '../../../test.module';
import { KontrachentDetailComponent } from 'app/entities/kontrachent/kontrachent-detail.component';
import { Kontrachent } from 'app/shared/model/kontrachent.model';

describe('Component Tests', () => {
  describe('Kontrachent Management Detail Component', () => {
    let comp: KontrachentDetailComponent;
    let fixture: ComponentFixture<KontrachentDetailComponent>;
    const route = ({ data: of({ kontrachent: new Kontrachent(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Todo1TestModule],
        declarations: [KontrachentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(KontrachentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(KontrachentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load kontrachent on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.kontrachent).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
