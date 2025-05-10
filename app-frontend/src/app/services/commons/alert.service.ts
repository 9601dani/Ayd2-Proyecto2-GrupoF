import { Injectable } from '@angular/core';
import Swal, { SweetAlertResult } from 'sweetalert2'

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor() { }

  success(title: string, text: string) {
    Swal.fire({ title, icon: "success", text })
  }

  error(title:string, text: string) {
    Swal.fire({ title, icon: "error", text })
  }

  warning(title: string, text: string) {
    Swal.fire({ title, icon: "warning", text })
  }

  yesNo(title: string, text: string, callback: any = null) {
    Swal.fire({
      title,
      icon: "warning",
      text,
      showCancelButton: true,
      confirmButtonColor: "#3da9fc",
      cancelButtonColor: "#CF3030",
      confirmButtonText: "Aceptar",
      cancelButtonText: "Cancelar",
    }).then((result) => {
      if (result?.value && callback) {
        callback(result);
      }
    });
  }
}
