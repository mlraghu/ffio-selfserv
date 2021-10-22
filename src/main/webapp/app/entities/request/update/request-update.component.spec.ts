jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RequestService } from '../service/request.service';
import { IRequest, Request } from '../request.model';

import { RequestUpdateComponent } from './request-update.component';

describe('Component Tests', () => {
  describe('Request Management Update Component', () => {
    let comp: RequestUpdateComponent;
    let fixture: ComponentFixture<RequestUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let requestService: RequestService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RequestUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RequestUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RequestUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      requestService = TestBed.inject(RequestService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const request: IRequest = { id: 456 };

        activatedRoute.data = of({ request });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(request));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Request>>();
        const request = { id: 123 };
        jest.spyOn(requestService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ request });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: request }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(requestService.update).toHaveBeenCalledWith(request);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Request>>();
        const request = new Request();
        jest.spyOn(requestService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ request });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: request }));
        saveSubject.complete();

        // THEN
        expect(requestService.create).toHaveBeenCalledWith(request);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Request>>();
        const request = { id: 123 };
        jest.spyOn(requestService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ request });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(requestService.update).toHaveBeenCalledWith(request);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
