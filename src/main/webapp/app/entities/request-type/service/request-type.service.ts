import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRequestType, getRequestTypeIdentifier } from '../request-type.model';

export type EntityResponseType = HttpResponse<IRequestType>;
export type EntityArrayResponseType = HttpResponse<IRequestType[]>;

@Injectable({ providedIn: 'root' })
export class RequestTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/request-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(requestType: IRequestType): Observable<EntityResponseType> {
    return this.http.post<IRequestType>(this.resourceUrl, requestType, { observe: 'response' });
  }

  update(requestType: IRequestType): Observable<EntityResponseType> {
    return this.http.put<IRequestType>(`${this.resourceUrl}/${getRequestTypeIdentifier(requestType) as number}`, requestType, {
      observe: 'response',
    });
  }

  partialUpdate(requestType: IRequestType): Observable<EntityResponseType> {
    return this.http.patch<IRequestType>(`${this.resourceUrl}/${getRequestTypeIdentifier(requestType) as number}`, requestType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRequestType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRequestType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRequestTypeToCollectionIfMissing(
    requestTypeCollection: IRequestType[],
    ...requestTypesToCheck: (IRequestType | null | undefined)[]
  ): IRequestType[] {
    const requestTypes: IRequestType[] = requestTypesToCheck.filter(isPresent);
    if (requestTypes.length > 0) {
      const requestTypeCollectionIdentifiers = requestTypeCollection.map(requestTypeItem => getRequestTypeIdentifier(requestTypeItem)!);
      const requestTypesToAdd = requestTypes.filter(requestTypeItem => {
        const requestTypeIdentifier = getRequestTypeIdentifier(requestTypeItem);
        if (requestTypeIdentifier == null || requestTypeCollectionIdentifiers.includes(requestTypeIdentifier)) {
          return false;
        }
        requestTypeCollectionIdentifiers.push(requestTypeIdentifier);
        return true;
      });
      return [...requestTypesToAdd, ...requestTypeCollection];
    }
    return requestTypeCollection;
  }
}
