import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRequest } from '../request.model';
import { RequestService } from '../service/request.service';
import { RequestDeleteDialogComponent } from '../delete/request-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-request',
  templateUrl: './request.component.html',
})
export class RequestComponent implements OnInit {
  requests?: IRequest[];
  isLoading = false;

  constructor(protected requestService: RequestService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.requestService.query().subscribe(
      (res: HttpResponse<IRequest[]>) => {
        this.isLoading = false;
        this.requests = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRequest): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(request: IRequest): void {
    const modalRef = this.modalService.open(RequestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.request = request;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
