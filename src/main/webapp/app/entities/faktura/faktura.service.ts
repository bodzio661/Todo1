import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IFaktura } from 'app/shared/model/faktura.model';

type EntityResponseType = HttpResponse<IFaktura>;
type EntityArrayResponseType = HttpResponse<IFaktura[]>;

@Injectable({ providedIn: 'root' })
export class FakturaService {
  public resourceUrl = SERVER_API_URL + 'api/fakturas';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/fakturas';

  constructor(protected http: HttpClient) {}

  create(faktura: IFaktura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(faktura);
    return this.http
      .post<IFaktura>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(faktura: IFaktura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(faktura);
    return this.http
      .put<IFaktura>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFaktura>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFaktura[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFaktura[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(faktura: IFaktura): IFaktura {
    const copy: IFaktura = Object.assign({}, faktura, {
      dataFaktury: faktura.dataFaktury && faktura.dataFaktury.isValid() ? faktura.dataFaktury.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataFaktury = res.body.dataFaktury ? moment(res.body.dataFaktury) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((faktura: IFaktura) => {
        faktura.dataFaktury = faktura.dataFaktury ? moment(faktura.dataFaktury) : undefined;
      });
    }
    return res;
  }
}
