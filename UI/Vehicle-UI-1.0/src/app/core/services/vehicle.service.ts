import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehicle } from 'src/app/shared/models/vehicle.model';
import { ApiResponse } from 'src/app/shared/models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class VehicleService {

  private baseUrl = 'http://localhost:8080/api/vehicles';

  constructor(private http: HttpClient) {}

 getVehicles() {
  return this.http.get<ApiResponse<Vehicle[]>>(this.baseUrl);
}


  getVehicleById(id: number) {
  return this.http.get<ApiResponse<Vehicle>>(`${this.baseUrl}/${id}`);
}

// getVehicleImages(id: number) {
//   return this.http.get<ApiResponse<string[]>>(`${this.baseUrl}/${id}/images`);
// }

downloadDocument(vehicleNumber: string) {
  return this.http.get(
    `${this.baseUrl}/${vehicleNumber}/document`,
    { responseType: 'blob' }
  );
}

addVehicle(vehicle: Vehicle) {
  return this.http.post<Vehicle>(this.baseUrl, vehicle);
}
uploadDocument(vehicleNumber: string, file: File) {
  const formData = new FormData();
  formData.append('file', file);

  return this.http.post(
    `${this.baseUrl}/${vehicleNumber}/document`,
    formData
  );
}
deleteVehicle(id: number) {
  return this.http.delete<ApiResponse<any>>(`${this.baseUrl}/${id}`);
}

updateVehicle(id: number, vehicle: Vehicle) {
  return this.http.put<ApiResponse<any>>(`${this.baseUrl}/${id}`, vehicle);
}


// Get images by vehicle id
getVehicleImages(id: number) {
  return this.http.get<ApiResponse<string[]>>(
    `${this.baseUrl}/${id}/images`
  );
}


// Upload image
uploadVehicleImage(id: number, file: File) {
  const formData = new FormData();

  // 🔥 MUST match backend parameter name
  formData.append('files', file);

  return this.http.post<ApiResponse<any>>(
    `${this.baseUrl}/${id}/images`,
    formData
  );
}



// Delete all images
deleteAllVehicleImages(id: number) {
  return this.http.delete<ApiResponse<any>>(
    `${this.baseUrl}/${id}/images`
  );
}


}
