import { Injectable } from '@angular/core';
import {BehaviorSubject, map, Observable} from 'rxjs';
import { CompanyInfo } from '../../utils/pdf-generator.utils';
import { CompanyService } from '../company/company.service';

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  activePageSubject = new BehaviorSubject<string>("");
  activeModalSubject = new BehaviorSubject<boolean>(false);

  constructor(private _companyService:CompanyService) { }

  emitActivePage(pageName: string) {
    this.activePageSubject.next(pageName);
  }

  getActivePage() {
    return this.activePageSubject.asObservable();
  }

  emitActiveModal(value: boolean) {
    this.activeModalSubject.next(value);
  }

  getActiveModal() {
    return this.activeModalSubject.asObservable();
  }

  getCompanyInfo(): Observable<CompanyInfo> {
    return this._companyService.findAllSettings().pipe(
      map((response: any) => {
        const settings: any = {};
        
        response.forEach((module: any) => {
          module.settings.forEach((s: any) => {
            settings[s.keyName.toLowerCase()] = s.keyValue;
          });
        });

        return {
          companyName: settings['company_name'] || '',
          companyAddress: settings['company_address'] || '',
          companyPhoneNumber: `+${settings['company_ext'] || ''} ${settings['company_phone_number'] || ''}`.trim(),
          emailAddress: settings['company_email_address'] || ''
        };
      })
    );
  }
}
