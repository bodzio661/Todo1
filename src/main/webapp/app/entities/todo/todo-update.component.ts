import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITodo, Todo } from 'app/shared/model/todo.model';
import { TodoService } from './todo.service';

@Component({
  selector: 'jhi-todo-update',
  templateUrl: './todo-update.component.html'
})
export class TodoUpdateComponent implements OnInit {
  isSaving = false;
  doneDateDp: any;

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required]],
    username: [null, [Validators.required]],
    doneDate: [null, [Validators.required]],
    isDone: [null, [Validators.required]]
  });

  constructor(protected todoService: TodoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ todo }) => {
      this.updateForm(todo);
    });
  }

  updateForm(todo: ITodo): void {
    this.editForm.patchValue({
      id: todo.id,
      description: todo.description,
      username: todo.username,
      doneDate: todo.doneDate,
      isDone: todo.isDone
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const todo = this.createFromForm();
    if (todo.id !== undefined) {
      this.subscribeToSaveResponse(this.todoService.update(todo));
    } else {
      this.subscribeToSaveResponse(this.todoService.create(todo));
    }
  }

  private createFromForm(): ITodo {
    return {
      ...new Todo(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      username: this.editForm.get(['username'])!.value,
      doneDate: this.editForm.get(['doneDate'])!.value,
      isDone: this.editForm.get(['isDone'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITodo>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
