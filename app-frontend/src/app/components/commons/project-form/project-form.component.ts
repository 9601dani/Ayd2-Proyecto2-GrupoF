import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Project } from '../project/project.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-project-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './project-form.component.html',
  styleUrl: './project-form.component.scss',
})
export class ProjectFormComponent implements OnInit {
  @Input() mode: 'create' | 'edit' = 'create';
  @Input() project: Project | null = null;
  users: any[] = [
    {
      id: 2,
      name: 'user 1',
    },
    {
      id: 3,
      name: 'user 2',
    },
  ];

  @Output() submitForm = new EventEmitter<Project>();
  @Output() cancel = new EventEmitter<void>();

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      fkUser: [null, Validators.required],
    });
  }

  ngOnInit() {
    if (this.mode === 'edit' && this.project) {

      console.log(this.project);
      
      this.form.patchValue({
        name: this.project.name,
        description: this.project.description,
        fkUser: this.project.fkUser,
      });
    }
  }

  onSubmit() {
    if (this.form.valid) {
      this.submitForm.emit(this.form.value);
    }
  }
}
