import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';
import { LocalStorageService } from '../../../../services/commons/local-storage.service';
import { CommonService } from '../../../../services/commons/common.service';
import { UploadService } from '../../../../services/upload/upload.service';
import { ColumnDefinition, generateReportPDF } from '../../../../utils/pdf-generator.utils';
import { MatIcon } from '@angular/material/icon';

export interface Report8Dto {
  userId: number;
  userName: string;
  salaryPerHour: number;
  cases: number;
}


@Component({
  selector: 'app-top-user-by-cases',
  standalone: true,
  imports: [CommonModule, TemplateComponent,MatIcon],
  templateUrl: './top-user-by-cases.component.html',
  styleUrl: './top-user-by-cases.component.scss'
})
export class TopUserByCasesComponent implements OnInit {
  userReport!: Report8Dto;
  loaded = false;

  constructor(private _reportService: ReportService,
     private _localStorageService:LocalStorageService,
        private _commonService:CommonService,
        private _uploadService:UploadService
  ) {}

  ngOnInit(): void {
    this._reportService.getReport8().subscribe({
      next: (data) => {
        this.userReport = data;
        this.loaded = true;
      },
      error: (err) => console.error(err)
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
  
    const dataForReport = [{
      userId: this.userReport.userId,
      userName: this.userReport.userName,
      salaryFormatted: `${currency} ${this.userReport.salaryPerHour.toFixed(2)}`,
      cases: this.userReport.cases
    }];
  
    const columns: ColumnDefinition<typeof dataForReport[0]>[] = [
      { header: 'ID', field: 'userId' },
      { header: 'Usuario', field: 'userName' },
      { header: 'Salario por hora', field: 'salaryFormatted' },
      { header: 'Casos atendidos', field: 'cases' }
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
                  { label: 'Total de Casos Atendidos', value: this.userReport.cases }
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
        console.error('Error al obtener información de la empresa:', err);
      }
    });
  }
  
}
