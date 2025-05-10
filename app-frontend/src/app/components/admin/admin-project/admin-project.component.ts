import { Component, OnInit } from '@angular/core';
import { TemplateComponent } from '../../commons/template/template.component';
import { CommonModule } from '@angular/common';
import { Project } from '../../commons/project/project.component';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../../services/project/project.service';
import { ModalComponent } from '../../commons/modal/modal.component';
import { CaseFormComponent } from '../case-form/case-form.component';
import { AlertService } from '../../../services/commons/alert.service';
import { CommonService } from '../../../services/commons/common.service';
import { SourceTextModule } from 'vm';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

export interface Case {
  id: number;
  name: string;
  description: string;
  fkProject: number;
  progressPercentage: number;
  fkCaseType: number;
  limitDate: Date;
  isEnabled: boolean;
  isCancelled: boolean;
  reasonCancellation: string;
}

@Component({
  selector: 'app-admin-project',
  standalone: true,
  imports: [
    TemplateComponent,
    CommonModule,
    ModalComponent,
    CaseFormComponent,
    ReactiveFormsModule,
  ],
  templateUrl: './admin-project.component.html',
  styleUrl: './admin-project.component.scss',
})
export class AdminProjectComponent implements OnInit {
  project: Project | undefined;
  projectId: number | undefined;
  cases: Case[] = [];

  userId: number | undefined;

  actualAction: 'edit' | 'cancel' | 'create' | null = null;
  caseSelected: any = null;

  form: FormGroup;

  constructor(
    private _activatedRoute: ActivatedRoute,
    private _projectService: ProjectService,
    private _alertService: AlertService,
    private _commonService: CommonService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      reason: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this._activatedRoute.params.subscribe((params) => {
      if (params['id']) {
        this.projectId = +params['id'];
      }
    });

    if (this.projectId) {
      this._projectService.getProjectById(this.projectId).subscribe({
        next: (value) => {
          this.project = value;
        },
      });

      this._projectService.getCasesByFkProject(this.projectId).subscribe({
        next: (value: any) => {
          
          
          this.cases = value.filter((c: Case) => c.isEnabled && !c.isCancelled);
        },
        error: (err) => {
          console.log(err);
        },
      });
    }
  }

  isOverdue(caseItem: Case): boolean {
    const today = new Date();
    const limitDate = new Date(caseItem.limitDate);
    return today > limitDate && caseItem.progressPercentage < 100;
  }

  openModal(action: 'edit' | 'cancel' | 'create', caseSelected: any = null) {
    this.actualAction = action;
    this.caseSelected = caseSelected;
    this._commonService.emitActiveModal(true);
  }

  closeModal() {
    this._commonService.emitActiveModal(false);
    this.actualAction = null;
    this.caseSelected = null;
  }

  onRegister(data: any) {
    data.fkProject = this.projectId;
    data.limitDate = new Date(data.limitDate);

    this._projectService.createCase(data).subscribe({
      next: (retCase) => {
        this.cases.push(retCase);

        this._alertService.success(
          'Registro exitoso',
          'Se registró el caso ' + retCase.name + ' exitosamente'
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
    data.fkProject = this.projectId;
    data.id = this.caseSelected.id;
    data.limitDate = new Date(data.limitDate);

    this._projectService.updateCase(data).subscribe({
      next: (retCase) => {
        const index = this.cases.findIndex((u) => u.id === retCase.id);
        if (index !== -1) {
          this.cases[index] = retCase;
        }

        this._alertService.success(
          'Actualización exitosa',
          'Se actualizo el caso ' + retCase.name + ' exitosamente'
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

  cancelCase() {
    const reasonCancellation = this.form.value.reason;
    const body = {
      id: this.caseSelected.id,
      reasonCancellation,
    };

    this._projectService.updateCancelCase(body).subscribe({
      next: (retCase) => {
        const index = this.cases.findIndex((u) => u.id === retCase.id);
        if (index !== -1) {
          this.cases.splice(index, 1);
        }

        this._alertService.success(
          'Actualización exitosa',
          'Se ha cancelado el caso ' + retCase.name + ' exitosamente'
        );
      },
      error: (err) => {
        this._alertService.error(
          'Error al cancelar',
          err?.error?.message || 'Ocurrió un error inesperado'
        );
      },
      complete: () => {
        this.closeModal();
      },
    });
  }
}
