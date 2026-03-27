import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiResponse } from 'src/app/shared/models/api-response.model';
import { OilReminder } from 'src/app/shared/models/oil-reminder.model';

@Injectable({
  providedIn: 'root'
})
export class ReminderService {
 private baseUrl = 'http://localhost:8080/api';
  constructor(private http:HttpClient) { }

    getOilTopReminder() {
    return this.http.get<ApiResponse<OilReminder>>(
      `${this.baseUrl}/oil-services/reminders/top`
    );

  

    
  }

  getTyreServiceReminders() {
    return this.http.get<ApiResponse<any>>(
      `${this.baseUrl}/tyre-services/reminders/top`
    );
  }
}
