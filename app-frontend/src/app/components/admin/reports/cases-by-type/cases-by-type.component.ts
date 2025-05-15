import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CaseType } from '../../../../models/CasePhase.model';
import { ProjectService } from '../../../../services/project/project.service';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';
import { MatIcon } from '@angular/material/icon';
import { CommonService } from '../../../../services/commons/common.service';
import { LocalStorageService } from '../../../../services/commons/local-storage.service';
import { UploadService } from '../../../../services/upload/upload.service';
import { ColumnDefinition, generateReportPDF } from '../../../../utils/pdf-generator.utils';

export interface CaseUserReportDto {
  caseId: number;
  caseName: string;
  description: string;
  caseTypeId: number;
  caseTypeName: string;
  createdAt: string;
  limitDate: string;
}


@Component({
  selector: 'app-cases-by-type',
  standalone: true,
  imports: [CommonModule, FormsModule, TemplateComponent, MatIcon],
  templateUrl: './cases-by-type.component.html',
  styleUrl: './cases-by-type.component.scss'
})
export class CasesByTypeComponent implements OnInit {
  caseTypes: CaseType[] = [];
  selectedTypeId: number | undefined;
  report: CaseUserReportDto[] = [];

  constructor(
    private _reportService: ReportService,
    private _projectService: ProjectService,
    private _commonService:CommonService,
    private _localStorageService:LocalStorageService,
    private _uploadService:UploadService
  ) {}

  ngOnInit(): void {
    this.loadCaseTypes();
    this.loadReport();
  }

  loadCaseTypes(): void {
    this._projectService.getAllCaseTypesWithPhases().subscribe({
      next: (data) => (this.caseTypes = data),
      error: (err) => console.error(err)
    });
  }

  loadReport(): void {
    this._reportService.getReport14(this.selectedTypeId).subscribe({
      next: (data) => {
        this.report = this.removeDuplicateCases(data);
      },
      error: (err) => console.error(err)
    });
  }

  async exportPdf(): Promise<void> {
    if (this.report.length === 0) return;
  
    const logoId = this._localStorageService.getItem(this._localStorageService.COMPANY_LOGO);
    const currency = this._localStorageService.getItem(this._localStorageService.COMPANY_CURRENCY);
    const username = this._localStorageService.getItem(this._localStorageService.USER_NAME);
  
    const now = new Date();
    const fechaFormateada = `${now.getDate().toString().padStart(2, '0')}/${
      (now.getMonth() + 1).toString().padStart(2, '0')
    }/${now.getFullYear()}`;
  
    const caseTypeName = this.selectedTypeId
      ? this.caseTypes.find(t => t.id === this.selectedTypeId)?.name || 'Tipo desconocido'
      : 'Todos los tipos';
  
    const dataForReport = this.report.map(r => ({
      caseId: r.caseId,
      caseName: r.caseName,
      description: r.description,
      caseTypeName: r.caseTypeName,
      createdAt: new Date(r.createdAt).toLocaleDateString(),
      limitDate: new Date(r.limitDate).toLocaleDateString(),
    }));
  
    const columns: ColumnDefinition<typeof dataForReport[0]>[] = [
      { header: 'ID', field: 'caseId' },
      { header: 'Nombre del caso', field: 'caseName' },
      { header: 'Descripción', field: 'description' },
      { header: 'Tipo', field: 'caseTypeName' },
      { header: 'Fecha creación', field: 'createdAt' },
      { header: 'Fecha límite', field: 'limitDate' }
    ];
  
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
                  { label: 'Tipo de Caso', value: caseTypeName },
                  { label: 'Total de Casos', value: this.report.length }
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
  private removeDuplicateCases(data: any[]): any[] {
    const seen = new Set<number>();
    return data.filter(item => {
      if (seen.has(item.caseId)) {
        return false; 
      } else {
        seen.add(item.caseId);
        return true;
      }
    });
  }
  
}
