import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITodo, Todo } from 'app/shared/model/todo.model';
import { TodoService } from './todo.service';
import { TodoComponent } from './todo.component';
import { TodoDetailComponent } from './todo-detail.component';
import { TodoUpdateComponent } from './todo-update.component';

@Injectable({ providedIn: 'root' })
export class TodoResolve implements Resolve<ITodo> {
  constructor(private service: TodoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITodo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((todo: HttpResponse<Todo>) => {
          if (todo.body) {
            return of(todo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Todo());
  }
}

export const todoRoute: Routes = [
  {
    path: '',
    component: TodoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'todo1App.todo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TodoDetailComponent,
    resolve: {
      todo: TodoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'todo1App.todo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TodoUpdateComponent,
    resolve: {
      todo: TodoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'todo1App.todo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TodoUpdateComponent,
    resolve: {
      todo: TodoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'todo1App.todo.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
