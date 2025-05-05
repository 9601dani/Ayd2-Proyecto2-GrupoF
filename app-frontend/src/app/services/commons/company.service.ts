import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CompanyService {
  
  readonly apiPages = `http://localhost:8003/v1/pages`;

  constructor(private http: HttpClient) {}

  getPages(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiPages}/${id}`);
  }
}
