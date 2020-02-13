import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Todo1TestModule } from '../../../test.module';
import { TodoDetailComponent } from 'app/entities/todo/todo-detail.component';
import { Todo } from 'app/shared/model/todo.model';

describe('Component Tests', () => {
  describe('Todo Management Detail Component', () => {
    let comp: TodoDetailComponent;
    let fixture: ComponentFixture<TodoDetailComponent>;
    const route = ({ data: of({ todo: new Todo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Todo1TestModule],
        declarations: [TodoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TodoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TodoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load todo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.todo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
