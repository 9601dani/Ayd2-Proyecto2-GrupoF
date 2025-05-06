import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  readonly apiUser = `${environment.API_URL}/v1/users`;
  readonly apiRole = `${environment.API_URL}/v1/roles`;

  constructor(private http: HttpClient) { }

  registerUser(user:any): Observable<any>{
    return this.http.post(`${this.apiUser}/register`, user)
  }

  updateUser(user:any): Observable<any>{
    return this.http.put(`${this.apiUser}/update`, user)
  }

  disabledUser(username: string): Observable<any> {
    return this.http.delete(`${this.apiUser}/delete/${username}`);
  }

  enableUser(user: any): Observable<any> {
    return this.http.put(`${this.apiUser}/enable`,user);
  }
  

  getAllUsers(): Observable<any>{
    return this.http.get(`${this.apiUser}/all`)
  }

  getAllRoles(): Observable<any>{
    return this.http.get(`${this.apiRole}/all`)
  }

  
}
