import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IKontrachent, Kontrachent } from 'app/shared/model/kontrachent.model';
import { KontrachentService } from './kontrachent.service';
import { KontrachentComponent } from './kontrachent.component';
import { KontrachentDetailComponent } from './kontrachent-detail.component';
import { KontrachentUpdateComponent } from './kontrachent-update.component';

@Injectable({ providedIn: 'root' })
export class KontrachentResolve implements Resolve<IKontrachent> {
  constructor(private service: KontrachentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKontrachent> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((kontrachent: HttpResponse<Kontrachent>) => {
          if (kontrachent.body) {
            return of(kontrachent.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Kontrachent());
  }
}

export const kontrachentRoute: Routes = [
  {
    path: '',
    component: KontrachentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams,
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'todo1App.kontrachent.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KontrachentDetailComponent,
    resolve: {
      kontrachent: KontrachentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'todo1App.kontrachent.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KontrachentUpdateComponent,
    resolve: {
      kontrachent: KontrachentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'todo1App.kontrachent.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KontrachentUpdateComponent,
    resolve: {
      kontrachent: KontrachentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'todo1App.kontrachent.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
