import { Injectable } from '@angular/core';
import {CookieService} from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  readonly TOKEN = 'token';
  readonly REFRESH_TOKEN = 'refresh_token';
  readonly USER_ID = 'user_id';
  readonly USER_NAME = 'user_name';
  readonly USER_PHOTO = 'user_photo';
  readonly COMPANY_LOGO = 'company_logo';
  readonly COMPANY_NAME = 'company_name';
  readonly COMPANY_CURRENCY = 'company_currency';
  readonly COMPANY_ADDRESS = 'company_address';
  readonly COMPANY_PHONE_NUMBER = 'company_phone_number';
  readonly COMPANY_EMAIL_ADDRESS = 'company_email_address';

  constructor(private _cookieService: CookieService) {}

  private isLocalStorageAvailable(): boolean {
    return typeof window !== 'undefined' && !!window.localStorage;
  }

  setItem(key: string, value: any): void {
    if (this.isLocalStorageAvailable()) {
      localStorage.setItem(key, JSON.stringify(value));
    }
  }

  getItem(key: string): any {
    if (this.isLocalStorageAvailable()) {
      const item = localStorage.getItem(key);
      return item ? JSON.parse(item) : null;
    }
    return null;
  }

  removeItem(key: string): void {
    if (this.isLocalStorageAvailable()) {
      localStorage.removeItem(key);
    }
  }

  saveTokens(token: any): void {
    this._cookieService.set(this.TOKEN, token.accessToken);
    this._cookieService.set(this.REFRESH_TOKEN, token.refreshToken);
  }


  logout(): void {
    this.removeItem(this.USER_ID);
    this.removeItem(this.USER_PHOTO);
    this.removeItem(this.USER_NAME);
    this._cookieService.delete(this.TOKEN);
    this._cookieService.delete(this.REFRESH_TOKEN);
  }
}
