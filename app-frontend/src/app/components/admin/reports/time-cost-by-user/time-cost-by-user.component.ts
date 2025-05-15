import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportService } from '../../../../services/report/report.service';
import { UserService } from '../../../../services/user/user.service';
import { TemplateComponent } from '../../../commons/template/template.component';
import { ColumnDefinition, generateReportPDF } from '../../../../utils/pdf-generator.utils';
import { LocalStorageService } from '../../../../services/commons/local-storage.service';
import { CommonService } from '../../../../services/commons/common.service';
import { UploadService } from '../../../../services/upload/upload.service';
import { MatIcon } from '@angular/material/icon';

export interface Report3Dto {
  id: number;
  username: string;
  salaryPerHour: number;
  totalHours: number;
  totalSalary: number;
}

@Component({
  selector: 'app-time-cost-by-user',
  standalone: true,
  imports: [CommonModule, FormsModule, TemplateComponent, MatIcon],
  templateUrl: './time-cost-by-user.component.html',
  styleUrl: './time-cost-by-user.component.scss'
})
export class TimeCostByUserComponent implements OnInit {
  report: Report3Dto[] = [];
  users: any[] = [];
  userId: number | undefined;

  constructor(private _reportService: ReportService, private _userService: UserService, private _localStorageService:LocalStorageService,
    private _commonService:CommonService,
    private _uploadService:UploadService
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
    this._reportService.getReport3(this.userId).subscribe({
      next: (data) => (this.report = data),
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
  
    const dataForReport = this.report.map(r => ({
      id: r.id,
      username: r.username,
      salaryFormatted: `${currency} ${r.salaryPerHour.toFixed(2)}`,
      totalHours: r.totalHours.toFixed(2),
      totalSalaryFormatted: `${currency} ${r.totalSalary.toFixed(2)}`
    }));
  
    const columns: ColumnDefinition<typeof dataForReport[0]>[] = [
      { header: 'ID', field: 'id' },
      { header: 'Usuario', field: 'username' },
      { header: 'Salario/Hora', field: 'salaryFormatted' },
      { header: 'Total Horas', field: 'totalHours' },
      { header: 'Total Salario', field: 'totalSalaryFormatted' }
    ];
  
    this._commonService.getCompanyInfo().subscribe({
      next: (companyInfo) => {
        this._uploadService.getImageBase64(logoId).subscribe({
          next: async (logoBase64: string) => {
            try {
              const totalHoras = this.report.reduce((sum, r) => sum + r.totalHours, 0);
              const totalSalario = this.report.reduce((sum, r) => sum + r.totalSalary, 0);
  
              await generateReportPDF(
                columns,
                dataForReport,
                companyInfo,
                logoBase64,
                currency,
                fechaFormateada,
                username,
                [
                  { label: 'Total de Horas', value: totalHoras, isCurrency: false },
                  { label: 'Total Invertido', value: totalSalario, isCurrency: true }
                ]
              );
            } catch (err) {
              console.error('Error al generar el PDF:', err);
            }
          },
          error: (err) => console.error('Error al obtener el logo:', err)
        });
      },
      error: (err) => console.error('Error al obtener datos de la empresa:', err)
    });
  }
  
}
