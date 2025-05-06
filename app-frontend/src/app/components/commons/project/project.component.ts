import { Component, OnInit } from '@angular/core';
import { TemplateComponent } from '../template/template.component';
import { CommonModule } from '@angular/common';
import { CommonService } from '../../../services/commons/common.service';
import { ModalComponent } from '../modal/modal.component';
import { ProjectFormComponent } from '../project-form/project-form.component';
import { ProjectService } from '../../../services/project/project.service';
import { AlertService } from '../../../services/commons/alert.service';

export interface Project {
  id: number;
  name: string;
  description: string;
  isEnabled: boolean;
  fkUser: number;
}

@Component({
  selector: 'app-project',
  standalone: true,
  imports: [
    TemplateComponent,
    CommonModule,
    ModalComponent,
    ProjectFormComponent,
  ],
  templateUrl: './project.component.html',
  styleUrl: './project.component.scss',
})
export class ProjectComponent implements OnInit {
  projects: Project[] = [];

  actualAction: 'edit' | 'disable' | 'create'  | null = null;
  projectSelected: any = null;

  constructor(private _commonService: CommonService, private _projectService: ProjectService, private _alertService: AlertService) {}

  ngOnInit(): void {
    this.projects = [
      {
        id: 1,
        name: 'Sistema de Inventario',
        description: 'Aplicación web para gestionar productos y existencias.',
        isEnabled: true,
        fkUser: 1,
      },
      {
        id: 2,
        name: 'Plataforma de Cursos',
        description:
          'Sistema educativo para ofrecer y administrar cursos en línea.',
        isEnabled:false,
        fkUser: 2,
      },
      {
        id: 3,
        name: 'Dashboard de Ventas',
        description: 'Visualización de datos de ventas en tiempo real.',
        isEnabled: true,
        fkUser: 3,
      },
    ];
  }

  openModal(
    action: 'edit' | 'disable' | 'create',
    project: any = null
  ) {
    this.actualAction = action;
    this.projectSelected = project;
    this._commonService.emitActiveModal(true);
  }

  closeModal() {
    this._commonService.emitActiveModal(false);
    this.actualAction = null;
    this.projectSelected = null;
  }

  onRegister(data: any) {
    this._projectService.createProject(data).subscribe({
      next: (project) => {
        this.projects.push(project);
        this._alertService.success(
          "Registro exitoso",
          "Se registró el proyecto " + project.name + " exitosamente"
        );
      },
      error: (err) => {
        this._alertService.error(
          "Error al registrar",
          err?.error?.message || "Ocurrió un error inesperado"
        );
      },
      complete: () => {
        this.closeModal();
      }
    });
  }

  onEdit(userData: any) {
    console.log(userData);
    
  }

  deleteProject() {
    if (this.projectSelected) {
      
      console.log('eliminar...');
      
    }
  }
}
