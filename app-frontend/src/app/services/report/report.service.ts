import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Report2Dto } from '../../components/admin/reports/time-cost-by-project/time-cost-by-project.component';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  readonly REPORT_API = `${environment.API_URL}/v1/reports`;

  constructor(private http: HttpClient) {}

  getReport1(params?: any) {
    return this.http.get<any[]>(`${this.REPORT_API}/report1`, { params });
  }

  getReport2(projectId?: number) {
  const params: any = {};
  if (projectId !== undefined && projectId !== null) {
    params.projectId = projectId;
  }

  return this.http.get<Report2Dto[]>(`${this.REPORT_API}/report2`, { params });
}

}
