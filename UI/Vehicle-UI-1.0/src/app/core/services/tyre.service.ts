import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Tyre } from 'src/app/shared/models/tyre-service.model';

@Injectable({
  providedIn: 'root'
})
export class TyreService {

   private baseUrl = 'http://localhost:8080/api/tyre-services';

  constructor(private http:HttpClient) { }

  createTyreService(data:Tyre):Observable<any>{
    return this.http.post<any>(this.baseUrl, data);
  }

  getByVehicleId(vehicleId:number):Observable<any>{
    return this.http.get<any>(`${this.baseUrl}/vehicle/${vehicleId}`);
  }

  getAll():Observable<Tyre[]>{
    return this.http.get<Tyre[]>(this.baseUrl);
  }

  uploadBill(id: number, formData: FormData): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/${id}/bill`, formData);
  }

  downloadBill(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${id}/bill`, {
      responseType: 'blob'
    });
  }

   updateTyreService(id: number, data: Tyre): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, data);
  }
}
