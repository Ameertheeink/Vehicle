import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { VehicleService } from 'src/app/core/services/vehicle.service';

import { Vehicle } from 'src/app/shared/models/vehicle.model';

import { Location } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { LoaderService } from 'src/app/shared/services/loader.service';
import { OilServiceService } from 'src/app/core/services/oil-service.service';
import { Oil } from 'src/app/shared/models/oil-service.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OilServiceModalComponent } from 'src/app/shared/modal/oil-service-modal/oil-service-modal.component';
import { DeleteConfirmComponent } from 'src/app/shared/modal/delete-confirm/delete-confirm.component';
import * as bootstrap from 'bootstrap';



// declare var bootstrap: any;

@Component({
  selector: 'app-vehicle-details',
  templateUrl: './vehicle-details.component.html',
  styleUrls: ['./vehicle-details.component.css'],
})
export class VehicleDetailsComponent implements OnInit {
deleteOil(arg0: number|undefined) {
throw new Error('Method not implemented.');
}
clearForm() {
throw new Error('Method not implemented.');
}
  @ViewChild('fileInput') fileInput!: ElementRef;

  vehicle: Vehicle | null = null;
  vehicleId!: number;
  isCollapsed = true;
 oil: Oil | null = null;
  oilForm!: FormGroup;
  oilModal: any;
 selectedOilId: number | null = null;
  images: string[] = [];
  selectedImageFile: File | null = null;

  loadingImages = false;
  deleteModal: any;
  showFileInput = true;


  constructor(
    private route: ActivatedRoute,
    private vehicleService: VehicleService,
    private location: Location,
    private toastr: ToastrService,
    private loaderService: LoaderService,
    private oilService: OilServiceService,
     private fb: FormBuilder,
     private modalService: NgbModal

  ) {}

  // ===============================
  // 🔹 INITIAL LOAD
  // ===============================
  ngOnInit(): void {
    this.vehicleId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadVehicle();
    this.loadImages();
    this.loadOilService();
     
  }

  // ===============================
  // 🔹 Load Vehicle Details
  // ===============================
  loadVehicle(): void {
    this.loaderService.show();
    this.vehicleService.getVehicleById(this.vehicleId).subscribe({
      next: (res) => {
        this.loaderService.hide();
        if (res.success) {
          this.vehicle = res.data;
        } else {
          this.toastr.warning(res.message);
        }
      },
      error: (err) => {
        this.loaderService.hide();
        console.error(err);
        this.toastr.error('Failed to load vehicle details');
      },
    });
  }

  // ===============================
  // 🔹 Load Vehicle Images
  // ===============================
  loadImages(): void {
    this.loaderService.show();
    this.loadingImages = true;

    this.vehicleService.getVehicleImages(this.vehicleId).subscribe({
      next: (res) => {
        this.loaderService.hide();
        if (res.success) {
          this.images = res.data || [];
        } else {
          this.toastr.warning(res.message);
        }
        this.loadingImages = false;
      },
      error: (err) => {
        this.loaderService.hide();
        console.error(err);
        this.toastr.error('Failed to load images');
        this.loadingImages = false;
      },
    });
  }
loadOilService(): void {
  this.loaderService.show();
  this.oilService.getByVehicleId(this.vehicleId).subscribe({
    next: (res) => {
      this.loaderService.hide();
      if (res.success && res.data && res.data.length > 0) {
        // Grab the first record from the array
        this.oil = res.data[0]; 
      } else {
        this.oil = null;
        this.toastr.info('No oil service records found');
      }
    },
    error: (err) => {
      this.loaderService.hide();
      console.error(err);
      this.toastr.error('Failed to load oil service details');
    },
  });
}

  // ===============================
  // 🔹 Image File Selection
  // ===============================
  onImageSelected(event: any): void {
    const file = event.target.files[0];
    if (!file) return;

    if (!file.type.startsWith('image/')) {
      this.toastr.error('Only image files allowed');
      return;
    }

    this.selectedImageFile = file;
  }

  // ===============================
  // 🔹 Upload Vehicle Image
  // ===============================
  uploadImage(): void {
    this.loaderService.show();
    if (!this.selectedImageFile) {
      this.loaderService.hide();
      this.toastr.warning('Please select an image');
      return;
    }

    this.vehicleService
      .uploadVehicleImage(this.vehicleId, this.selectedImageFile)
      .subscribe({
        next: (res) => {
          this.loaderService.hide();
          if (res.success) {
            this.toastr.success(res.message);

            // Reset file input
            this.selectedImageFile = null;
            // this.fileInput.nativeElement.value = '';

            this.showFileInput = false;
            setTimeout(() => {
              this.showFileInput = true;
            }, 0);

            // Reload images
            this.loadImages();

            setTimeout(() => {
  const carousel = document.getElementById('carouselExample');
  if (carousel) {
    const bsCarousel = bootstrap.Carousel.getInstance(carousel)
      || new bootstrap.Carousel(carousel);

    bsCarousel.to(0); // Go to upload slide
  }
}, 300);
          } else {
            this.toastr.warning(res.message);
          }
        },
        error: (err) => {
          this.loaderService.hide();
          console.error(err);
          this.toastr.error('Upload failed');
        },
      });
  }

