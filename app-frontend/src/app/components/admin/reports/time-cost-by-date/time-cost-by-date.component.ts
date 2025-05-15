import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';

export interface Report5Dto {
  totalHours: number;
  totalInvested: number;
}


@Component({
  selector: 'app-time-cost-by-date',
  standalone: true,
  imports: [CommonModule, FormsModule, TemplateComponent],
  templateUrl: './time-cost-by-date.component.html',
  styleUrl: './time-cost-by-date.component.scss'
})
export class TimeCostByDateComponent {
  dateInit: string = '';
  dateEnd: string = '';
  report: Report5Dto | null = null;

  constructor(private _reportService: ReportService) {}

  loadReport(): void {
    const init = this.dateInit ? `${this.dateInit}T00:00:00` : undefined;
    const end = this.dateEnd ? `${this.dateEnd}T23:59:59` : undefined;

    this._reportService.getReport5(init, end).subscribe({
      next: (data) => (this.report = data),
      error: (err) => console.error(err)
    });
  }
}
