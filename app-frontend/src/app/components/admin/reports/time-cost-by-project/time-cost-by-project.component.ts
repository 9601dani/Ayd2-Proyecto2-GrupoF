import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../../../services/report/report.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TemplateComponent } from '../../../commons/template/template.component';
import { ProjectService } from '../../../../services/project/project.service';

export interface Report2Dto {
  projectId: number;
  projectName: string;
  totalHours: number;
  totalInvested: number;
}


@Component({
  selector: 'app-time-cost-by-project',
  standalone: true,
  imports: [CommonModule, TemplateComponent, FormsModule],
  templateUrl: './time-cost-by-project.component.html',
  styleUrl: './time-cost-by-project.component.scss'
})
export class TimeCostByProjectComponent implements OnInit {
  report: Report2Dto[] = [];
  projectId: number | undefined;
  projects: any = []

  constructor(private _reportService: ReportService, private _projectService: ProjectService) {}

  ngOnInit(): void {
    this.loadReport();

    this._projectService.getAllProjects().subscribe({
      next: (value) => {
        this.projects = value;
        console.log(value);
        
      },
    })
  }

  loadReport(): void {
    this._reportService.getReport2(this.projectId).subscribe({
      next: (data) => (this.report = data),
      error: (err) => console.error(err),
    });
  }
}