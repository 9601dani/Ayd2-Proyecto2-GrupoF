import { Component } from '@angular/core';
import { TemplateComponent } from '../template/template.component';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../services/user/user.service';
import { LocalStorageService } from '../../../services/commons/local-storage.service';
import { AlertService } from '../../../services/commons/alert.service';
import { FormsModule, NgForm } from '@angular/forms';
import { ImagePipe } from '../../../pipes/image.pipe';
import { NotProfileDirective } from '../../../directives/not-profile.directive';
import { Router } from '@angular/router';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-my-profile',
  standalone: true,
  imports: [TemplateComponent, CommonModule, FormsModule, ImagePipe, NotProfileDirective, MatIcon],
  templateUrl: './my-profile.component.html',
  styleUrl: './my-profile.component.scss'
})
export class MyProfileComponent {

  user: any = {};
  selectedImage: File | null = null;
  previewImage: string | null = null;
  password:string = ''
  showPassword=false;


  constructor(private _userService:UserService, private _localStorage:LocalStorageService,
    private _alertService:AlertService, private _router:Router
  ){}
  
  ngOnInit(){

    this._userService.getByUsername(this._localStorage.getItem(this._localStorage.USER_NAME)).subscribe(response=>{
      next:
        this.user = response
    })
    
  }

  onImageSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      if (!file.type.startsWith('image/')) {
        this._alertService.warning('Archivo inválido', 'Solo se permiten imágenes.');
        return;
      }
  
      this.selectedImage = file;
  
      const reader = new FileReader();
      reader.onload = () => {
        this.previewImage = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }
  
  updatePhoto() {
    if (!this.selectedImage || !this.user?.username) return;
  
    const formData = new FormData();
    formData.append('file', this.selectedImage);
  
    this._userService.updatePhotoPath(formData, this.user.id).subscribe({
      next: (res) => {
        this.user.photo = res.photo;
        this.previewImage = null;
        this.selectedImage = null;
        this._localStorage.setItem(this._localStorage.USER_PHOTO, this.user.photo)
        this._alertService.success('Foto actualizada', 'Se actualizó la foto de perfil correctamente.');
        this.reloadPage()
      },
      error: (err) => {
        this._alertService.error('Error', 'No se pudo actualizar la foto');
      }
    });
  }
  
  onUpdateUser(form: NgForm) {
    if (!form.dirty) {
      this._alertService.warning('Sin cambios', 'No hay cambios para guardar.');
      return;
    }

    this._userService.updateUserMyProfile(this.user).subscribe({
      next: (updated) => {
        this.user = updated;
        this._alertService.success('Actualizado', 'Información actualizada correctamente.');
        this.password = ''
      },
      error: (err) => {
        this._alertService.error('Error', 'No se pudo actualizar la información.');
      }
    });
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  reloadPage() {
    const currentUrl = this._router.url;

    this._router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this._router.navigateByUrl(currentUrl);
    });
  }

}
