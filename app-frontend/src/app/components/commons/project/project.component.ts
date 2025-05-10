import { Component, OnInit } from '@angular/core';
import { TemplateComponent } from '../template/template.component';
import { CommonModule } from '@angular/common';
import { CommonService } from '../../../services/commons/common.service';
import { ModalComponent } from '../modal/modal.component';
import { ProjectFormComponent } from '../project-form/project-form.component';
import { ProjectService } from '../../../services/project/project.service';
import { AlertService } from '../../../services/commons/alert.service';
import { LocalStorageService } from '../../../services/commons/local-storage.service';
import { SourceTextModule } from 'vm';
import { Router } from '@angular/router';

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
  myProjects: Project[] = [];
  userId: number | undefined;

  actualAction: 'edit' | 'disable' | 'create' | null = null;
  projectSelected: any = null;

  constructor(
    private _commonService: CommonService,
    private _projectService: ProjectService,
    private _alertService: AlertService,
    private _localStorageService: LocalStorageService,
    private _router: Router
  ) {}

  ngOnInit(): void {
    this.userId = this._localStorageService.getItem(
      this._localStorageService.USER_ID
    );

    this._projectService.getAllProjects().subscribe({
      next: (value: any) => {
        this.projects = value.filter(
          (project: { isEnabled: any }) => project.isEnabled
        );

        this.myProjects = value.filter(
          (project: Project) =>
            project.isEnabled && project.fkUser === this.userId
        );
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  openModal(action: 'edit' | 'disable' | 'create', project: any = null) {
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
        this.updateMyProjects();

        this._alertService.success(
          'Registro exitoso',
          'Se registró el proyecto ' + project.name + ' exitosamente'
        );
      },
      error: (err) => {
        this._alertService.error(
          'Error al registrar',
          err?.error?.message || 'Ocurrió un error inesperado'
        );
      },
      complete: () => {
        this.closeModal();
      },
    });
  }

  onEdit(data: any) {
    const projectId = this.projectSelected.id;
    data.id = projectId;

    this._projectService.updateProject(data).subscribe({
      next: (project) => {
        const index = this.projects.findIndex((u) => u.id === project.id);
        if (index !== -1) {
          this.projects[index] = project;
          this.updateMyProjects();
        }
        this._alertService.success(
          'Actualización exitosa',
          'Se actualizó el proyecto ' + project.name + ' exitosamente'
        );
      },
      error: (err) => {
        this._alertService.error(
          'Error al actualizar',
          err?.error?.message || 'Ocurrió un error inesperado'
        );
      },
      complete: () => {
        this.closeModal();
      },
    });
  }

  deleteProject() {
    if (this.projectSelected) {
      const body = {
        id: this.projectSelected.id,
        enable: false,
      };

      this._projectService.updateIsEnable(body).subscribe({
        next: (value) => {
          const index = this.projects.findIndex(
            (u) => u.id === this.projectSelected.id
          );

          if (index !== -1) {
            this.projects.splice(index, 1);
            this.updateMyProjects();
          }
          this._alertService.success(
            'Proyecto Deshabilitado',
            'Se deshabilitó el proyecto ' +
              this.projectSelected.name +
              ' exitosamente'
          );
        },
        error: (err) => {
          this._alertService.error(
            'Error al deshabilitar',
            err?.error?.message || 'Ocurrió un error inesperado'
          );
        },
        complete: () => {
          this.closeModal();
        },
      });
    }
  }

  updateMyProjects() {
    if (this.userId) {
      this.myProjects = this.projects.filter(
        (project: Project) =>
          project.isEnabled && project.fkUser === this.userId
      );
    }
  }

  showProject(project: Project){
    this._router.navigate(['/project', project.id])
    
  }
}
