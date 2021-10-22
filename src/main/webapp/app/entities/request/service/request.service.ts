import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRequest, getRequestIdentifier } from '../request.model';

export type EntityResponseType = HttpResponse<IRequest>;
export type EntityArrayResponseType = HttpResponse<IRequest[]>;

@Injectable({ providedIn: 'root' })
export class RequestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/requests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(request: IRequest): Observable<EntityResponseType> {
    return this.http.post<IRequest>(this.resourceUrl, request, { observe: 'response' });
  }

  update(request: IRequest): Observable<EntityResponseType> {
    return this.http.put<IRequest>(`${this.resourceUrl}/${getRequestIdentifier(request) as number}`, request, { observe: 'response' });
  }

  partialUpdate(request: IRequest): Observable<EntityResponseType> {
    return this.http.patch<IRequest>(`${this.resourceUrl}/${getRequestIdentifier(request) as number}`, request, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRequest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRequestToCollectionIfMissing(requestCollection: IRequest[], ...requestsToCheck: (IRequest | null | undefined)[]): IRequest[] {
    const requests: IRequest[] = requestsToCheck.filter(isPresent);
    if (requests.length > 0) {
      const requestCollectionIdentifiers = requestCollection.map(requestItem => getRequestIdentifier(requestItem)!);
      const requestsToAdd = requests.filter(requestItem => {
        const requestIdentifier = getRequestIdentifier(requestItem);
        if (requestIdentifier == null || requestCollectionIdentifiers.includes(requestIdentifier)) {
          return false;
        }
        requestCollectionIdentifiers.push(requestIdentifier);
        return true;
      });
      return [...requestsToAdd, ...requestCollection];
    }
    return requestCollection;
  }
}
