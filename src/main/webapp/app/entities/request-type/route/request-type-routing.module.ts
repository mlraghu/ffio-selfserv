import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RequestTypeComponent } from '../list/request-type.component';
import { RequestTypeDetailComponent } from '../detail/request-type-detail.component';
import { RequestTypeUpdateComponent } from '../update/request-type-update.component';
import { RequestTypeRoutingResolveService } from './request-type-routing-resolve.service';

const requestTypeRoute: Routes = [
  {
    path: '',
    component: RequestTypeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RequestTypeDetailComponent,
    resolve: {
      requestType: RequestTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RequestTypeUpdateComponent,
    resolve: {
      requestType: RequestTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RequestTypeUpdateComponent,
    resolve: {
      requestType: RequestTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(requestTypeRoute)],
  exports: [RouterModule],
})
export class RequestTypeRoutingModule {}
