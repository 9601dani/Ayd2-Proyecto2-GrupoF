import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';
import { MatIcon } from '@angular/material/icon';
import { LocalStorageService } from '../../../../services/commons/local-storage.service';
import { CommonService } from '../../../../services/commons/common.service';
import { UploadService } from '../../../../services/upload/upload.service';
import { ColumnDefinition, generateReportPDF } from '../../../../utils/pdf-generator.utils';

export interface ProjectResponseWithoutUser {
  id: number;
  name: string;
  description: string;
  isEnabled: boolean;
  fkUser: number;
  username: string
}


@Component({
  selector: 'app-projects-report',
  standalone: true,
  imports: [CommonModule, TemplateComponent, FormsModule, MatIcon],
  templateUrl: './projects-report.component.html',
  styleUrl: './projects-report.component.scss'
})
export class ProjectsReportComponent implements OnInit {
  allProjects: ProjectResponseWithoutUser[] = [];
  filteredProjects: ProjectResponseWithoutUser[] = [];

  search: string = '';
  stateFilter: boolean | null = null;

  constructor(private _reportService: ReportService,
    private _localStorageService:LocalStorageService,
    private _commonService: CommonService,
    private _uploadService:UploadService
  ) {}

  ngOnInit(): void {
    this._reportService.getReport7().subscribe({
      next: (data) => {
        this.allProjects = data;
        this.applyFilters();
      },
      error: (err) => console.error(err),
    });
  }

  applyFilters(): void {
    this.filteredProjects = this.allProjects.filter((p) => {
      const matchState = this.stateFilter === null || p.isEnabled === this.stateFilter;
      const matchSearch =
        this.search.trim() === '' ||
        p.name.toLowerCase().includes(this.search.toLowerCase()) ||
        p.description.toLowerCase().includes(this.search.toLowerCase());

      return matchState && matchSearch;
    });
  }
  async exportPdf(): Promise<void> {
    if (this.filteredProjects.length === 0) return;
  
    const logoId = this._localStorageService.getItem(this._localStorageService.COMPANY_LOGO);
    const currency = this._localStorageService.getItem(this._localStorageService.COMPANY_CURRENCY);
    const username = this._localStorageService.getItem(this._localStorageService.USER_NAME);
  
    const now = new Date();
    const fechaFormateada = `${now.getDate().toString().padStart(2, '0')}/${
      (now.getMonth() + 1).toString().padStart(2, '0')
    }/${now.getFullYear()}`;
  
    const dataForReport = this.filteredProjects.map(p => ({
      id: p.id,
      name: p.name,
      description: p.description,
      state: p.isEnabled ? 'Activo' : 'Inactivo',
      username: p.username
    }));
  
    const columns: ColumnDefinition<typeof dataForReport[0]>[] = [
      { header: 'ID', field: 'id' },
      { header: 'Nombre', field: 'name' },
      { header: 'Descripción', field: 'description' },
      { header: 'Estado', field: 'state' },
      { header: 'Usuario Asignado', field: 'username' }
    ];
  
    const totalActivos = this.filteredProjects.filter(p => p.isEnabled).length;
    const totalInactivos = this.filteredProjects.filter(p => !p.isEnabled).length;
  
    this._commonService.getCompanyInfo().subscribe({
      next: (companyInfo) => {
        this._uploadService.getImageBase64(logoId).subscribe({
          next: async (logoBase64: string) => {
            try {
              await generateReportPDF(
                columns,
                dataForReport,
                companyInfo,
                logoBase64,
                currency,
                fechaFormateada,
                username,
                [
                  { label: 'Total Proyectos Activos', value: totalActivos },
                  { label: 'Total Proyectos Inactivos', value: totalInactivos },
                  { label: 'Total Proyectos', value: this.filteredProjects.length }
                ]
              );
            } catch (err) {
              console.error('Error al generar PDF:', err);
            }
          },
          error: (err) => {
            console.error('Error al obtener el logo en base64:', err);
          }
        });
      },
      error: (err) => {
        console.error('Error al obtener la info de la empresa:', err);
      }
    });
  }
  
}
