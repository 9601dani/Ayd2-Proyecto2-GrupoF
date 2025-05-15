import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';

export interface ProjectResponseWithoutUser {
  id: number;
  name: string;
  description: string;
  isEnabled: boolean;
  fkUser: number;
}


@Component({
  selector: 'app-projects-report',
  standalone: true,
  imports: [CommonModule, TemplateComponent, FormsModule],
  templateUrl: './projects-report.component.html',
  styleUrl: './projects-report.component.scss'
})
export class ProjectsReportComponent implements OnInit {
  allProjects: ProjectResponseWithoutUser[] = [];
  filteredProjects: ProjectResponseWithoutUser[] = [];

  search: string = '';
  stateFilter: boolean | null = null;

  constructor(private _reportService: ReportService) {}

  ngOnInit(): void {
    this._reportService.getReport7().subscribe({
      next: (data) => {
        this.allProjects = data;
        this.applyFilters();
      },
      error: (err) => console.error(err),
    });
  }

  applyFilters(): void {
    this.filteredProjects = this.allProjects.filter((p) => {
      const matchState = this.stateFilter === null || p.isEnabled === this.stateFilter;
      const matchSearch =
        this.search.trim() === '' ||
        p.name.toLowerCase().includes(this.search.toLowerCase()) ||
        p.description.toLowerCase().includes(this.search.toLowerCase());

      return matchState && matchSearch;
    });
  }
}
