import { Component } from '@angular/core';
import { MenuService, Module } from '../../../service/test/menu.service';
import { ToolbarComponent } from '../toolbar/toolbar.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
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
