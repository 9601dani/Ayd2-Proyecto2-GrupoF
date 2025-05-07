import { Component, OnInit } from '@angular/core';
import { TemplateComponent } from '../template/template.component';
import { ModalComponent } from '../modal/modal.component';
import { Project } from '../project/project.component';
import { CommonService } from '../../../services/commons/common.service';
import { ProjectService } from '../../../services/project/project.service';
import { AlertService } from '../../../services/commons/alert.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-disabled-project',
  standalone: true,
  imports: [TemplateComponent, ModalComponent, CommonModule],
  templateUrl: './disabled-project.component.html',
  styleUrl: './disabled-project.component.scss'
})
export class DisabledProjectComponent implements OnInit{
projects: Project[] = [];

  actualAction: 'active' | null = null;
  projectSelected: any = null;

  constructor(
    private _commonService: CommonService,
    private _projectService: ProjectService,
    private _alertService: AlertService
  ) {}

  ngOnInit(): void {
    this._projectService.getAllProjects().subscribe({
      next: (value: any) => {
        this.projects = value.filter(
          (project: { isEnabled: any }) => project.isEnabled === false
        );
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  openModal(action: 'active', project: any = null) {
    this.actualAction = action;
    this.projectSelected = project;
    this._commonService.emitActiveModal(true);
  }

  closeModal() {
    this._commonService.emitActiveModal(false);
    this.actualAction = null;
    this.projectSelected = null;
  }


  activeProject() {
    if (this.projectSelected) {
      const body = {
        id: this.projectSelected.id,
        enable: true,
      };

      this._projectService.updateIsEnable(body).subscribe({
        next: (value) => {
          const index = this.projects.findIndex(
            (u) => u.id === this.projectSelected.id
          );
          
          if (index !== -1) {
            this.projects.splice(index, 1); 
          }
          this._alertService.success(
            'Proyecto Habilitado',
            'Se activo de nuevo el proyecto ' +
              this.projectSelected.name +
              ' exitosamente'
          );
        },
        error: (err) => {
          this._alertService.error(
            'Error al activar',
            err?.error?.message || 'Ocurrió un error inesperado'
          );
        },
        complete: () => {
          this.closeModal();
        },
      });
    }
  }
}
