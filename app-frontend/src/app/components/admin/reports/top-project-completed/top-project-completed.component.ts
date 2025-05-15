import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';

export interface TopProjectByCompletedCasesDto {
  projectId: number;
  projectName: string;
  totalCases: number;
}

@Component({
  selector: 'app-top-project-completed',
  standalone: true,
  imports: [CommonModule, TemplateComponent],
  templateUrl: './top-project-completed.component.html',
  styleUrl: './top-project-completed.component.scss'
})
export class TopProjectCompletedComponent implements OnInit {
  report!: TopProjectByCompletedCasesDto;
  loaded = false;

  constructor(private _reportService: ReportService) {}

  ngOnInit(): void {
    this._reportService.getReport10().subscribe({
      next: (data) => {
        this.report = data;
        this.loaded = true;
      },
      error: (err) => console.error(err)
    });
  }
}
