import { Component } from '@angular/core';
import { CaseType } from '../../../models/CasePhase.model';
import { ProjectService } from '../../../services/project/project.service';
import { TemplateComponent } from '../../commons/template/template.component';
import { ModalComponent } from '../../commons/modal/modal.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CommonService } from '../../../services/commons/common.service';
import { AlertService } from '../../../services/commons/alert.service';
import { CaseTypeFormComponent } from '../case-type-form/case-type-form.component';

@Component({
  selector: 'app-case-type',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalComponent, TemplateComponent, CaseTypeFormComponent],
  templateUrl: './case-type.component.html',
  styleUrl: './case-type.component.scss'
})
export class CaseTypeComponent {
  caseTypes: CaseType[] = [];
  selectedCaseType: CaseType | null = null;
  action: 'create' | 'edit' | 'phases' | null = null;

  constructor(
    private _commonService: CommonService,
    private _caseTypeService: ProjectService,
    private _alertService: AlertService
  ) {}

  ngOnInit() {
    this.loadCaseTypes();
  }

  loadCaseTypes() {
/*     this._caseTypeService.getAllCaseTypesWithPhases().subscribe({
      next: (data) => {
        this.caseTypes = data;
      },
      error: (err) => {
        this._alertService.error('Error al cargar tipos de caso', err.message || 'Error inesperado');
      }
    }); */
    this.caseTypes = [
      {
        id: 1,
        name: 'Investigación',
        description: 'Casos relacionados con investigaciones internas',
        phases: [
          { id: 1, name: 'Inicio', FK_Case_Type: 1 },
          { id: 2, name: 'Análisis', FK_Case_Type: 1, next_phase: 3 },
          { id: 3, name: 'Informe', FK_Case_Type: 1 }
        ]
      },
      {
        id: 2,
        name: 'Auditoría',
        description: 'Casos de auditoría financiera',
        phases: [
          { id: 4, name: 'Planificación', FK_Case_Type: 2 },
          { id: 5, name: 'Ejecución', FK_Case_Type: 2 }
        ]
      }
    ];
  }

  openModal(action: 'create' | 'edit' | 'phases', caseType?: CaseType) {
    this.action = action;
    this.selectedCaseType = caseType || null;
    this._commonService.emitActiveModal(true);
  }

  closeModal() {
    this.action = null;
    this.selectedCaseType = null;
    this._commonService.emitActiveModal(false);
  }

  onFormSubmit(caseType: CaseType) {
    const request = this.action === 'create'
      ? this._caseTypeService.createCaseType(caseType)
      : this._caseTypeService.updateCaseType(caseType);

    request.subscribe({
      next: (res) => {
        this._alertService.success(
          this.action === 'create' ? 'Registro exitoso' : 'Actualización exitosa',
          `El tipo de caso "${res.name}" fue ${this.action === 'create' ? 'registrado' : 'actualizado'} correctamente.`
        );
        this.loadCaseTypes();
        this.closeModal();
      },
      error: (err) => {
        this._alertService.error('Error', err.message || 'Ocurrió un error inesperado');
      }
    });
  }
}
