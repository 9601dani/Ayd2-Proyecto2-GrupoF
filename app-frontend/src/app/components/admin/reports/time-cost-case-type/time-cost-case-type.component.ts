import { Component, OnInit } from '@angular/core';
import { TemplateComponent } from '../../../commons/template/template.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CaseType } from '../../../../models/CasePhase.model';
import { ProjectService } from '../../../../services/project/project.service';
import { ReportService } from '../../../../services/report/report.service';
import { UploadService } from '../../../../services/upload/upload.service';
import { LocalStorageService } from '../../../../services/commons/local-storage.service';
import { CommonService } from '../../../../services/commons/common.service';
import { ColumnDefinition, generateReportPDF } from '../../../../utils/pdf-generator.utils';
import { MatIcon } from '@angular/material/icon';

export interface Report4Dto {
  typeId: number;
  typeName: string;
  totalHours: number;
  totalInvested: number;
}


@Component({
  selector: 'app-time-cost-case-type',
  standalone: true,
  imports: [CommonModule, FormsModule, TemplateComponent, MatIcon],
  templateUrl: './time-cost-case-type.component.html',
  styleUrl: './time-cost-case-type.component.scss'
})
export class TimeCostCaseTypeComponent implements OnInit {
  report: Report4Dto[] = [];
  caseTypes: CaseType[] = [];
  selectedTypeId: number | undefined;

  constructor(
    private _reportService: ReportService,
    private _projectService: ProjectService,
    private _uploadService: UploadService,
    private _localStorageService: LocalStorageService,
    private _commonService: CommonService
  ) {}

  ngOnInit(): void {
    this.loadCaseTypes();
    this.loadReport();
  }

  loadCaseTypes(): void {
    this._projectService.getAllCaseTypesWithPhases().subscribe({
      next: (data) => (this.caseTypes = data),
      error: (err) => console.error(err),
    });
  }

  loadReport(): void {
    this._reportService.getReport4(this.selectedTypeId).subscribe({
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
      typeId: r.typeId,
      typeName: r.typeName,
      totalHours: r.totalHours.toFixed(2),
      totalInvestedFormatted: `${currency} ${r.totalInvested.toFixed(2)}`
    }));
  
    const columns: ColumnDefinition<typeof dataForReport[0]>[] = [
      { header: 'ID Tipo', field: 'typeId' },
      { header: 'Nombre', field: 'typeName' },
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
                  { label: 'Total Horas', value: totalHours, isCurrency: false },
                  { label: 'Total Invertido', value: totalInvested, isCurrency: true }
                ]
              );
            } catch (err) {
              console.error('Error al generar PDF:', err);
            }
          },
          error: (err) => {
            console.error('Error al obtener logo base64:', err);
          }
        });
      },
      error: (err) => {
        console.error('Error al obtener datos de la empresa:', err);
      }
    });
  }
  
}
