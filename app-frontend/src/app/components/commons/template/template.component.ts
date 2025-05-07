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
import {LocalStorageService} from '../../../services/commons/local-storage.service';
import {ImagePipe} from '../../../pipes/image.pipe';
import {UserService} from '../../../services/user/user.service';
import {AlertService} from '../../../services/commons/alert.service';
import {Router} from '@angular/router';

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
    ImagePipe,
  ]
})
export class TemplateComponent implements OnInit {

  modules: Module[] = [];
  isLogged: boolean = false;
  photo: string = '';
  username: string = '';

  private breakpointObserver = inject(BreakpointObserver);
  constructor(
    private _menuService: MenuService,
    private _commonService: CommonService,
    private _localStorageService: LocalStorageService,
    private _userService: UserService,
    private _alertService: AlertService,
    private _router: Router
  ) {}

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  ngOnInit(): void {
    this.isLogged = (this._localStorageService.getItem(this._localStorageService.USER_ID) || 0) !== 0;

    if(this.isLogged) {
      this.getUserData();
      this.getUserModules();
    }

  }

  getUserData() {
    this.photo = this._localStorageService.getItem(this._localStorageService.USER_PHOTO);
    this.username = this._localStorageService.getItem(this._localStorageService.USER_NAME);
  }

  getUserModules() {
    const userId = this._localStorageService.getItem(this._localStorageService.USER_ID) || 0;
    this._menuService.getMenuByRole(userId).subscribe(data => {
      this.modules = data;
    });
  }

  openModal() {
    this._commonService.emitActiveModal(true);
  }

  logout() {
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
}
