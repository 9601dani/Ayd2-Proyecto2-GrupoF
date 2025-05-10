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
    this._caseTypeService.getAllCaseTypesWithPhases().subscribe({
      next: (data) => {
        this.caseTypes = data;
      },
      error: (err) => {
        this._alertService.error('Error al cargar tipos de caso', err.error.message || 'Error inesperado');
      }
    });
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
        this._alertService.error('Error', err.error.message || 'Ocurrió un error inesperado');
      }
    });
  }
}
