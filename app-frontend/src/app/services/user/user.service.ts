import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  readonly USER_API = `${environment.API_URL}/v1/users`;
  readonly TOKEN_API = `${environment.API_URL}/v1/tokens`;

  constructor(private http: HttpClient) { }

  authenticate(usernameOrEmail: string, password: string): Observable<any> {
    return this.http.post(`${this.USER_API}/authenticate`, { usernameOrEmail, password });
  }

  refreshToken(id: number, token: string): Observable<any> {
    return this.http.post(`${this.TOKEN_API}`, { id, token });
  }

  test(): Observable<any> {
    return this.http.get(`${this.USER_API}`);
  }

  logout(id: number): Observable<any> {
    return this.http.put(`${this.USER_API}/logout/${id}`, {})
  }
}
