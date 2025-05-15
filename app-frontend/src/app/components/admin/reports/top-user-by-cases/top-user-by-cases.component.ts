import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';

export interface Report8Dto {
  userId: number;
  userName: string;
  salaryPerHour: number;
  cases: number;
}


@Component({
  selector: 'app-top-user-by-cases',
  standalone: true,
  imports: [CommonModule, TemplateComponent],
  templateUrl: './top-user-by-cases.component.html',
  styleUrl: './top-user-by-cases.component.scss'
})
export class TopUserByCasesComponent implements OnInit {
  userReport!: Report8Dto;
  loaded = false;

  constructor(private _reportService: ReportService) {}

  ngOnInit(): void {
    this._reportService.getReport8().subscribe({
      next: (data) => {
        this.userReport = data;
        this.loaded = true;
      },
      error: (err) => console.error(err)
    });
  }
}
