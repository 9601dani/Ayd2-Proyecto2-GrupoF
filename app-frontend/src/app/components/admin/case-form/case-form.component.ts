import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ProjectService } from '../../../services/project/project.service';
import { Case } from '../admin-project/admin-project.component';
import { SourceTextModule } from 'node:vm';

@Component({
  selector: 'app-case-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './case-form.component.html',
  styleUrl: './case-form.component.scss',
})
export class CaseFormComponent implements OnInit {
  @Input() mode: 'create' | 'edit' = 'create';
  @Input() caseData: Case | null = null;
  @Output() submitForm = new EventEmitter<Case>();
  @Output() cancel = new EventEmitter<void>();

  form: FormGroup;
  types: any[] = [
    { id: 1, name: 'Tipo1' },
    { id: 2, name: 'Tipo2' },
  ];

  constructor(
    private fb: FormBuilder,
    private _projectService: ProjectService
  ) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      fkCaseType: [null, Validators.required],
      limitDate: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    if (this.mode === 'edit' && this.caseData) {
      const formatDate = this.formatDate(this.caseData.limitDate)
      this.form.patchValue({
        name: this.caseData.name,
        description: this.caseData.description,
        fkCaseType: this.caseData.fkCaseType,
        limitDate: formatDate,
      });
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.submitForm.emit(this.form.value);
    }
  }

  formatDate(date: Date) {
    const anio = date.getFullYear();
    const mes = String(date.getMonth() + 1).padStart(2, '0');
    const dia = String(date.getDate()).padStart(2, '0');

    const fechaFormateada = `${anio}-${mes}-${dia}`;

    return fechaFormateada;
  }
}
