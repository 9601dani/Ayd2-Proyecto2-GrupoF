import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportService } from '../../../../services/report/report.service';
import { TemplateComponent } from '../../../commons/template/template.component';
import { CommonService } from '../../../../services/commons/common.service';
import { UploadService } from '../../../../services/upload/upload.service';
import { ColumnDefinition, generateReportPDF } from '../../../../utils/pdf-generator.utils';
import { LocalStorageService } from '../../../../services/commons/local-storage.service';
import { MatIcon } from '@angular/material/icon';

export interface UserResponseWithName {
  id: number;
  username: string;
  email: string;
  salaryPerHour: number;
  isEnabled: boolean;
  firstName: string;
  lastName: string;
}

interface UserExportRow {
  id: number;
  username: string;
  fullName: string;
  email: string;
  salaryFormatted: string;
  status: string;
}




@Component({
  selector: 'app-users-report',
  standalone: true,
  imports: [CommonModule, TemplateComponent, FormsModule, MatIcon],
  templateUrl: './users-report.component.html',
  styleUrl: './users-report.component.scss'
})
export class UsersReportComponent implements OnInit {
  allUsers: UserResponseWithName[] = [];
  filteredUsers: UserResponseWithName[] = [];

  search: string = '';
  minSalary: number | null = null;
  stateFilter: boolean | null = null;

  isExportDisabled: boolean = true;


  constructor(private _reportService: ReportService,
    private _commonService:CommonService,
    private _uploadService:UploadService,
    private _localStorageService:LocalStorageService
  ) {}

  ngOnInit(): void {
    this._reportService.getReport6().subscribe({
      next: (data) => {
        this.allUsers = data;
        this.applyFilters();
      },
      error: (err) => console.error(err),
    });
  }

  applyFilters(): void {
    this.filteredUsers = this.allUsers.filter((user) => {
      const matchState = this.stateFilter === null || user.isEnabled === this.stateFilter;
      const matchSalary = this.minSalary === null || user.salaryPerHour >= this.minSalary;
      const matchSearch =
        this.search.trim() === '' ||
        user.username.toLowerCase().includes(this.search.toLowerCase()) ||
        `${user.firstName} ${user.lastName}`.toLowerCase().includes(this.search.toLowerCase());
  
      return matchState && matchSalary && matchSearch;
    });
  
    this.isExportDisabled = this.filteredUsers.length === 0;
  }
  

  async generarPDF(): Promise<void> {
    const logoId = this._localStorageService.getItem(this._localStorageService.COMPANY_LOGO);
    const currency = this._localStorageService.getItem(this._localStorageService.COMPANY_CURRENCY);
    const username = this._localStorageService.getItem(this._localStorageService.USER_NAME);
  
    const fechaActual = new Date();
    const fechaFormateada = `${fechaActual.getDate().toString().padStart(2, '0')}/${
      (fechaActual.getMonth() + 1).toString().padStart(2, '0')
    }/${fechaActual.getFullYear()}`;
  
    const dataForReport: UserExportRow[] = this.filteredUsers.map(user => ({
      id: user.id,
      username: user.username,
      fullName: `${user.firstName} ${user.lastName}`,
      email: user.email,
      salaryFormatted: `${currency} ${user.salaryPerHour.toFixed(2)}`,
      status: user.isEnabled ? 'Activo' : 'Inactivo',
    }));
    const totalPerHour = this.filteredUsers.reduce((sum, user) => sum + user.salaryPerHour, 0);

    const columns: ColumnDefinition<UserExportRow>[] = [
      { header: 'ID', field: 'id' },
      { header: 'Salario/Hora', field: 'salaryFormatted' },
      { header: 'Usuario', field: 'username' },
      { header: 'Nombre', field: 'fullName' },
      { header: 'Email', field: 'email' },
      { header: 'Estado', field: 'status' },
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
                  { label: 'Total Usuarios', value: dataForReport.length },
                  { label: 'Total por Hora Pagado', value: totalPerHour, isCurrency: true }
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
