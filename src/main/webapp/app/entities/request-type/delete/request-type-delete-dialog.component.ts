import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRequestType } from '../request-type.model';
import { RequestTypeService } from '../service/request-type.service';

@Component({
  templateUrl: './request-type-delete-dialog.component.html',
})
export class RequestTypeDeleteDialogComponent {
  requestType?: IRequestType;

  constructor(protected requestTypeService: RequestTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.requestTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
