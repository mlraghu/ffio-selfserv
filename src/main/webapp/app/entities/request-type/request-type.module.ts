import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RequestTypeComponent } from './list/request-type.component';
import { RequestTypeDetailComponent } from './detail/request-type-detail.component';
import { RequestTypeUpdateComponent } from './update/request-type-update.component';
import { RequestTypeDeleteDialogComponent } from './delete/request-type-delete-dialog.component';
import { RequestTypeRoutingModule } from './route/request-type-routing.module';

@NgModule({
  imports: [SharedModule, RequestTypeRoutingModule],
  declarations: [RequestTypeComponent, RequestTypeDetailComponent, RequestTypeUpdateComponent, RequestTypeDeleteDialogComponent],
  entryComponents: [RequestTypeDeleteDialogComponent],
})
export class RequestTypeModule {}
