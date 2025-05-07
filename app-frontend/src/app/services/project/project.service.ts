import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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
}
