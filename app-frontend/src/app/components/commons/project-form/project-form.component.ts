import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Project } from '../project/project.component';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../services/user/user.service';

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
  users: any[] = [];

  @Output() submitForm = new EventEmitter<Project>();
  @Output() cancel = new EventEmitter<void>();

  form: FormGroup;

  constructor(private fb: FormBuilder, private _userService: UserService) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      fkUser: [null, Validators.required],
    });

    this._userService.getUsersByRole(1).subscribe({
      next: (value) => {
        this.users = value;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  ngOnInit() {
    if (this.mode === 'edit' && this.project) {
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