  // ===============================
  // 🔹 Open Delete Confirmation Modal
  // ===============================
  // openDeleteModal(): void {
  //   const modalElement = document.getElementById('deleteModal');
  //   this.deleteModal = new bootstrap.Modal(modalElement);
  //   this.deleteModal.show();
  // }

  // ===============================
  // 🔹 Confirm Delete All Images
  // ===============================
  confirmDelete(): void {
    this.loaderService.show();
    this.vehicleService.deleteAllVehicleImages(this.vehicleId).subscribe({
      next: (res) => {
        this.loaderService.hide();
        if (res.success) {
          this.toastr.success(res.message);
          this.images = [];
          
        } else {
          this.toastr.warning(res.message);
        }
      },
      error: (err) => {
        this.loaderService.hide();
        console.error(err);
        this.toastr.error('Delete failed');
      },
    });
  }

  // ===============================
  // 🔹 Close Delete Modal
  // ===============================

openDeleteModal(): void {
   const modalRef = this.modalService.open(DeleteConfirmComponent, {
    centered: true,
    backdrop: 'static'
  });

  modalRef.componentInstance.message =
    'Are you sure you want to delete all images?';

  modalRef.result.then((result) => {
    if (result === 'yes') {
      this.confirmDelete(); // ✅ call your existing method
    }
  });
}
  // ===============================
  // 🔹 Download Document
  // ===============================
 downloadDoc(): void {
  if (!this.vehicle) {
    this.toastr.warning('Vehicle data not loaded');
    return;
  }

  this.vehicleService
    .downloadDocument(this.vehicle.vehicleNumber)
    .subscribe((blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'vehicle-document.pdf';
      a.click();
    });
}

  // ===============================
  // 🔹 Back Navigation
  // ===============================
  goBack(): void {
    this.location.back();
  }

  viewImage(img: string) {
    window.open('http://localhost:8080/uploads/' + img, '_blank');
  }

  viewOilBill(oil: any) {
  const fileUrl = 'http://localhost:8080/' + oil.serviceBillPath;
  window.open(fileUrl, '_blank');
}
openAddOilModal() {
  const modalRef = this.modalService.open(OilServiceModalComponent, {
    size: 'lg',
    centered: true,
    backdrop: 'static'
  });

  modalRef.componentInstance.vehicleId = this.vehicleId; // ✅ CORRECT

  modalRef.result.then((result) => {
    if (result) {
      this.handleSave(result);
    }
  });
}

openEditOilModal(oil: any, event?: Event) {

  // ✅ remove focus (fix warning)
  (event?.target as HTMLElement)?.blur();

  const modalRef = this.modalService.open(OilServiceModalComponent, {
    size: 'lg',
    centered: true,
    backdrop: 'static'
  });

  modalRef.componentInstance.vehicleId = this.vehicleId; // ✅ fixed
  modalRef.componentInstance.oilData = oil;

  modalRef.result.then((result) => {
    if (result) {
      this.handleSave(result);
    }
  });
}
handleSave(event: any) {
  this.loaderService.show();
  if (event.id) {
    this.oilService.updateOilService(event.id, event.data).subscribe({
      next: () => {
          if (event.file) {
          this.uploadBill(event.id, event.file);
        }
        this.loadOilService();
         this.loaderService.hide();
        this.toastr.success('Oil service updated successfully');
       
      },
      error: (err) => {
        console.error(err);
        this.loaderService.hide();
        this.toastr.error('Failed to update oil service');
      }
    });
  } else {
    this.oilService.createOilService(event.data).subscribe({
      next: () => {this.loadOilService();
         this.loaderService.hide();
        this.toastr.success('Oil service created successfully');
       
      }
,      error: (err) => {
        console.error(err);
        this.loaderService.hide();
        this.toastr.error('Failed to create oil service');
      }
    });
  }
}
uploadBill(id: number, file: File) {
  const formData = new FormData();
  formData.append('file', file);

  this.oilService.uploadBill(id, formData).subscribe({
    next: () => {
      this.toastr.success('Bill uploaded successfully');
    },
    error: (err) => {
      console.error(err);
      this.toastr.error('Failed to upload bill');
    }
  });
}
}