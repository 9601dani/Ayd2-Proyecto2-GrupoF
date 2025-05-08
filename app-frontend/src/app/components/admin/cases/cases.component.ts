import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CasePhase, CaseType } from '../../../models/CasePhase.model';
import { CommonService } from '../../../services/commons/common.service';
import { CasePhaseFormComponent } from '../case-phase-form/case-phase-form.component';
import { ModalComponent } from '../../commons/modal/modal.component';
import { TemplateComponent } from '../../commons/template/template.component';

@Component({
  selector: 'app-cases',
  standalone: true,
  imports: [CommonModule, CasePhaseFormComponent, ModalComponent, TemplateComponent],
  templateUrl: './cases.component.html',
  styleUrl: './cases.component.scss'
})
export class CasesComponent {
  actualAction: 'view' | 'register' | 'edit' | null = null;
  selectedType: CaseType | null = null;
  filteredPhases: CasePhase[] = [];

  caseTypes: CaseType[] = [
    { id: 1, name: 'Penal', description: 'Procesos penales' },
    { id: 2, name: 'Familiar', description: 'Casos de familia' }
  ];

  casePhases: CasePhase[] = [
    { id: 101, name: 'Investigación', FK_Case_Types: 1, next_phase: 102 },
    { id: 102, name: 'Audiencia preliminar', FK_Case_Types: 1, next_phase: 103 },
    { id: 103, name: 'Juicio oral', FK_Case_Types: 1, next_phase: null },
    { id: 201, name: 'Denuncia', FK_Case_Types: 2, next_phase: 202 },
    { id: 202, name: 'Conciliación', FK_Case_Types: 2, next_phase: null }
  ];

  constructor(private _commonService: CommonService){}

  getCaseTypeName(id: number): string {
    return this.caseTypes.find(t => t.id === id)?.name ?? 'Desconocido';
  }

  getNextPhaseName(id: number | null | undefined): string {
    if (!id) return 'Fin';
    const phase = this.casePhases.find(p => p.id === id);
    return phase ? phase.name : 'Desconocida';
  }

  onRegisterPhases(phases: CasePhase[]) {
    // TODO: registrar
    console.log('Fases registradas para el tipo de caso:', phases);
    this.closeModal();
  }

  openModal(action: 'view' | 'register' | 'edit', type: CaseType) {
    this.actualAction = action;
    this.selectedType = type;
    this.filteredPhases = this.casePhases.filter(p => p.FK_Case_Types === type.id);
    this._commonService.emitActiveModal(true);
  }
  
  closeModal() {
    this.actualAction = null;
    this.selectedType = null;
    this.filteredPhases = [];
    this._commonService.emitActiveModal(false);
  }

  onEditPhases(phases: CasePhase[]): void {
    console.log('Fases editadas:', phases);
    // TODO : update
    this.closeModal();
  }
  

}
