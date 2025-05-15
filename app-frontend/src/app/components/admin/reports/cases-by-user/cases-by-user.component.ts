import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportService } from '../../../../services/report/report.service';
import { UserService } from '../../../../services/user/user.service';
import { TemplateComponent } from '../../../commons/template/template.component';
import { MatIcon } from '@angular/material/icon';
import { UploadService } from '../../../../services/upload/upload.service';
import { CommonService } from '../../../../services/commons/common.service';
import { LocalStorageService } from '../../../../services/commons/local-storage.service';
import { ColumnDefinition, generateReportPDF } from '../../../../utils/pdf-generator.utils';

export interface CaseUserReportDto {
  caseId: number;
  caseName: string;
  description: string;
  caseTypeId: number;
  caseTypeName: string;
  createdAt: string;
  limitDate: string;
  userId: number;
}


@Component({
  selector: 'app-cases-by-user',
  standalone: true,
  imports: [CommonModule, FormsModule, TemplateComponent,MatIcon],
  templateUrl: './cases-by-user.component.html',
  styleUrl: './cases-by-user.component.scss'
})
export class CasesByUserComponent implements OnInit {
  users: any[] = [];
  selectedUserId: number | undefined;
  report: CaseUserReportDto[] = [];

  constructor(
    private _reportService: ReportService,
    private _userService: UserService,
    private _uploadService:UploadService,
    private _commonService:CommonService,
    private _localStorageService:LocalStorageService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
    this.loadReport();
  }

  loadUsers(): void {
    this._userService.getUsersByRole(2).subscribe({
      next: (data) => (this.users = data),
      error: (err) => console.error(err)
    });
  }

  loadReport(): void {
    this._reportService.getReport13(this.selectedUserId).subscribe({
      next: (data) => (this.report = data),
      error: (err) => console.error(err)
    });
  }

  async exportPdf(): Promise<void> {
    if (this.report.length === 0) return;
  
    const logoId = this._localStorageService.getItem(this._localStorageService.COMPANY_LOGO);
    const currency = this._localStorageService.getItem(this._localStorageService.COMPANY_CURRENCY); // opcional
    const username = this._localStorageService.getItem(this._localStorageService.USER_NAME);
  
    const now = new Date();
    const fechaFormateada = `${now.getDate().toString().padStart(2, '0')}/${
      (now.getMonth() + 1).toString().padStart(2, '0')
    }/${now.getFullYear()}`;

    const developerName = this.selectedUserId
      ? this.users.find(u => u.id === this.selectedUserId)?.username || 'Desarrollador desconocido'
      : 'Todos los desarrolladores';
  
    const dataForReport = this.report.map(c => ({
      caseId: c.caseId,
      caseName: c.caseName,
      description: c.description,
      caseTypeName: c.caseTypeName,
      createdAt: new Date(c.createdAt).toLocaleDateString(),
      limitDate: new Date(c.limitDate).toLocaleDateString()
    }));
  
    const columns: ColumnDefinition<typeof dataForReport[0]>[] = [
      { header: 'ID', field: 'caseId' },
      { header: 'Nombre del caso', field: 'caseName' },
      { header: 'Descripción', field: 'description' },
      { header: 'Tipo de caso', field: 'caseTypeName' },
      { header: 'Fecha creación', field: 'createdAt' },
      { header: 'Fecha límite', field: 'limitDate' },
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
                  { label: 'Desarrollador', value: developerName },
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
}
