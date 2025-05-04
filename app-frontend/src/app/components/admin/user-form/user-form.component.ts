import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit {
  @Input() mode: 'register' | 'edit' = 'register';
  @Input() user: any = null;
  @Input() roles: { id: number, name: string }[] = [];

  @Output() submitForm = new EventEmitter<any>();
  @Output() cancel = new EventEmitter<void>();

  showPassword: boolean = false;

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      salaryPerHour: [null, [Validators.required, Validators.min(0.01)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: [null, Validators.required]
    });
  }

  ngOnInit() {
    if (this.mode === 'edit' && this.user) {
      this.form.patchValue({
        username: this.user.username,
        email: this.user.email,
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        salaryPerHour: this.user.salaryPerHour,
        role: this.user.role?.id ?? null
      });
  
      this.form.get('password')?.disable();
    }
  }

  onSubmit() {
    const result = this.form.getRawValue();
  
    if (this.mode === 'register') {
      this.submitForm.emit(result);
    } else if (this.mode === 'edit') {
      this.submitForm.emit(result);
    }
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  blockInvalidKeys(event: KeyboardEvent) {
    const invalidKeys = ['-', '+', 'e', 'E'];
    if (invalidKeys.includes(event.key)) {
      event.preventDefault();
    }
  }

  limitDecimals(event: Event) {
    const input = event.target as HTMLInputElement;
    const value = input.value;
  
    if (value.includes('.')) {
      const [intPart, decimalPart] = value.split('.');
      if (decimalPart.length > 2) {
        input.value = `${intPart}.${decimalPart.substring(0, 2)}`;
        this.form.get('salaryPerHour')?.setValue(parseFloat(input.value));
      }
    }
  }
  
}
