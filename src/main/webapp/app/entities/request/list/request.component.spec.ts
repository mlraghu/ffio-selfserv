import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RequestService } from '../service/request.service';

import { RequestComponent } from './request.component';

describe('Component Tests', () => {
  describe('Request Management Component', () => {
    let comp: RequestComponent;
    let fixture: ComponentFixture<RequestComponent>;
    let service: RequestService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RequestComponent],
      })
        .overrideTemplate(RequestComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RequestComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(RequestService);

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
      expect(comp.requests?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
