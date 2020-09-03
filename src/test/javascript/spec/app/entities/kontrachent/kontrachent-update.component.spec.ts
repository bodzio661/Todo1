import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Todo1TestModule } from '../../../test.module';
import { KontrachentUpdateComponent } from 'app/entities/kontrachent/kontrachent-update.component';
import { KontrachentService } from 'app/entities/kontrachent/kontrachent.service';
import { Kontrachent } from 'app/shared/model/kontrachent.model';

describe('Component Tests', () => {
  describe('Kontrachent Management Update Component', () => {
    let comp: KontrachentUpdateComponent;
    let fixture: ComponentFixture<KontrachentUpdateComponent>;
    let service: KontrachentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Todo1TestModule],
        declarations: [KontrachentUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(KontrachentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(KontrachentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(KontrachentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Kontrachent(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Kontrachent();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
