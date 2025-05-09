import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CaseType } from '../../models/CasePhase.model';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  readonly PROJECT_API = `${environment.API_URL}/v1/projects`;

  constructor(private http: HttpClient) {}

  createProject(body: any): Observable<any> {
    return this.http.post(`${this.PROJECT_API}/save`, body);
  }
  
  updateProject(body: any): Observable<any> {
    return this.http.put(`${this.PROJECT_API}/update`, body);
  }
  
  getAllProjects(): Observable<any>{
    return this.http.get(`${this.PROJECT_API}/all`);
  }

  updateIsEnable(body: any): Observable<any> {
    return this.http.put(`${this.PROJECT_API}/update/enable`, body);
  }

  /* ------------------------ TYPE CASES ----------------------- */
  getAllCaseTypesWithPhases(): Observable<CaseType[]> {
    return this.http.get<CaseType[]>(`${this.PROJECT_API}/case-types?include=phases`);
  }
  
  createCaseType(data: CaseType): Observable<CaseType> {
    return this.http.post<CaseType>(`${this.PROJECT_API}/case-types`, data);
  }
  
  updateCaseType(data: CaseType): Observable<CaseType> {
    return this.http.put<CaseType>(`${this.PROJECT_API}/case-types/${data.id}`, data);
  }
  
}
