import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CasePhase, CaseType } from '../../../models/CasePhase.model';

@Component({
  selector: 'app-case-phase-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './case-phase-form.component.html',
  styleUrl: './case-phase-form.component.scss'
})
export class CasePhaseFormComponent {
  @Input() mode: 'register' | 'edit' = 'register';
  @Input() preSelectedType: number | null = null;
  @Input() caseTypes: CaseType[] = [];
  @Input() phases: CasePhase[] = [];
  

  @Output() submitForm = new EventEmitter<CasePhase[]>();
  @Output() cancel = new EventEmitter<void>();

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      FK_Case_Types: [null, Validators.required],
      phases: this.fb.array([])
    });
  }

  get phasesFormArray(): FormArray {
    return this.form.get('phases') as FormArray;
  }

  addPhase() {
    const index = this.phases.length + 1;
    this.phasesFormArray.push(this.fb.group({
      name: ['', Validators.required],
      order: [index, Validators.required]
    }));
  }

  removePhase(index: number) {
    this.phasesFormArray.removeAt(index);
  }


  onSubmit() {
    if (this.form.valid) {
      const FK_Case_Types = this.form.get('FK_Case_Types')?.value;
      const result: CasePhase[] = this.phasesFormArray.value.map((p: any) => ({
        ...p,
        FK_Case_Types
      }));
      this.submitForm.emit(result);
    }
  }
}
