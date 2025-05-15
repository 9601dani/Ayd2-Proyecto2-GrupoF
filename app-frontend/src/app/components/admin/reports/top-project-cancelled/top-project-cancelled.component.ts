import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';

export interface TopProjectByCancelledCasesDto {
  projectId: number;
  projectName: string;
  totalCancelledCases: number;
}

@Component({
  selector: 'app-top-project-cancelled',
  standalone: true,
  imports: [CommonModule, TemplateComponent],
  templateUrl: './top-project-cancelled.component.html',
  styleUrl: './top-project-cancelled.component.scss'
})
export class TopProjectCancelledComponent implements OnInit {
  report!: TopProjectByCancelledCasesDto;
  loaded = false;

  constructor(private _reportService: ReportService) {}

  ngOnInit(): void {
    this._reportService.getReport11().subscribe({
      next: (data) => {
        this.report = data;
        this.loaded = true;
      },
      error: (err) => console.error(err)
    });
  }
}
