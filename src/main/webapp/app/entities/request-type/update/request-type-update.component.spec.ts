jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RequestTypeService } from '../service/request-type.service';
import { IRequestType, RequestType } from '../request-type.model';

import { RequestTypeUpdateComponent } from './request-type-update.component';

describe('Component Tests', () => {
  describe('RequestType Management Update Component', () => {
    let comp: RequestTypeUpdateComponent;
    let fixture: ComponentFixture<RequestTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let requestTypeService: RequestTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RequestTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RequestTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RequestTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      requestTypeService = TestBed.inject(RequestTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const requestType: IRequestType = { id: 456 };

        activatedRoute.data = of({ requestType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(requestType));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RequestType>>();
        const requestType = { id: 123 };
        jest.spyOn(requestTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ requestType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: requestType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(requestTypeService.update).toHaveBeenCalledWith(requestType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RequestType>>();
        const requestType = new RequestType();
        jest.spyOn(requestTypeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ requestType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: requestType }));
        saveSubject.complete();

        // THEN
        expect(requestTypeService.create).toHaveBeenCalledWith(requestType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RequestType>>();
        const requestType = { id: 123 };
        jest.spyOn(requestTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ requestType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(requestTypeService.update).toHaveBeenCalledWith(requestType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
