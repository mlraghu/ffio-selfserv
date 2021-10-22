import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRequest, Request } from '../request.model';
import { RequestService } from '../service/request.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { RequestStatus } from 'app/entities/enumerations/request-status.model';

@Component({
  selector: 'jhi-request-update',
  templateUrl: './request-update.component.html',
})
export class RequestUpdateComponent implements OnInit {
  isSaving = false;
  requestStatusValues = Object.keys(RequestStatus);

  editForm = this.fb.group({
    id: [],
    requestID: [null, []],
    requestType: [],
    projectInfo: [null, [Validators.required]],
    costCode: [null, [Validators.required]],
    environments: [null, [Validators.required, Validators.min(1)]],
    configInput: [null, [Validators.required]],
    status: [null, [Validators.required]],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected requestService: RequestService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ request }) => {
      this.updateForm(request);
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
    const request = this.createFromForm();
    if (request.id !== undefined) {
      this.subscribeToSaveResponse(this.requestService.update(request));
    } else {
      this.subscribeToSaveResponse(this.requestService.create(request));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRequest>>): void {
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

  protected updateForm(request: IRequest): void {
    this.editForm.patchValue({
      id: request.id,
      requestID: request.requestID,
      requestType: request.requestType,
      projectInfo: request.projectInfo,
      costCode: request.costCode,
      environments: request.environments,
      configInput: request.configInput,
      status: request.status,
    });
  }

  protected createFromForm(): IRequest {
    return {
      ...new Request(),
      id: this.editForm.get(['id'])!.value,
      requestID: this.editForm.get(['requestID'])!.value,
      requestType: this.editForm.get(['requestType'])!.value,
      projectInfo: this.editForm.get(['projectInfo'])!.value,
      costCode: this.editForm.get(['costCode'])!.value,
      environments: this.editForm.get(['environments'])!.value,
      configInput: this.editForm.get(['configInput'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
