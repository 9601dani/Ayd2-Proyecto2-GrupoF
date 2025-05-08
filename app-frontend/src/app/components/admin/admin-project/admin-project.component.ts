import { Component, OnInit } from '@angular/core';
import { TemplateComponent } from '../../commons/template/template.component';
import { CommonModule } from '@angular/common';
import { Project } from '../../commons/project/project.component';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../../services/project/project.service';

@Component({
  selector: 'app-admin-project',
  standalone: true,
  imports: [TemplateComponent, CommonModule],
  templateUrl: './admin-project.component.html',
  styleUrl: './admin-project.component.scss'
})
export class AdminProjectComponent implements OnInit{

  project: Project | undefined;
  projectId: number | undefined;

  constructor(private _activatedRoute: ActivatedRoute, private _projectService: ProjectService) {
    
  }

  ngOnInit(): void {
    this._activatedRoute.params.subscribe((params) => {
      if (params['id']) {
        this.projectId = +params['id'];
      }
    });
    
    if(this.projectId){
      this._projectService.getProjectById(this.projectId).subscribe({
        next: (value) => {
            this.project = value;
            
        },
      })
    }
  }

}
