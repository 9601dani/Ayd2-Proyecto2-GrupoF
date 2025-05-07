import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  readonly SETTING_URL = `${environment.API_URL}/settings`

  constructor(private http: HttpClient) { }

  findAll(): Observable<any> {
    return this.http.get(`${this.SETTING_URL}`);
  }
}
