import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Todo1TestModule } from '../../../test.module';
import { TodoUpdateComponent } from 'app/entities/todo/todo-update.component';
import { TodoService } from 'app/entities/todo/todo.service';
import { Todo } from 'app/shared/model/todo.model';

describe('Component Tests', () => {
  describe('Todo Management Update Component', () => {
    let comp: TodoUpdateComponent;
    let fixture: ComponentFixture<TodoUpdateComponent>;
    let service: TodoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Todo1TestModule],
        declarations: [TodoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TodoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TodoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TodoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Todo(123);
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
        const entity = new Todo();
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
