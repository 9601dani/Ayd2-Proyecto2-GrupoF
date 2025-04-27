import { Component } from '@angular/core';
import { MenuService, Module } from '../../../service/test/menu.service';
import { ToolbarComponent } from '../toolbar/toolbar.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ToolbarComponent, SidebarComponent, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  modules: Module[] = [];
  sidebarCollapsed = false;

  constructor(private _menuService: MenuService) {}

  ngOnInit(): void {
    const roleId = 1;
    this._menuService.getMenuByRole(roleId).subscribe(data => {
      this.modules = data;
    });
  }

  toggleSidebar() {
    this.sidebarCollapsed = !this.sidebarCollapsed;
  }
}
