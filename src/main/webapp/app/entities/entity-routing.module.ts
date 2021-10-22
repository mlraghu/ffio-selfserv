import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'request',
        data: { pageTitle: 'Requests' },
        loadChildren: () => import('./request/request.module').then(m => m.RequestModule),
      },
      {
        path: 'request-type',
        data: { pageTitle: 'RequestTypes' },
        loadChildren: () => import('./request-type/request-type.module').then(m => m.RequestTypeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
