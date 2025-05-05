import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TemplateComponent} from '../template/template.component';
import {ModalComponent} from '../modal/modal.component';
import {LoginComponent} from '../login/login.component';
import {UserService} from '../../../services/user/user.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, TemplateComponent, ModalComponent, LoginComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  constructor(private _userService: UserService) {
  }

  test() {
    this._userService.test().subscribe({
      next: response => {
        console.log(response)
      },
      error: error => {
        console.error(error);
      }
    })
  }
}
