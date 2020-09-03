import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IKontrachent } from 'app/shared/model/kontrachent.model';

type EntityResponseType = HttpResponse<IKontrachent>;
type EntityArrayResponseType = HttpResponse<IKontrachent[]>;

@Injectable({ providedIn: 'root' })
export class KontrachentService {
  public resourceUrl = SERVER_API_URL + 'api/kontrachents';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/kontrachents';

  constructor(protected http: HttpClient) {}

  create(kontrachent: IKontrachent): Observable<EntityResponseType> {
    return this.http.post<IKontrachent>(this.resourceUrl, kontrachent, { observe: 'response' });
  }

  update(kontrachent: IKontrachent): Observable<EntityResponseType> {
    return this.http.put<IKontrachent>(this.resourceUrl, kontrachent, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IKontrachent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKontrachent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKontrachent[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
