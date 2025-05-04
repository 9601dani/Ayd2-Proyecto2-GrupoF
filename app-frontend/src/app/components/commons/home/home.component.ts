import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TemplateComponent} from '../template/template.component';
import {ModalComponent} from '../modal/modal.component';
import {LoginComponent} from '../login/login.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, TemplateComponent, ModalComponent, LoginComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
