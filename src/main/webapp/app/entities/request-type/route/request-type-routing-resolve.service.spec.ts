jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRequestType, RequestType } from '../request-type.model';
import { RequestTypeService } from '../service/request-type.service';

import { RequestTypeRoutingResolveService } from './request-type-routing-resolve.service';

describe('RequestType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RequestTypeRoutingResolveService;
  let service: RequestTypeService;
  let resultRequestType: IRequestType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RequestTypeRoutingResolveService);
    service = TestBed.inject(RequestTypeService);
    resultRequestType = undefined;
  });

  describe('resolve', () => {
    it('should return IRequestType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRequestType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRequestType).toEqual({ id: 123 });
    });

    it('should return new IRequestType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRequestType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRequestType).toEqual(new RequestType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RequestType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRequestType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRequestType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
