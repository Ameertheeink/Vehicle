import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TyreService } from 'src/app/core/services/tyre.service';
import { TyreModalComponent } from 'src/app/shared/modal/tyre-modal/tyre-modal.component';
import { Tyre } from 'src/app/shared/models/tyre-service.model';
import { LoaderService } from 'src/app/shared/services/loader.service';

@Component({
  selector: 'app-tyre',
  templateUrl: './tyre.component.html',
  styleUrls: ['./tyre.component.css']
})
export class TyreComponent implements OnInit {

  tyre: Tyre | null = null;
  vehicleId!: number;

  constructor(
    private tyreService: TyreService,
    private route: ActivatedRoute,
    private toastr: ToastrService,
    private modalService: NgbModal,
    private loaderService: LoaderService
  ) { }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.vehicleId = Number(idParam);
    this.loadTyreService();
  }

  loadTyreService() {
    this.loaderService.show();
    this.tyreService.getByVehicleId(this.vehicleId).subscribe({
      next: (res) => {
        if (res.success && res.data.length > 0) {
          // Getting the first item as the "current" tyre state
          this.tyre = res.data[0];
        }
        this.loaderService.hide();
      },
      error: (err) => {
        console.error('Failed to load tyre service', err);
        this.loaderService.hide();
      }
    });
  }

  /**
   * Unified method to handle both Add and Edit
   * @param data - existing tyre data if editing, null if adding new
   */
  openTyreModal(data: Tyre | null = null) {
    const modalRef = this.modalService.open(TyreModalComponent, {
      size: 'lg',
      centered: true,
      backdrop: 'static'
    });

    // Pass data to the modal
    modalRef.componentInstance.tyreData = data;

    modalRef.result.then((result) => {
      if (result && result.data) {
        this.saveTyreService(result.data, result.file);
      }
    }).catch(() => {
      // Modal dismissed/cancelled
    });
  }

  saveTyreService(formData: any, file: File | null) {
    this.loaderService.show();

    // Prepare payload
    const payload: Tyre = {
      ...formData,
      vehicleId: this.vehicleId
    };

    // Determine if we are updating (PUT) or creating (POST)
    const request$ = this.tyre?.id 
      ? this.tyreService.updateTyreService(this.tyre.id, payload)
      : this.tyreService.createTyreService(payload);

    request$.subscribe({
      next: (res) => {
        if (res.success) {
          const newId = this.tyre?.id || res.data.id;
          
          if (file) {
            this.handleFileUpload(newId, file);
          } else {
            this.onSaveSuccess('Tyre information saved successfully!');
          }
        }
      },
      error: (err) => {
        this.toastr.error('Error saving tyre service');
        this.loaderService.hide();
      }
    });
  }

  private handleFileUpload(id: number, file: File) {
    const formData = new FormData();
    formData.append('file', file);

    this.tyreService.uploadBill(id, formData).subscribe({
      next: () => this.onSaveSuccess('Information and Bill uploaded successfully!'),
      error: () => {
        this.toastr.warning('Service saved, but bill upload failed.');
        this.onSaveSuccess(); // Still refresh data
      }
    });
  }

  private onSaveSuccess(message?: string) {
    if (message) this.toastr.success(message);
    this.loadTyreService(); // Refresh display
    this.loaderService.hide();
  }

  deleteTyre() {
    if (this.tyre?.id && confirm('Are you sure you want to delete this record?')) {
      // Assuming you'll add a delete method to your service later
      // this.tyreService.delete(this.tyre.id).subscribe(...)
      this.toastr.info('Delete functionality to be implemented');
    }
  }

  viewBill(tyre: any) {
  const fileUrl = 'http://localhost:8080/' + tyre.billPath; // Adjust based on your API response
  window.open(fileUrl, '_blank');
}
}