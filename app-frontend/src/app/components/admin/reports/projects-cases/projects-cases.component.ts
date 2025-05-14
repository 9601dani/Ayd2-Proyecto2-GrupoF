import { Component, OnInit } from '@angular/core';
import { TemplateComponent } from '../../../commons/template/template.component';
import { ReportService } from '../../../../services/report/report.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

export interface Report1Dto {
  projectId: number;
  projectName: string;
  description: string;
  isEnabled: boolean;
  caseCount: number;
}

@Component({
  selector: 'app-projects-cases',
  standalone: true,
  imports: [TemplateComponent, CommonModule, FormsModule],
  templateUrl: './projects-cases.component.html',
  styleUrl: './projects-cases.component.scss',
})
export class ProjectsCasesComponent implements OnInit {
  report: Report1Dto[] = [];
  filter: boolean | null = null;

  constructor(private _reportService: ReportService) {}

  ngOnInit(): void {
    this.loadReport();
  }

  loadReport(): void {
    const params: any = {};
    if (this.filter !== null) {
      params.isEnabled = this.filter;
    }

    this._reportService.getReport1(params).subscribe({
      next: (data: any) => (this.report = data),
      error: (err:any) => console.error(err),
    });
  }
}
