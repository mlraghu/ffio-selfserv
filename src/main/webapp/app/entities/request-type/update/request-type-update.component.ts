import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRequestType, RequestType } from '../request-type.model';
import { RequestTypeService } from '../service/request-type.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-request-type-update',
  templateUrl: './request-type-update.component.html',
})
export class RequestTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    requestTypeID: [null, []],
    requestTypeName: [],
    requestTypeInput: [null, [Validators.required]],
    approvalProcess: [null, [Validators.required]],
    automationProcess: [null, [Validators.required]],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected requestTypeService: RequestTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ requestType }) => {
      this.updateForm(requestType);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('selfServiceApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const requestType = this.createFromForm();
    if (requestType.id !== undefined) {
      this.subscribeToSaveResponse(this.requestTypeService.update(requestType));
    } else {
      this.subscribeToSaveResponse(this.requestTypeService.create(requestType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRequestType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(requestType: IRequestType): void {
    this.editForm.patchValue({
      id: requestType.id,
      requestTypeID: requestType.requestTypeID,
      requestTypeName: requestType.requestTypeName,
      requestTypeInput: requestType.requestTypeInput,
      approvalProcess: requestType.approvalProcess,
      automationProcess: requestType.automationProcess,
    });
  }

  protected createFromForm(): IRequestType {
    return {
      ...new RequestType(),
      id: this.editForm.get(['id'])!.value,
      requestTypeID: this.editForm.get(['requestTypeID'])!.value,
      requestTypeName: this.editForm.get(['requestTypeName'])!.value,
      requestTypeInput: this.editForm.get(['requestTypeInput'])!.value,
      approvalProcess: this.editForm.get(['approvalProcess'])!.value,
      automationProcess: this.editForm.get(['automationProcess'])!.value,
    };
  }
}
