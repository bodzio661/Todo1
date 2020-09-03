import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFaktura, Faktura } from 'app/shared/model/faktura.model';
import { FakturaService } from './faktura.service';
import { FakturaComponent } from './faktura.component';
import { FakturaDetailComponent } from './faktura-detail.component';
import { FakturaUpdateComponent } from './faktura-update.component';

@Injectable({ providedIn: 'root' })
export class FakturaResolve implements Resolve<IFaktura> {
  constructor(private service: FakturaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFaktura> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((faktura: HttpResponse<Faktura>) => {
          if (faktura.body) {
            return of(faktura.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Faktura());
  }
}

export const fakturaRoute: Routes = [
  {
    path: '',
    component: FakturaComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams,
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'todo1App.faktura.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FakturaDetailComponent,
    resolve: {
      faktura: FakturaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'todo1App.faktura.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FakturaUpdateComponent,
    resolve: {
      faktura: FakturaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'todo1App.faktura.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FakturaUpdateComponent,
    resolve: {
      faktura: FakturaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'todo1App.faktura.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
