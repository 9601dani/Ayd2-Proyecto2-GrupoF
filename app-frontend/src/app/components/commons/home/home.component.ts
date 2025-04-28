import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TemplateComponent} from '../template/template.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, TemplateComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
