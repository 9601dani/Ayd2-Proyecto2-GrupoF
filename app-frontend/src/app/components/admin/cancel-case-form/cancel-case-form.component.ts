import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';


@Component({
  selector: 'app-cancel-case-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cancel-case-form.component.html',
  styleUrl: './cancel-case-form.component.scss'
})
export class CancelCaseFormComponent implements OnInit {
  @Output() submitReason = new EventEmitter<string>();
  @Output() cancel = new EventEmitter<void>();

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      reason: ['', Validators.required],
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.form.valid) {
      this.submitReason.emit(this.form.value.reason);
    }
  }
}