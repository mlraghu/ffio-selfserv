import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRequestType } from '../request-type.model';
import { RequestTypeService } from '../service/request-type.service';
import { RequestTypeDeleteDialogComponent } from '../delete/request-type-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-request-type',
  templateUrl: './request-type.component.html',
})
export class RequestTypeComponent implements OnInit {
  requestTypes?: IRequestType[];
  isLoading = false;

  constructor(protected requestTypeService: RequestTypeService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.requestTypeService.query().subscribe(
      (res: HttpResponse<IRequestType[]>) => {
        this.isLoading = false;
        this.requestTypes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRequestType): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(requestType: IRequestType): void {
    const modalRef = this.modalService.open(RequestTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.requestType = requestType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
