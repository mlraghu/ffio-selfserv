import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RequestTypeService } from '../service/request-type.service';

import { RequestTypeComponent } from './request-type.component';

describe('Component Tests', () => {
  describe('RequestType Management Component', () => {
    let comp: RequestTypeComponent;
    let fixture: ComponentFixture<RequestTypeComponent>;
    let service: RequestTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RequestTypeComponent],
      })
        .overrideTemplate(RequestTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RequestTypeComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(RequestTypeService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.requestTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
