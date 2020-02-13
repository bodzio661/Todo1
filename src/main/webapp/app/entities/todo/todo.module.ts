import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Todo1SharedModule } from 'app/shared/shared.module';
import { TodoComponent } from './todo.component';
import { TodoDetailComponent } from './todo-detail.component';
import { TodoUpdateComponent } from './todo-update.component';
import { TodoDeleteDialogComponent } from './todo-delete-dialog.component';
import { todoRoute } from './todo.route';

@NgModule({
  imports: [Todo1SharedModule, RouterModule.forChild(todoRoute)],
  declarations: [TodoComponent, TodoDetailComponent, TodoUpdateComponent, TodoDeleteDialogComponent],
  entryComponents: [TodoDeleteDialogComponent]
})
export class Todo1TodoModule {}
