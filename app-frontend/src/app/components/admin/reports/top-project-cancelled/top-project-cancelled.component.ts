import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';
import { MatIcon } from '@angular/material/icon';
import { UploadService } from '../../../../services/upload/upload.service';
import { LocalStorageService } from '../../../../services/commons/local-storage.service';
import { CommonService } from '../../../../services/commons/common.service';
import { ColumnDefinition, generateReportPDF } from '../../../../utils/pdf-generator.utils';

export interface TopProjectByCancelledCasesDto {
  projectId: number;
  projectName: string;
  totalCancelledCases: number;
}

@Component({
  selector: 'app-top-project-cancelled',
  standalone: true,
  imports: [CommonModule, TemplateComponent, MatIcon],
  templateUrl: './top-project-cancelled.component.html',
  styleUrl: './top-project-cancelled.component.scss'
})
export class TopProjectCancelledComponent implements OnInit {
  report!: TopProjectByCancelledCasesDto;
  loaded = false;

  constructor(private _reportService: ReportService,
    private _uploadService: UploadService,
    private _localStorageService: LocalStorageService,
    private _commonService: CommonService
  ) {}

  ngOnInit(): void {
    this._reportService.getReport11().subscribe({
      next: (data) => {
        this.report = data;
        this.loaded = true;
      },
      error: (err) => console.error(err)
    });
  }

  async exportToPDF(): Promise<void> {
    const logoId = this._localStorageService.getItem(this._localStorageService.COMPANY_LOGO);
    const currency = this._localStorageService.getItem(this._localStorageService.COMPANY_CURRENCY);
    const username = this._localStorageService.getItem(this._localStorageService.USER_NAME);
  
    const fecha = new Date();
    const fechaFormateada = `${fecha.getDate().toString().padStart(2, '0')}/${
      (fecha.getMonth() + 1).toString().padStart(2, '0')
    }/${fecha.getFullYear()}`;
  
    const dataForReport = [
      {
        projectId: this.report.projectId,
        projectName: this.report.projectName,
        totalCancelledCases: this.report.totalCancelledCases
      }
    ];
  
    const columns: ColumnDefinition<typeof dataForReport[0]>[] = [
      { header: 'ID del Proyecto', field: 'projectId' },
      { header: 'Nombre del Proyecto', field: 'projectName' },
      { header: 'Casos Cancelados', field: 'totalCancelledCases' }
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
                  {
                    label: 'Total Casos Cancelados',
                    value: this.report.totalCancelledCases,
                    isCurrency: false
                  }
                ]
              );
            } catch (err) {
              console.error('Error al generar PDF:', err);
            }
          },
          error: (err) => console.error('Error obteniendo logo:', err)
        });
      },
      error: (err) => console.error('Error obteniendo info empresa:', err)
    });
  }
  
}
