import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  readonly PROJECT_API = `${environment.API_URL}/v1/projects`;

  constructor(private http: HttpClient) { }

  createProject(body:any): Observable<any>{
      return this.http.post(`${this.PROJECT_API}/save`, body)
    }
}
