import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';
import { UploadService } from '../../../../services/upload/upload.service';
import { CommonService } from '../../../../services/commons/common.service';
import { LocalStorageService } from '../../../../services/commons/local-storage.service';
import { ColumnDefinition, generateReportPDF } from '../../../../utils/pdf-generator.utils';
import { MatIcon } from '@angular/material/icon';

export interface TopProjectByCompletedCasesDto {
  projectId: number;
  projectName: string;
  totalCases: number;
}

@Component({
  selector: 'app-top-project-completed',
  standalone: true,
  imports: [CommonModule, TemplateComponent, MatIcon],
  templateUrl: './top-project-completed.component.html',
  styleUrl: './top-project-completed.component.scss'
})
export class TopProjectCompletedComponent implements OnInit {
  report!: TopProjectByCompletedCasesDto;
  loaded = false;

  constructor(private _reportService: ReportService,
      private _uploadService: UploadService,
      private _commonService: CommonService,
      private _localStorageService: LocalStorageService
  ) {}

  ngOnInit(): void {
    this._reportService.getReport10().subscribe({
      next: (data) => {
        this.report = data;
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
      projectId: this.report.projectId,
      projectName: this.report.projectName,
      totalCases: this.report.totalCases
    }];
  
    const columns: ColumnDefinition<typeof dataForReport[0]>[] = [
      { header: 'ID Proyecto', field: 'projectId' },
      { header: 'Nombre del Proyecto', field: 'projectName' },
      { header: 'Casos Finalizados', field: 'totalCases' }
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
                  { label: 'Casos finalizados', value: this.report.totalCases }
                ]
              );
            } catch (err) {
              console.error('Error al generar PDF:', err);
            }
          },
          error: (err) => console.error('Error al obtener logo:', err)
        });
      },
      error: (err) => console.error('Error al obtener info empresa:', err)
    });
  }
  
}
