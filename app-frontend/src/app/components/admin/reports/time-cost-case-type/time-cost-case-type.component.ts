import { Component, OnInit } from '@angular/core';
import { TemplateComponent } from '../../../commons/template/template.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CaseType } from '../../../../models/CasePhase.model';
import { ProjectService } from '../../../../services/project/project.service';
import { ReportService } from '../../../../services/report/report.service';

export interface Report4Dto {
  typeId: number;
  typeName: string;
  totalHours: number;
  totalInvested: number;
}


@Component({
  selector: 'app-time-cost-case-type',
  standalone: true,
  imports: [CommonModule, FormsModule, TemplateComponent],
  templateUrl: './time-cost-case-type.component.html',
  styleUrl: './time-cost-case-type.component.scss'
})
export class TimeCostCaseTypeComponent implements OnInit {
  report: Report4Dto[] = [];
  caseTypes: CaseType[] = [];
  selectedTypeId: number | undefined;

  constructor(
    private _reportService: ReportService,
    private _projectService: ProjectService
  ) {}

  ngOnInit(): void {
    this.loadCaseTypes();
    this.loadReport();
  }

  loadCaseTypes(): void {
    this._projectService.getAllCaseTypesWithPhases().subscribe({
      next: (data) => (this.caseTypes = data),
      error: (err) => console.error(err),
    });
  }

  loadReport(): void {
    this._reportService.getReport4(this.selectedTypeId).subscribe({
      next: (data) => (this.report = data),
      error: (err) => console.error(err),
    });
  }
}
