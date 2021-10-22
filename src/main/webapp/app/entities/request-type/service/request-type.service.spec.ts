import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRequestType, RequestType } from '../request-type.model';

import { RequestTypeService } from './request-type.service';

describe('RequestType Service', () => {
  let service: RequestTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IRequestType;
  let expectedResult: IRequestType | IRequestType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RequestTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      requestTypeID: 'AAAAAAA',
      requestTypeName: 'AAAAAAA',
      requestTypeInput: 'AAAAAAA',
      approvalProcess: 'AAAAAAA',
      automationProcess: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a RequestType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new RequestType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RequestType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestTypeID: 'BBBBBB',
          requestTypeName: 'BBBBBB',
          requestTypeInput: 'BBBBBB',
          approvalProcess: 'BBBBBB',
          automationProcess: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RequestType', () => {
      const patchObject = Object.assign(
        {
          approvalProcess: 'BBBBBB',
          automationProcess: 'BBBBBB',
        },
        new RequestType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RequestType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          requestTypeID: 'BBBBBB',
          requestTypeName: 'BBBBBB',
          requestTypeInput: 'BBBBBB',
          approvalProcess: 'BBBBBB',
          automationProcess: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a RequestType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRequestTypeToCollectionIfMissing', () => {
      it('should add a RequestType to an empty array', () => {
        const requestType: IRequestType = { id: 123 };
        expectedResult = service.addRequestTypeToCollectionIfMissing([], requestType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(requestType);
      });

      it('should not add a RequestType to an array that contains it', () => {
        const requestType: IRequestType = { id: 123 };
        const requestTypeCollection: IRequestType[] = [
          {
            ...requestType,
          },
          { id: 456 },
        ];
        expectedResult = service.addRequestTypeToCollectionIfMissing(requestTypeCollection, requestType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RequestType to an array that doesn't contain it", () => {
        const requestType: IRequestType = { id: 123 };
        const requestTypeCollection: IRequestType[] = [{ id: 456 }];
        expectedResult = service.addRequestTypeToCollectionIfMissing(requestTypeCollection, requestType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(requestType);
      });

      it('should add only unique RequestType to an array', () => {
        const requestTypeArray: IRequestType[] = [{ id: 123 }, { id: 456 }, { id: 97122 }];
        const requestTypeCollection: IRequestType[] = [{ id: 123 }];
        expectedResult = service.addRequestTypeToCollectionIfMissing(requestTypeCollection, ...requestTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const requestType: IRequestType = { id: 123 };
        const requestType2: IRequestType = { id: 456 };
        expectedResult = service.addRequestTypeToCollectionIfMissing([], requestType, requestType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(requestType);
        expect(expectedResult).toContain(requestType2);
      });

      it('should accept null and undefined values', () => {
        const requestType: IRequestType = { id: 123 };
        expectedResult = service.addRequestTypeToCollectionIfMissing([], null, requestType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(requestType);
      });

      it('should return initial array if no RequestType is added', () => {
        const requestTypeCollection: IRequestType[] = [{ id: 123 }];
        expectedResult = service.addRequestTypeToCollectionIfMissing(requestTypeCollection, undefined, null);
        expect(expectedResult).toEqual(requestTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
