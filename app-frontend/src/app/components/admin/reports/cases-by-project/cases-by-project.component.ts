import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProjectService } from '../../../../services/project/project.service';
import { TemplateComponent } from '../../../commons/template/template.component';

@Component({
  selector: 'app-cases-by-project',
  standalone: true,
  imports: [CommonModule, FormsModule, TemplateComponent],
  templateUrl: './cases-by-project.component.html',
  styleUrl: './cases-by-project.component.scss'
})
export class CasesByProjectComponent implements OnInit {
  projects: any[] = [];
  cases: any[] = [];
  selectedProjectId: number | null = null;

  constructor(private _projectService: ProjectService) {}

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this._projectService.getAllProjects().subscribe({
      next: (data) => (this.projects = data),
      error: (err) => console.error(err)
    });
  }

  loadCases(): void {
    if (this.selectedProjectId !== null) {
      this._projectService.getCasesByFkProject(this.selectedProjectId).subscribe({
        next: (data) => (this.cases = data),
        error: (err) => console.error(err)
      });
    }
  }
}
