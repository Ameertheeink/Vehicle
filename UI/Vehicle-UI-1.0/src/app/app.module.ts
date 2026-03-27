import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { SidebarComponent } from './shared/components/sidebar/sidebar.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { LoadingBarModule } from '@ngx-loading-bar/core';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';
import { LoaderComponent } from './shared/loader/loader.component';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { UppercaseDirective } from './shared/directives/uppercase.directive';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { LayoutComponent } from './shared/components/layout/layout.component';
import { OilServiceModalComponent } from './shared/modal/oil-service-modal/oil-service-modal.component';
import { DeleteConfirmComponent } from './shared/modal/delete-confirm/delete-confirm.component';
import { TyreModalComponent } from './shared/modal/tyre-modal/tyre-modal.component';




@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    SidebarComponent,
    DashboardComponent,
    LoaderComponent,
    UppercaseDirective,
    LayoutComponent,
    OilServiceModalComponent,
    DeleteConfirmComponent,
    TyreModalComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    LoadingBarModule,
    LoadingBarRouterModule,HttpClientModule,ReactiveFormsModule, BrowserAnimationsModule,NgbModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-top-right',
      preventDuplicates: true
    })
    
    
  ],
  providers: [],
  bootstrap: [AppComponent],
  exports: [UppercaseDirective]
})
export class AppModule { }
