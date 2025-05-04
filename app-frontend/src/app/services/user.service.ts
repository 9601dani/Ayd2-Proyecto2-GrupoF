import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  readonly apiUser = `${environment.API_URL}/v1/users`;

  constructor(private http: HttpClient) { }

  registerUser(user:any): Observable<any>{
    return this.http.post(`${this.apiUser}/register`, user)
  }

}
