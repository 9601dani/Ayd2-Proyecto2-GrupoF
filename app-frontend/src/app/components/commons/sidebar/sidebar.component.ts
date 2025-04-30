import {Component, OnDestroy, OnInit} from '@angular/core';
import {MenuService, Module} from '../../../services/test/menu.service';
import { CommonModule } from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import {Subscription} from 'rxjs';
import {CommonService} from '../../../services/commons/common.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent implements OnInit, OnDestroy {
  modules: Module[] = [];
  activePage: string = "";
  private subscription!: Subscription;

  constructor(
    private _commonService: CommonService,
    private _menuService: MenuService,
    private _router: Router
  ) {
  }

  ngOnInit() {
    this.getModules();
    this.subscribeToBehavior();
  }

  subscribeToBehavior() {
    this.subscription = this._commonService.getActivePage().subscribe({
      next: (pageName: string) => {
        this.activePage = pageName;
      }
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  getModules() {
    this._menuService.getMenuByRole(1).subscribe({
      next: response => {
        this.modules = response;
      },
      error: err => {
        console.error(err);
      }
    })
  }

  redirectTo(page: any) {
    this._commonService.emitActivePage(page.name);
    this._router.navigate([page.path]);
  }
}
