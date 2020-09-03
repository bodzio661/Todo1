import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Todo1TestModule } from '../../../test.module';
import { FakturaUpdateComponent } from 'app/entities/faktura/faktura-update.component';
import { FakturaService } from 'app/entities/faktura/faktura.service';
import { Faktura } from 'app/shared/model/faktura.model';

describe('Component Tests', () => {
  describe('Faktura Management Update Component', () => {
    let comp: FakturaUpdateComponent;
    let fixture: ComponentFixture<FakturaUpdateComponent>;
    let service: FakturaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Todo1TestModule],
        declarations: [FakturaUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FakturaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FakturaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FakturaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Faktura(123);
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
        const entity = new Faktura();
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
