import {Component, OnInit} from '@angular/core';
import {ModalComponent} from "../modal/modal.component";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatIcon} from '@angular/material/icon';
import {MatPrefix} from '@angular/material/form-field';
import {UserService} from '../../../services/user/user.service';
import {AlertService} from '../../../services/commons/alert.service';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {CommonService} from '../../../services/commons/common.service';
import {LocalStorageService} from '../../../services/commons/local-storage.service';
import {Router} from '@angular/router';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ModalComponent,
    ReactiveFormsModule,
    MatIcon,
    MatProgressSpinner,
    NgClass
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;
  visibility: boolean = false;
  isLoading: boolean = false;

  constructor(
    private _fb: FormBuilder,
    private _userService: UserService,
    private _alertService: AlertService,
    private _commonService: CommonService,
    private _localStorageService: LocalStorageService,
    private _router: Router
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

    this.isLoading = true;

    const { usernameOrEmail, password } = this.loginForm.value;

    this._userService.authenticate(usernameOrEmail, password).subscribe({
      next: (response: any) => {
        this.saveUserData(response);
        this.isLoading = false;
        this._commonService.emitActiveModal(false);
        this.reloadPage();
      },
      error: (error: any) => {
        this._alertService.error("Error!", error.error.message);
        this.isLoading = false;
      }
    })
  }

  saveUserData(response: any) {
    this._localStorageService.setItem(this._localStorageService.USER_ID, response.id);
    this._localStorageService.setItem(this._localStorageService.USER_NAME, response.username);
    this._localStorageService.setItem(this._localStorageService.USER_PHOTO, response.photo);
    this._localStorageService.saveTokens(response.token);
  }

  reloadPage() {
    const currentUrl = this._router.url;

    this._router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this._router.navigateByUrl(currentUrl);
    });
  }

  closeModal() {
    this.loginForm.reset();
    this._commonService.emitActiveModal(false);
  }

}
