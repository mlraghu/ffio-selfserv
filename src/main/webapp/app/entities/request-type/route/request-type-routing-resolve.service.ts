import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRequestType, RequestType } from '../request-type.model';
import { RequestTypeService } from '../service/request-type.service';

@Injectable({ providedIn: 'root' })
export class RequestTypeRoutingResolveService implements Resolve<IRequestType> {
  constructor(protected service: RequestTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRequestType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((requestType: HttpResponse<RequestType>) => {
          if (requestType.body) {
            return of(requestType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RequestType());
  }
}
