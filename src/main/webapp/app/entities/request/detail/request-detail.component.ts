import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRequest } from '../request.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-request-detail',
  templateUrl: './request-detail.component.html',
})
export class RequestDetailComponent implements OnInit {
  request: IRequest | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ request }) => {
      this.request = request;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
