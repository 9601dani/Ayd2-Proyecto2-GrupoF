import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CaseType } from '../../../models/CasePhase.model';
import { FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ModalComponent } from '../../commons/modal/modal.component';
import { TemplateComponent } from '../../commons/template/template.component';

@Component({
  selector: 'app-case-type-form',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalComponent, TemplateComponent, ReactiveFormsModule],
  templateUrl: './case-type-form.component.html',
  styleUrl: './case-type-form.component.scss'
})
export class CaseTypeFormComponent implements OnInit {
  @Input() mode: 'create' | 'edit' = 'create';
  @Input() caseType?: CaseType;
  @Output() submitForm = new EventEmitter<CaseType>();
  @Output() cancel = new EventEmitter<void>();

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      phases: this.fb.array([this.createPhaseGroup()])
    });
  }
  

  ngOnInit() {
    if (this.mode === 'edit' && this.caseType) {
      this.form.patchValue({
        name: this.caseType.name,
        description: this.caseType.description,
      });
  
      const casePhases = (this.caseType as any).phases || [];
      const phaseFormGroups = casePhases.map((phase: any) =>
        this.fb.group({
          name: [phase.name, Validators.required],
          next_phase: [phase.next_phase || null]
        })
      );
  
      this.form.setControl('phases', this.fb.array(phaseFormGroups));
    }
  }
  

  onSubmit() {
    if (this.form.valid) {
      this.submitForm.emit({ ...this.caseType, ...this.form.value });
    }
  }

  createPhaseGroup(): FormGroup {
    return this.fb.group({
      name: ['', Validators.required],
      next_phase: [null]
    });
  }
  
  get phases(): FormArray {
    return this.form.get('phases') as FormArray;
  }
  
  addPhase() {
    this.phases.push(this.createPhaseGroup());
  }
  
  removePhase(index: number) {
    this.phases.removeAt(index);
  }
}
