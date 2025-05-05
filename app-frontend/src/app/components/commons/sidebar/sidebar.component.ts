import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { CommonService } from '../../../services/commons/common.service';
import { CompanyService } from '../../../services/commons/company.service';

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
  private subscription!: Subscription;

  constructor(
    private _commonService: CommonService,
    private _companyService: CompanyService,
    private _router: Router
  ) {}

  ngOnInit() {
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
    this._companyService.getPages(2).subscribe({
      next: (value: any) => {
        this.modules = this.transformToModules(value);
      },
      error: (err) => {
        console.log(err);
      },
    });
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
}
