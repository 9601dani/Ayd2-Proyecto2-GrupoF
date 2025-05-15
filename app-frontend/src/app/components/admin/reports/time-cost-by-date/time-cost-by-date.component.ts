import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';
import { LocalStorageService } from '../../../../services/commons/local-storage.service';
import { UploadService } from '../../../../services/upload/upload.service';
import { CommonService } from '../../../../services/commons/common.service';
import { ColumnDefinition, generateReportPDF } from '../../../../utils/pdf-generator.utils';
import { MatIcon } from '@angular/material/icon';

export interface Report5Dto {
  totalHours: number;
  totalInvested: number;
}


@Component({
  selector: 'app-time-cost-by-date',
  standalone: true,
  imports: [CommonModule, FormsModule, TemplateComponent,MatIcon],
  templateUrl: './time-cost-by-date.component.html',
  styleUrl: './time-cost-by-date.component.scss'
})
export class TimeCostByDateComponent {
  dateInit: string = '';
  dateEnd: string = '';
  report: Report5Dto | null = null;

  currency!:String

  constructor(private _reportService: ReportService,
    private _localStorageService:LocalStorageService,
    private _uploadService: UploadService,
    private _commonService: CommonService
  ) {}

  loadReport(): void {
    const init = this.dateInit ? `${this.dateInit}T00:00:00` : undefined;
    const end = this.dateEnd ? `${this.dateEnd}T23:59:59` : undefined;

    this.currency= this._localStorageService.getItem(this._localStorageService.COMPANY_CURRENCY)

    this._reportService.getReport5(init, end).subscribe({
      next: (data) => (this.report = data),
      error: (err) => console.error(err)
    });
  }

  async exportPdf(): Promise<void> {
    if (!this.report) return;
  
    const logoId = this._localStorageService.getItem(this._localStorageService.COMPANY_LOGO);
    const currency = this._localStorageService.getItem(this._localStorageService.COMPANY_CURRENCY);
    const username = this._localStorageService.getItem(this._localStorageService.USER_NAME);
  
    const fechaActual = new Date();
    const fechaFormateada = `${fechaActual.getDate().toString().padStart(2, '0')}/${
      (fechaActual.getMonth() + 1).toString().padStart(2, '0')
    }/${fechaActual.getFullYear()}`;

    const displayDateInit = this.dateInit ? this.dateInit : '-----';
    const displayDateEnd = this.dateEnd ? this.dateEnd : fechaFormateada;

    const dataForReport = [{
      dateInit: displayDateInit,
      dateEnd: displayDateEnd,
      totalHours: this.report.totalHours.toFixed(2),
      totalInvestedFormatted: `${currency} ${this.report.totalInvested.toFixed(2)}`
    }];

  
    const columns: ColumnDefinition<typeof dataForReport[0]>[]  = [
      { header: 'Fecha de Inicio', field: 'dateInit' },
      { header: 'Fecha de Fin', field: 'dateEnd' },
      { header: 'Total de Horas', field: 'totalHours' },
      { header: 'Total Invertido', field: 'totalInvestedFormatted' }
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
                  { label: 'Total de Horas', value: this.report!.totalHours },
                  { label: 'Total Invertido', value: this.report!.totalInvested, isCurrency: true }
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
