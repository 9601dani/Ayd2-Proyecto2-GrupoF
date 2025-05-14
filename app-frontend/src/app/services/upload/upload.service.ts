import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadService {
  readonly UPLOAD_URL = `${environment.API_URL}/v1/uploads`;
  

  constructor(private http: HttpClient) { }

  getImageBase64(fileKey: string) {
    const params = new HttpParams().set('fileKey', fileKey);
    return this.http.get(`${this.UPLOAD_URL}/images/base64`, {
      params,
      responseType: 'text'
    });
  }
}
