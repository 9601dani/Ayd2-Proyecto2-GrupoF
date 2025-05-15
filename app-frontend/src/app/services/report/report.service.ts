import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Report2Dto } from '../../components/admin/reports/time-cost-by-project/time-cost-by-project.component';
import { Report3Dto } from '../../components/admin/reports/time-cost-by-user/time-cost-by-user.component';
import { Report4Dto } from '../../components/admin/reports/time-cost-case-type/time-cost-case-type.component';
import { UserResponseWithName } from '../../components/admin/reports/users-report/users-report.component';
import { ProjectResponseWithoutUser } from '../../components/admin/reports/projects-report/projects-report.component';
import { Report8Dto } from '../../components/admin/reports/top-user-by-cases/top-user-by-cases.component';
import { Report9Dto } from '../../components/admin/reports/top-user-by-pay/top-user-by-pay.component';
import { TopProjectByCompletedCasesDto } from '../../components/admin/reports/top-project-completed/top-project-completed.component';
import { TopProjectByCancelledCasesDto } from '../../components/admin/reports/top-project-cancelled/top-project-cancelled.component';

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

    return this.http.get<Report2Dto[]>(`${this.REPORT_API}/report2`, {
      params,
    });
  }

  getReport3(userId?: number) {
    const params: any = {};
    if (userId !== undefined && userId !== null) {
      params.userId = userId;
    }

    return this.http.get<Report3Dto[]>(`${this.REPORT_API}/report3`, {
      params,
    });
  }

  getReport4(typeId?: number) {
    const params: any = {};
    if (typeId !== undefined && typeId !== null) {
      params.typeId = typeId;
    }

    return this.http.get<Report4Dto[]>(`${this.REPORT_API}/report4`, {
      params,
    });
  }

  getReport6() {
    return this.http.get<UserResponseWithName[]>(`${this.REPORT_API}/report6`);
  }

  getReport7() {
    return this.http.get<ProjectResponseWithoutUser[]>(
      `${this.REPORT_API}/report7`
    );
  }

  getReport8() {
    return this.http.get<Report8Dto>(`${this.REPORT_API}/report8`);
  }

  getReport9() {
    return this.http.get<Report9Dto>(`${this.REPORT_API}/report9`);
  }

  getReport10() {
    return this.http.get<TopProjectByCompletedCasesDto>(
      `${this.REPORT_API}/report10`
    );
  }

  getReport11() {
    return this.http.get<TopProjectByCancelledCasesDto>(
      `${this.REPORT_API}/report11`
    );
  }
}
