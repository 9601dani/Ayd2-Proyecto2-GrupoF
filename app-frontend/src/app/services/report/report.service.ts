import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  readonly REPORT_API = `${environment.API_URL}/v1/reports`;

  constructor(private http: HttpClient) {}

  getReport1(params?: any) {
  return this.http.get<any[]>(`${this.REPORT_API}/report1`, { params });
}

}
