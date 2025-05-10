import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Title} from '@angular/platform-browser';
import {Subscription} from 'rxjs';
import {CompanyService} from './services/company/company.service';
import {LocalStorageService} from './services/commons/local-storage.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit, OnDestroy {

  private _titleService: Title = inject(Title);
  private subscription!: Subscription;
  private _companyService: CompanyService = inject(CompanyService);
  private _localStorageService: LocalStorageService = inject(LocalStorageService);
  title = 'app-frontend';

  constructor() {

  }

  ngOnInit() {
    this.subscribeSettingBehavior();
    this.getSettings();
  }

  ngOnDestroy() {
    if(this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  subscribeSettingBehavior() {
    this.subscription = this._companyService.getSettingBehavior().subscribe(value => {
      this.getSettings();
    });
  }

  getSettings() {
    this._companyService.findSettingsByKeyname().subscribe({
      next: (response: any) => {
        const title = response.find((r: any) => r.keyName === 'company_name');
        this._titleService.setTitle(title.keyValue);
        response.forEach((r: any) => {
          this._localStorageService.removeItem(r.keyName);
          this._localStorageService.setItem(r.keyName, r.keyValue);
        })
      },
      error: (error: any) => {
        console.error(error);
      }
    })
  }
}
