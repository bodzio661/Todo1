import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITodo } from 'app/shared/model/todo.model';
import { TodoService } from './todo.service';

@Component({
  templateUrl: './todo-delete-dialog.component.html'
})
export class TodoDeleteDialogComponent {
  todo?: ITodo;

  constructor(protected todoService: TodoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.todoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('todoListModification');
      this.activeModal.close();
    });
  }
}
