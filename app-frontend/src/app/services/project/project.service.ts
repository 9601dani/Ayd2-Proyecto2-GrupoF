import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';
import { CaseType } from '../../models/CasePhase.model';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  readonly PROJECT_API = `${environment.API_URL}/v1/projects`;
  readonly TYPE_CASE_API = `${environment.API_URL}/v1/type_cases`;
  readonly CASE_API = `${environment.API_URL}/v1/cases`;
  readonly COMMENT_API = `${environment.API_URL}/v1/comments`;

  constructor(private http: HttpClient) {}

  createProject(body: any): Observable<any> {
    return this.http.post(`${this.PROJECT_API}/save`, body);
  }

  updateProject(body: any): Observable<any> {
    return this.http.put(`${this.PROJECT_API}/update`, body);
  }

  getAllProjects(): Observable<any> {
    return this.http.get(`${this.PROJECT_API}/all`);
  }

  updateIsEnable(body: any): Observable<any> {
    return this.http.put(`${this.PROJECT_API}/update/enable`, body);
  }

  /* ------------------------ TYPE CASES ----------------------- */
  getAllCaseTypesWithPhases(): Observable<CaseType[]> {
    return this.http.get<CaseType[]>(`${this.TYPE_CASE_API}`);
  }

  createCaseType(data: CaseType): Observable<CaseType> {
    return this.http.post<CaseType>(`${this.TYPE_CASE_API}`, data);
  }

  updateCaseType(data: CaseType): Observable<CaseType> {
    return this.http.put<CaseType>(`${this.TYPE_CASE_API}/${data.id}`, data);
  }
    /* ------------------------ END TYPE CASES ----------------------- */


  getProjectById(id: number): Observable<any> {
    return this.http.get(`${this.PROJECT_API}/${id}`);
  }

  createCase(body: any): Observable<any> {
    return this.http.post(`${this.CASE_API}/save`, body);
  }

  getCaseById(id: number): Observable<any> {
    return this.http.get(`${this.CASE_API}/${id}`);
  }

  updateCase(body: any): Observable<any> {
    return this.http.put(`${this.CASE_API}/update`, body);
  }

  updateCancelCase(body: any): Observable<any> {
    return this.http.put(`${this.CASE_API}/update/cancel`, body);
  }

  getCasesByFkProject(id: number): Observable<any> {
    return this.http.get(`${this.CASE_API}/all/${id}`);
  }


  getCasesWithUserByFkProject(id: number): Observable<any> {
    return this.http.get(`${this.CASE_API}/active/${id}`);
  }

  /*-------------------------------COMMENTS--------------------------------*/

  saveComment(data: any): Observable<any> {
    return this.http.post(`${this.COMMENT_API}`, data);
  }

  getCommentsByCaseAndParentId(id: number, idParent: number | null = null): Observable<any> {
    let params = new HttpParams();
    if (idParent !== null) {
      params = params.set("idParent", idParent);
    }
    return this.http.get(`${this.COMMENT_API}/find-by-case-id/${id}`, {params});
  }

  
  getCasesWithUserByFkProject(id: number): Observable<any> {
    return this.http.get(`${this.CASE_API}/active/${id}`);
  }

}
