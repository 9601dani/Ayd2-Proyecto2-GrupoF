import {Component, inject, OnInit} from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AsyncPipe } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import {MenuService, Module} from '../../../services/test/menu.service';
import {ToolbarComponent} from '../toolbar/toolbar.component';
import {SidebarComponent} from "../sidebar/sidebar.component";
import {CommonService} from '../../../services/commons/common.service';

@Component({
  selector: 'app-template',
  templateUrl: './template.component.html',
  styleUrl: './template.component.scss',
  standalone: true,
    imports: [
        MatToolbarModule,
        MatButtonModule,
        MatSidenavModule,
        MatListModule,
        MatIconModule,
        AsyncPipe,
        ToolbarComponent,
        SidebarComponent,
    ]
})
export class TemplateComponent implements OnInit {

  modules: Module[] = [];

  private breakpointObserver = inject(BreakpointObserver);
  constructor(
    private _menuService: MenuService,
    private _commonService: CommonService
  ) {}

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  ngOnInit(): void {
    const roleId = 1;
    this._menuService.getMenuByRole(roleId).subscribe(data => {
      this.modules = data;
    });
  }

  openModal() {
    this._commonService.emitActiveModal(true);
  }
}
