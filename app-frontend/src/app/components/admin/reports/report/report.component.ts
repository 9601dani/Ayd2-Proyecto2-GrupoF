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
      route: '/reports/time-cost-by-project'
    },
    {
      title: 'Horas y dinero por desarrollador',
      description: 'Visualiza cuánto ha trabajado y ganado un desarrollador.',
      route: '/reports/time-cost-by-user'
    },
    {
      title: 'Horas y dinero por tipo de caso',
      description: 'Analiza inversión según el tipo de caso.',
      route: '/reports/time-cost-by-case-type'
    },
    {
      title: 'Horas y dinero por intervalo de tiempo',
      description: 'Selecciona un rango de fechas para el análisis.',
      route: '/reports/time-cost-by-date'
    },
    {
      title: 'Reporte de desarrolladores',
      description: 'Con opción de aplicar múltiples filtros.',
      route: '/reports/users'
    },
    {
      title: 'Reporte de proyectos',
      description: 'Con opción de aplicar múltiples filtros.',
      route: '/reports/projects'
    },
    {
      title: 'Top desarrollador por casos',
      description: 'Desarrollador que ha atendido más casos.',
      route: '/reports/top-user-by-cases'
    },
    {
      title: 'Top desarrollador por dinero',
      description: 'Desarrollador que más ha ganado resolviendo casos.',
      route: '/reports/top-user-by-pay'
    },
    {
      title: 'Proyecto con más casos finalizados',
      description: 'El proyecto más exitoso en términos de resolución.',
      route: '/reports/top-project-completed'
    },
    {
      title: 'Proyecto con más casos cancelados',
      description: 'Visualiza el proyecto con más cancelaciones.',
      route: '/reports/top-project-cancelled'
    },
    {
      title: 'Casos por proyecto',
      description: 'Visualiza los casos registrados por proyecto.',
      route: '/reports/cases-by-project'
    },
    {
      title: 'Casos por desarrollador',
      description: 'Casos asignados a un desarrollador específico.',
      route: '/reports/cases-by-user'
    },
    {
      title: 'Casos por tipo',
      description: 'Casos clasificados por tipo.',
      route: '/reports/cases-by-type'
    }
  ];

  goTo(path: string) {
    this.router.navigateByUrl(path);
  }
}
