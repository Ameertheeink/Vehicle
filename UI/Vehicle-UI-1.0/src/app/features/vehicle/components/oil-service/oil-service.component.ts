import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { OilServiceService } from 'src/app/core/services/oil-service.service';

@Component({
  selector: 'app-oil-service',
  templateUrl: './oil-service.component.html',
  styleUrls: ['./oil-service.component.css']
})
export class OilServiceComponent implements OnInit {
 @Input() vehicleId!: number;

  oilForm!: FormGroup;
  oilServices: any[] = [];
  selectedFile: File | null = null;

  constructor(
    private fb: FormBuilder,
    private oilService: OilServiceService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.loadOilServices();
  }

  initializeForm() {
    this.oilForm = this.fb.group({
      lastServiceKm: ['', Validators.required],
      serviceIntervalKm: ['', Validators.required],
      lastServiceDate: ['', Validators.required],
      oilBrand: ['', Validators.required],
      oilQuantityLitres: ['', Validators.required],
      serviceVendor: ['', Validators.required]
    });
  }

  createOilService() {
    if (this.oilForm.invalid) return;

    const payload = {
      vehicleId: this.vehicleId,
      ...this.oilForm.value
    };

    this.oilService.createOilService(payload).subscribe({
      next: (res) => {
        if (res.success) {
          this.toastr.success(res.message);
          this.oilForm.reset();
          this.loadOilServices();
        }
      },
      error: () => this.toastr.error('Failed to create oil service')
    });
  }

  loadOilServices() {
    this.oilService.getByVehicleId(this.vehicleId).subscribe({
      next: (res) => {
        if (res.success) {
          this.oilServices = res.data;
        }
      }
    });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

uploadBill(serviceId: number) {
  if (!this.selectedFile) return;

  const formData = new FormData();
  formData.append('file', this.selectedFile); // ✅ important

  this.oilService.uploadBill(serviceId, formData)
    .subscribe({
      next: () => {
        this.toastr.success('Bill uploaded');
        this.selectedFile = null;
        this.loadOilServices();
      },
      error: (err) => {
        console.error(err);
        this.toastr.error('Upload failed');
      }
    });
}

  downloadBill(serviceId: number) {
    this.oilService.downloadBill(serviceId)
      .subscribe(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'oil-service-bill';
        a.click();
      });
  }
}
