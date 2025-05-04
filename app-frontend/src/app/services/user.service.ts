import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  readonly apiUser = `${environment.API_URL}/v1/users`;

  constructor(private http: HttpClient) { }



}
