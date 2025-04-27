import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

export interface Page {
  id: number;
  name: string;
  path: string;
}

export interface Module {
  id: number;
  name: string;
  path: string;
  pages: Page[];
}

@Injectable({
  providedIn: 'root'
})
export class MenuService {

  constructor() {}

  getMenuByRole(roleId: number): Observable<Module[]> {
    const menu: Module[] = [
      {
        id: 1,
        name: 'Dashboard',
        path: '/dashboard',
        pages: [
          { id: 1, name: 'Inicio', path: '/dashboard/home' },
          { id: 2, name: 'Reportes', path: '/dashboard/reports' }
        ]
      },
      {
        id: 2,
        name: 'Usuarios',
        path: '/usuarios',
        pages: [
          { id: 3, name: 'Lista de Usuarios', path: '/usuarios/list' }
        ]
      }
    ];

    return of(menu);
  }
}
