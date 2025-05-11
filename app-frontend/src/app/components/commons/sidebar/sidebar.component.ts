import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { CommonService } from '../../../services/commons/common.service';
import { LocalStorageService } from '../../../services/commons/local-storage.service';
import {UserService} from '../../../services/user/user.service';
import {CompanyService} from '../../../services/company/company.service';

export interface Page {
  id: number;
  name: string;
  path: string;
}

export interface Module {
  id: number;
  name: string;
  path: string;
  pages: Page[];
}

interface ApiItem {
  moduleId: number;
  moduleName: string;
  modulePath: string;
  pageId: number;
  pageName: string;
  pagePath: string;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
})
export class SidebarComponent implements OnInit, OnDestroy {
  modules: Module[] = [];
  activePage: string = '';
  isLogged: boolean = false;
  private subscription!: Subscription;

  constructor(
    private _commonService: CommonService,
    private _companyService: CompanyService,
    private _router: Router,
    private _localStorageService: LocalStorageService,
    private _userService: UserService
  ) {}

  ngOnInit() {
    this.isLogged = (this._localStorageService.getItem(this._localStorageService.USER_ID) || 0) !== 0;
    this.getModules();
    this.subscribeToBehavior();
  }

  subscribeToBehavior() {
    this.subscription = this._commonService.getActivePage().subscribe({
      next: (pageName: string) => {
        this.activePage = pageName;
      },
    });
  }

  ngOnDestroy() {
    this.subscription?.unsubscribe();
  }

  getModules() {
    const userId = this._localStorageService.getItem(
      this._localStorageService.USER_ID
    );

    if (userId) {
      this._companyService.getPages(userId).subscribe({
        next: (value: any) => {
          this.modules = this.transformToModules(value);
        },
        error: (err) => {
          console.log(err);
        },
      });
    }
  }

  redirectTo(page: any) {
    this._commonService.emitActivePage(page.name);
    this._router.navigate([page.path]);
  }

  transformToModules(data: ApiItem[]): Module[] {
    const moduleMap = new Map<number, Module>();

    data.forEach((item) => {
      if (!moduleMap.has(item.moduleId)) {
        moduleMap.set(item.moduleId, {
          id: item.moduleId,
          name: item.moduleName,
          path: item.modulePath,
          pages: [],
        });
      }

      const module = moduleMap.get(item.moduleId)!;
      module.pages.push({
        id: item.pageId,
        name: item.pageName,
        path: item.pagePath,
      });
    });

    return Array.from(moduleMap.values());
  }

  logout() {
    this._commonService.emitActivePage('logout');
    const id = this._localStorageService.getItem(this._localStorageService.USER_ID) || 0;
    this._userService.logout(id).subscribe({
      next: (response: any) => {
        this._localStorageService.logout();
        this._router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
          this._router.navigateByUrl('/home');
        });
      },
      error: err => {
        console.log(err);
      }
    })
  }

  viewMyProfile(){
    this._commonService.emitActivePage("profile");
    this._router.navigateByUrl('/profile');
  }

  goToHome(){
    this._commonService.emitActivePage("home");
    this._router.navigateByUrl('/home');
  }
 

}
