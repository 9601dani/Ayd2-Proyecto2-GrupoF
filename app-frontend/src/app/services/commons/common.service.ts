import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  activePageSubject = new BehaviorSubject<string>("");
  activeModalSubject = new BehaviorSubject<boolean>(false);

  constructor() { }

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
}
