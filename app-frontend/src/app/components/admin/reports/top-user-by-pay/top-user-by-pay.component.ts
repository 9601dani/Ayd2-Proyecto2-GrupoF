import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';

export interface Report9Dto {
  userId: number;
  userName: string;
  salaryPerHour: number;
  totalHours: number;
  totalInvested: number;
}


@Component({
  selector: 'app-top-user-by-pay',
  standalone: true,
  imports: [CommonModule, TemplateComponent],
  templateUrl: './top-user-by-pay.component.html',
  styleUrl: './top-user-by-pay.component.scss'
})
export class TopUserByPayComponent implements OnInit {
  userReport!: Report9Dto;
  loaded = false;

  constructor(private _reportService: ReportService) {}

  ngOnInit(): void {
    this._reportService.getReport9().subscribe({
      next: (data) => {
        this.userReport = data;
        this.loaded = true;
      },
      error: (err) => console.error(err)
    });
  }
}
