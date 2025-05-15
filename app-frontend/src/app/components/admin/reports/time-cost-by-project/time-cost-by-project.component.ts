import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../../../services/report/report.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TemplateComponent } from '../../../commons/template/template.component';
import { ProjectService } from '../../../../services/project/project.service';
import { UploadService } from '../../../../services/upload/upload.service';
import { LocalStorageService } from '../../../../services/commons/local-storage.service';
import { CommonService } from '../../../../services/commons/common.service';
import { ColumnDefinition, generateReportPDF } from '../../../../utils/pdf-generator.utils';
import { MatIcon } from '@angular/material/icon';

export interface Report2Dto {
  projectId: number;
  projectName: string;
  totalHours: number;
  totalInvested: number;
}


@Component({
  selector: 'app-time-cost-by-project',
  standalone: true,
  imports: [CommonModule, TemplateComponent, FormsModule, MatIcon],
  templateUrl: './time-cost-by-project.component.html',
  styleUrl: './time-cost-by-project.component.scss'
})
export class TimeCostByProjectComponent implements OnInit {
  report: Report2Dto[] = [];
  projectId: number | undefined;
  projects: any = []

  constructor(private _reportService: ReportService, private _projectService: ProjectService,
    private _uploadService: UploadService,
    private _localStorageService: LocalStorageService,
    private _commonService: CommonService
  ) {}

  ngOnInit(): void {
    this.loadReport();

    this._projectService.getAllProjects().subscribe({
      next: (value) => {
        this.projects = value;
      },
    })
  }

  loadReport(): void {
    this._reportService.getReport2(this.projectId).subscribe({
      next: (data) => (this.report = data),
      error: (err) => console.error(err),
    });
  }

  async generarPDF(): Promise<void> {
    const logoId = this._localStorageService.getItem(this._localStorageService.COMPANY_LOGO);
    const currency = this._localStorageService.getItem(this._localStorageService.COMPANY_CURRENCY);
    const username = this._localStorageService.getItem(this._localStorageService.USER_NAME);
  
    const fechaActual = new Date();
    const fechaFormateada = `${fechaActual.getDate().toString().padStart(2, '0')}/${
      (fechaActual.getMonth() + 1).toString().padStart(2, '0')
    }/${fechaActual.getFullYear()}`;
  
    const dataForReport = this.report.map(r => ({
      projectId: r.projectId,
      projectName: r.projectName,
      totalHours: r.totalHours.toFixed(2),
      totalInvestedFormatted: `${currency} ${r.totalInvested.toFixed(2)}`
    }));
  
    const columns: ColumnDefinition<typeof dataForReport[0]>[] = [
      { header: 'ID', field: 'projectId' },
      { header: 'Nombre', field: 'projectName' },
      { header: 'Total Horas', field: 'totalHours' },
      { header: 'Total Invertido', field: 'totalInvestedFormatted' }
    ];
  
    const totalHours = this.report.reduce((sum, r) => sum + r.totalHours, 0);
    const totalInvested = this.report.reduce((sum, r) => sum + r.totalInvested, 0);
  
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
                  { label: 'Total de Proyectos', value: this.report.length },
                  { label: 'Total Horas', value: totalHours },
                  { label: 'Total Invertido', value: totalInvested, isCurrency: true }
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