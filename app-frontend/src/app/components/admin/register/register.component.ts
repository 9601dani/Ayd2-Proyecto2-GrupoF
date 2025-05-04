import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TemplateComponent } from '../../commons/template/template.component';
import { ModalComponent } from '../../commons/modal/modal.component';
import { CommonService } from '../../../services/commons/common.service';
import { UserFormComponent } from '../user-form/user-form.component';

interface User {
  id: number;
  username: string;
  email: string;
  salary_per_hour: number;
  is_enabled: boolean;
  first_name: string;
  last_name: string;
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, TemplateComponent, ModalComponent, UserFormComponent],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  users: User[] = [
    {
      id: 1,
      username: 'admin',
      email: 'admin@example.com',
      salary_per_hour: 25.5,
      is_enabled: true,
      first_name: 'Juan',
      last_name: 'Pérez'
    },
    {
      id: 2,
      username: 'user123',
      email: 'user123@example.com',
      salary_per_hour: 15,
      is_enabled: false,
      first_name: 'Ana',
      last_name: 'Gómez'
    }
  ];

  roles = [
    { id: 1, name: 'Administrador' },
    { id: 2, name: 'Desarrollador' }
  ];

  actualAction: 'edit' | 'delete' | 'register' | null = null;
  userSelected: any = null;

  constructor(private _commonService: CommonService) {}

  openModal(action: 'edit' | 'delete' | 'register', user: any = null) {
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
      //TODO logica para eliminar usuarios
      console.log('deleted user ', this.userSelected.first_name)
      this.closeModal()
    }
  }

  onRegister(data: any) {
    const newUser = {
      ...data,
      id: Math.max(...this.users.map(u => u.id)) + 1
    };
    this.users.push(newUser);
    this.closeModal();
  }

  onEdit(userData: any) {
    console.log('Usuario editado:', userData);
    // TODO: llamar al servicio para actualizar en la BD
    this.closeModal();
  }
}
