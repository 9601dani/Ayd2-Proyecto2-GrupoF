import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TemplateComponent } from '../../commons/template/template.component';
import { ModalComponent } from '../../commons/modal/modal.component';
import { CommonService } from '../../../services/commons/common.service';
import { UserFormComponent } from '../user-form/user-form.component';
import Swal from 'sweetalert2';
import { AlertService } from '../../../services/commons/alert.service';
import { UserService } from '../../../services/user/user.service';

interface User {
  id: number;
  username: string;
  email: string;
  salaryPerHour: number;
  isEnabled: boolean;
  firstName: string;
  lastName: string;
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, TemplateComponent, ModalComponent, UserFormComponent],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  users: User[] = [];

  roles = [];

  actualAction: 'edit' | 'delete' | 'register'| 'enable' | null = null;
  userSelected: any = null;

  constructor(private _commonService: CommonService, private _userService:UserService,
    private _alertService: AlertService
  ) {}

  ngOnInit(){
    this._userService.getAllUsers().subscribe(response =>{
      next:
        this.users = response
    })

    this._userService.getAllRoles().subscribe(responde =>{
      next:
      this.roles = responde
    })

  }

  openModal(action: 'edit' | 'delete' | 'register'| 'enable', user: any = null) {
    this.actualAction = action;
    this.userSelected = user;
    this._commonService.emitActiveModal(true);
  }

  closeModal() {
    this._commonService.emitActiveModal(false);
    this.actualAction = null;
    this.userSelected = null;
  }

  deleteUser() {
    if (this.userSelected) {
      this._userService.disabledUser(this.userSelected.username).subscribe({
        next: (deletedUser) => {
          const index = this.users.findIndex(u => u.id === deletedUser.id);
          if (index !== -1) {
            this.users[index] = deletedUser;
          }
          this._alertService.success(
            "Actualización exitosa",
            "Se desactivo el usuario " + deletedUser.username + " exitosamente"
          );
        },
        error: (err) => {
          this._alertService.error("Error al actualizar", err.message || "Ocurrió un error");
        },
        complete: () => {
          this.closeModal();
        }
      });
    }
  }

  onRegister(data: any) {
    this._userService.registerUser(data).subscribe({
      next: (newUser) => {
        this.users.push(newUser);
        this._alertService.success(
          "Registro exitoso",
          "Se registró el usuario " + newUser.username + " exitosamente"
        );
      },
      error: (err) => {
        this._alertService.error(
          "Error al registrar",
          err?.error?.message || "Ocurrió un error inesperado"
        );
      },
      complete: () => {
        this.closeModal();
      }
    });
  }
  

  onEdit(userData: any) {
    this._userService.updateUser(userData).subscribe({
      next: (updatedUser) => {
        const index = this.users.findIndex(u => u.id === updatedUser.id);
        if (index !== -1) {
          this.users[index] = updatedUser;
        }
        this._alertService.success(
          "Actualización exitosa",
          "Se actualizó el usuario " + updatedUser.username + " exitosamente"
        );
      },
      error: (err) => {
        this._alertService.error("Error al actualizar", err.message || "Ocurrió un error");
      },
      complete: () => {
        this.closeModal();
      }
    });
  }

  enableUser() {
    if (this.userSelected) {
      this._userService.enableUser(this.userSelected.username).subscribe({
        next: (enabledUser) => {
          const index = this.users.findIndex(u => u.id === enabledUser.id);
          if (index !== -1) {
            this.users[index] = enabledUser;
          }
          this._alertService.success(
            "Activación exitosa",
            "Se activó el usuario " + enabledUser.username + " exitosamente"
          );
          this.closeModal();
        },
        error: (err) => {
          this._alertService.error("Error al activar", err.message || "Ocurrió un error");
        }
      });
    }
  }
  
  
}
