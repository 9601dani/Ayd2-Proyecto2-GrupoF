import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  readonly USER_API = `${environment.API_URL}/v1/users`;
  readonly TOKEN_API = `${environment.API_URL}/v1/tokens`;
  readonly ROLE_API = `${environment.API_URL}/v1/roles`;

  constructor(private http: HttpClient) {}

  authenticate(usernameOrEmail: string, password: string): Observable<any> {
    return this.http.post(`${this.USER_API}/authenticate`, {
      usernameOrEmail,
      password,
    });
  }

  refreshToken(id: number, token: string): Observable<any> {
    return this.http.post(`${this.TOKEN_API}`, { id, token });
  }

  test(): Observable<any> {
    return this.http.get(`${this.USER_API}`);
  }

  logout(id: number): Observable<any> {
    return this.http.put(`${this.USER_API}/logout/${id}`, {});
  }

  getUsersByRole(role: number): Observable<any> {
    return this.http.get(`${this.USER_API}/role/${role}`);
  }
  registerUser(user: any): Observable<any> {
    return this.http.post(`${this.USER_API}/register`, user);
  }

  updateUser(user: any): Observable<any> {
    return this.http.put(`${this.USER_API}/update`, user);
  }

  disabledUser(username: string): Observable<any> {
    return this.http.delete(`${this.USER_API}/delete/${username}`);
  }

  enableUser(user: any): Observable<any> {
    return this.http.put(`${this.USER_API}/enable`, user);
  }

  getAllUsers(): Observable<any> {
    return this.http.get(`${this.USER_API}/all`);
  }

  getAllRoles(): Observable<any> {
    return this.http.get(`${this.ROLE_API}/all`);
  }

  getByUsername(username: string): Observable<any> {
    return this.http.get(`${this.USER_API}/byUsername/${username}`);
  }

  updatePhotoPath(formData: FormData, id: number): Observable<any> {
    return this.http.put<any>(
      `${this.USER_API}/update/photo_path/${id}`,
      formData
    );
  }

  updateUserMyProfile(userData: any): Observable<any> {
    return this.http.put(`${this.USER_API}/myProfile`, userData);
  }
}
