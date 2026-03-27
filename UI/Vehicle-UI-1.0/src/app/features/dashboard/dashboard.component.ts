import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';

import { ReminderService } from 'src/app/core/services/reminder.service';
import { OilReminder } from 'src/app/shared/models/oil-reminder.model';
import { TyreReminder } from 'src/app/shared/models/tyre-reminder.model';
import { LoaderService } from 'src/app/shared/services/loader.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  oilReminder!: OilReminder;
  tyreReminder!: TyreReminder;

  constructor(
    private reminderService: ReminderService,
    private loaderService: LoaderService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  /**
   * Load Oil + Tyre reminders together
   */
  loadDashboardData() {

    this.loaderService.show();

    forkJoin({
      oil: this.reminderService.getOilTopReminder(),
      tyre: this.reminderService.getTyreServiceReminders()
    }).subscribe({

      next: (res) => {

        if (res.oil.success) {
          this.oilReminder = res.oil.data;
        }

        if (res.tyre.success) {
          this.tyreReminder = res.tyre.data;
        }

        this.loaderService.hide();
      },

      error: (err) => {
        console.error(err);
        this.loaderService.hide();
      }

    });
  }

  /**
   * Progress calculation for oil reminder
   */
  getProgress(): number {

    if (!this.oilReminder) return 0;

    const max = 5000; // later can come from API
    return ((max - this.oilReminder.remainingKm) / max) * 100;
  }

}