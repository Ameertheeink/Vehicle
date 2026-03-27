import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { VehicleRoutingModule } from './vehicle-routing.module';
import { VehicleListComponent } from './components/vehicle-list/vehicle-list.component';
import { VehicleDetailsComponent } from './components/vehicle-details/vehicle-details.component';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { OilServiceComponent } from './components/oil-service/oil-service.component';
import { TyreComponent } from './components/tyre/tyre/tyre.component';
import { TyreModalComponent } from 'src/app/shared/modal/tyre-modal/tyre-modal.component';

@NgModule({
  declarations: [
    VehicleListComponent,
    VehicleDetailsComponent,
    OilServiceComponent,
    TyreComponent,

  ],
  imports: [
    CommonModule,
    VehicleRoutingModule,ReactiveFormsModule,NgbModule
  ]
})
export class VehicleModule { }
