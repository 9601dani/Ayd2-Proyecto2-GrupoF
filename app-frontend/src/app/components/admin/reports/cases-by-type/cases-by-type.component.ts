import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CaseType } from '../../../../models/CasePhase.model';
import { ProjectService } from '../../../../services/project/project.service';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';

export interface CaseUserReportDto {
  caseId: number;
  caseName: string;
  description: string;
  caseTypeId: number;
  caseTypeName: string;
  createdAt: string;
  limitDate: string;
}


@Component({
  selector: 'app-cases-by-type',
  standalone: true,
  imports: [CommonModule, FormsModule, TemplateComponent],
  templateUrl: './cases-by-type.component.html',
  styleUrl: './cases-by-type.component.scss'
})
export class CasesByTypeComponent implements OnInit {
  caseTypes: CaseType[] = [];
  selectedTypeId: number | undefined;
  report: CaseUserReportDto[] = [];

  constructor(
    private _reportService: ReportService,
    private _projectService: ProjectService
  ) {}

  ngOnInit(): void {
    this.loadCaseTypes();
    this.loadReport(); // carga todo inicialmente
  }

  loadCaseTypes(): void {
    this._projectService.getAllCaseTypesWithPhases().subscribe({
      next: (data) => (this.caseTypes = data),
      error: (err) => console.error(err)
    });
  }

  loadReport(): void {
    this._reportService.getReport14(this.selectedTypeId).subscribe({
      next: (data) => (this.report = data),
      error: (err) => console.error(err)
    });
  }
}
