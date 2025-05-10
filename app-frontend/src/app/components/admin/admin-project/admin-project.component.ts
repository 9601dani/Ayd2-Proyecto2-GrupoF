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
  isCanceled: boolean;
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
  cases: Case[] = [
    {
      id: 1,
      name: 'Diseño de interfaz de usuario',
      description:
        'Crear los wireframes y mockups para la vista principal de la aplicación.',
      fkProject: 2,
      progressPercentage: 80,
      fkCaseType: 1,
      limitDate: new Date('2025-04-01'),
      isEnabled: true,
      isCanceled: false,
      reasonCancellation: '',
    },
    {
      id: 2,
      name: 'Integración con base de datos',
      description:
        'Conectar la aplicación con la base de datos PostgreSQL para gestionar usuarios.',
      fkProject: 2,
      progressPercentage: 50,
      fkCaseType: 2,
      limitDate: new Date('2025-06-15'),
      isEnabled: true,
      isCanceled: false,
      reasonCancellation: '',
    },
    {
      id: 3,
      name: 'Pruebas funcionales',
      description:
        'Realizar pruebas de los módulos de login y registro para asegurar funcionamiento.',
      fkProject: 3,
      progressPercentage: 30,
      fkCaseType: 1,
      limitDate: new Date('2025-06-20'),
      isEnabled: true,
      isCanceled: false,
      reasonCancellation: '',
    },
    {
      id: 4,
      name: 'Documentación técnica',
      description:
        'Escribir documentación sobre la arquitectura del sistema y su instalación.',
      fkProject: 3,
      progressPercentage: 100,
      fkCaseType: 1,
      limitDate: new Date('2025-06-30'),
      isEnabled: false,
      isCanceled: false,
      reasonCancellation: '',
    },
  ];

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
    console.log(data);
  }

  onEdit(data: any) {
    console.log(data);
  }

  cancelCase(){
    console.log(this.form.value);
    
  }
}
