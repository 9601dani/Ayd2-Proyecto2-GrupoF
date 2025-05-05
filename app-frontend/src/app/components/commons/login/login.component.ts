import {Component, OnInit} from '@angular/core';
import {ModalComponent} from "../modal/modal.component";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatIcon} from '@angular/material/icon';
import {MatPrefix} from '@angular/material/form-field';
import {UserService} from '../../../services/user/user.service';
import {AlertService} from '../../../services/commons/alert.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ModalComponent,
    ReactiveFormsModule,
    MatIcon,
    MatPrefix
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;
  visibility: boolean = false;

  constructor(
    private _fb: FormBuilder,
    private _userService: UserService,
    private _alertService: AlertService
  ) {
  }

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.loginForm = this._fb.group({
      usernameOrEmail: [null, Validators.required],
      password: [null, Validators.required]
    });
  }

  authenticate() {

    if(this.loginForm.invalid) {
      this._alertService.error("Error!", "Por favor ingrese sus credenciales.");
      return;
    }

    const { usernameOrEmail, password } = this.loginForm.value;

    this._userService.authenticate(usernameOrEmail, password).subscribe({
      next: (response: any) => {
        console.log(response);
      },
      error: (error: any) => {
        console.log(error);
      }
    })

  }

}
