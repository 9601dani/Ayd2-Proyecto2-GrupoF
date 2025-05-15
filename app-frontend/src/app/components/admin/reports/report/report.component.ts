import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { TemplateComponent } from '../../../commons/template/template.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-report',
  standalone: true,
  imports: [CommonModule, TemplateComponent],
  templateUrl: './report.component.html',
  styleUrl: './report.component.scss'
})
export class ReportComponent {

  constructor(private router: Router) {}

  reports = [
    {
      title: 'Cantidad de casos por proyecto',
      description: 'Con opción de filtrar por estado (activo/inactivo).',
      route: '/projects-cases'
    },
    {
      title: 'Horas y dinero por proyecto',
      description: 'Reporte del tiempo y costo invertido en un proyecto específico.',
      route: '/time-cost-by-project'
    },
    {
      title: 'Horas y dinero por desarrollador',
      description: 'Visualiza cuánto ha trabajado y ganado un desarrollador.',
      route: '/time-cost-by-user'
    },
    {
      title: 'Horas y dinero por tipo de caso',
      description: 'Analiza inversión según el tipo de caso.',
      route: '/time-cost-by-case-type'
    },
    {
      title: 'Horas y dinero por intervalo de tiempo',
      description: 'Selecciona un rango de fechas para el análisis.',
      route: '/time-cost-by-date'
    },
    {
      title: 'Reporte de desarrolladores',
      description: 'Con opción de aplicar múltiples filtros.',
      route: '/users-report'
    },
    {
      title: 'Reporte de proyectos',
      description: 'Con opción de aplicar múltiples filtros.',
      route: '/projects-report'
    },
    {
      title: 'Top desarrollador por casos',
      description: 'Desarrollador que ha atendido más casos.',
      route: '/top-user-by-cases'
    },
    {
      title: 'Top desarrollador por dinero',
      description: 'Desarrollador que más ha ganado resolviendo casos.',
      route: '/top-user-by-pay'
    },
    {
      title: 'Proyecto con más casos finalizados',
      description: 'El proyecto más exitoso en términos de resolución.',
      route: '/top-project-completed'
    },
    {
      title: 'Proyecto con más casos cancelados',
      description: 'Visualiza el proyecto con más cancelaciones.',
      route: '/top-project-cancelled'
    },
    {
      title: 'Casos por proyecto',
      description: 'Visualiza los casos registrados por proyecto.',
      route: '/cases-by-project'
    },
    {
      title: 'Casos por desarrollador',
      description: 'Casos asignados a un desarrollador específico.',
      route: '/cases-by-user'
    },
    {
      title: 'Casos por tipo',
      description: 'Casos clasificados por tipo.',
      route: '/cases-by-type'
    }
  ];

  goTo(path: string) {
    this.router.navigateByUrl(path);
  }
}
