import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  readonly PARAMS: string[] = ['company_name', 'company_logo', 'company_currency', 'company_address', 'company_phone_number', 'company_email_address'];

  readonly SETTING_URL = `${environment.API_URL}/v1/settings`
  readonly PAGES_URL = `${environment.API_URL}/v1/pages`;

  private settingsBehaviorSubject = new BehaviorSubject(false);


  constructor(private http: HttpClient) { }

  getPages(id: number): Observable<any> {
    return this.http.get<any>(`${this.PAGES_URL}/${id}`);
  }

  findAllSettings(): Observable<any> {
    return this.http.get(`${this.SETTING_URL}`);
  }

  updateCompanySettings(data: any): Observable<any> {
    return this.http.put(`${this.SETTING_URL}`, data);
  }

  findSettingsByKeyname(): Observable<any> {
    let params = new HttpParams();
    this.PARAMS.forEach(p => {
      params = params.append("keyName", p);
    })
    return this.http.get(`${this.SETTING_URL}/find-by-key-names`, { params });
  }

  emitSettingBehavior() {
    this.settingsBehaviorSubject.next(true);
  }

  getSettingBehavior() {
    return this.settingsBehaviorSubject.asObservable();
  }
}
