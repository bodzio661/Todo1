import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'todo',
        loadChildren: () => import('./todo/todo.module').then(m => m.Todo1TodoModule),
      },
      {
        path: 'faktura',
        loadChildren: () => import('./faktura/faktura.module').then(m => m.Todo1FakturaModule),
      },
      {
        path: 'kontrachent',
        loadChildren: () => import('./kontrachent/kontrachent.module').then(m => m.Todo1KontrachentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class Todo1EntityModule {}
