import { Component, inject, OnInit } from '@angular/core';
import { TemplateComponent } from '../../../commons/template/template.component';
import { ReportService } from '../../../../services/report/report.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, FormsModule, Validators } from '@angular/forms';
import { LocalStorageService } from '../../../../services/commons/local-storage.service';
import { UploadService } from '../../../../services/upload/upload.service';
import { generateReportPDF } from '../../../../utils/pdf-generator.utils';
import { CompanyService } from '../../../../services/company/company.service';
import { CommonService } from '../../../../services/commons/common.service';
import { MatIcon } from '@angular/material/icon';

export interface Report1Dto {
  projectId: number;
  projectName: string;
  description: string;
  isEnabled: boolean;
  caseCount: number;
}

export interface CompanyInfo {
  companyName: string;
  companyAddress: string;
  companyPhoneNumber: string;
  emailAddress: string;
}

export interface ColumnDefinition<T> {
  header: string;
  field: keyof T;
  transform?: (val: any) => string;
}


@Component({
  selector: 'app-projects-cases',
  standalone: true,
  imports: [TemplateComponent, CommonModule, FormsModule, MatIcon],
  templateUrl: './projects-cases.component.html',
  styleUrl: './projects-cases.component.scss',
})
export class ProjectsCasesComponent implements OnInit {
  report: Report1Dto[] = [];
  filter: boolean | null = null;

  constructor(private _reportService: ReportService, private _localStorageService:LocalStorageService,
    private _uploadService:UploadService, private _companyService:CompanyService,
    private _companyInfoService:CommonService
  ) {}

  settingsForm!: FormGroup;
  settingModules: any[] = [];
  formFields: { key: string, control: FormControl, setting: any }[] = [];
  private _fb: FormBuilder = inject(FormBuilder);

  ngOnInit(): void {
    this.loadReport();
    this.findAllSettings();
  }

    findAllSettings() {
      this._companyService.findAllSettings().subscribe({
        next: (response: any) => {
          this.settingModules = response;
          this.settingsForm = this._fb.group({});
          this.formFields = [];
          
          this.settingModules.forEach(module => {
            module.settings.forEach((s: any) => {
              const control = new FormControl(s.keyValue, Validators.required);
              this.settingsForm.addControl(s.keyName, control);
              this.formFields.push({ key: s.keyName, control, setting: s });
            });
          });
        },
        error: (error: any) => {
          console.info(error);
        }
      });
    }

  loadReport(): void {
    const params: any = {};
    if (this.filter !== null) {
      params.isEnabled = this.filter;
    }

    this._reportService.getReport1(params).subscribe({
      next: (data: any) => (this.report = data),
      error: (err:any) => console.error(err),
    });
  }

  async generarPDF(): Promise<void> {
    const logoId = this._localStorageService.getItem(this._localStorageService.COMPANY_LOGO);
    const currency = this._localStorageService.getItem(this._localStorageService.COMPANY_CURRENCY);
    const username = this._localStorageService.getItem(this._localStorageService.USER_NAME);
  
    const columns: ColumnDefinition<Report1Dto>[] = [
      { header: 'ID', field: 'projectId' },
      { header: 'Nombre', field: 'projectName' },
      { header: 'Descripción', field: 'description' },
      { header: 'Estado', field: 'isEnabled', transform: (val) => val ? 'Activo' : 'Inactivo' },
      { header: 'Casos', field: 'caseCount' }
    ];
  
    const fechaActual = new Date();
    const fechaFormateada = `${fechaActual.getDate().toString().padStart(2, '0')}/${
      (fechaActual.getMonth() + 1).toString().padStart(2, '0')
    }/${fechaActual.getFullYear()}`;
  
    const dataForReport = this.report.map(item => ({
      ...item,
      isEnabled: item.isEnabled ? 'Activo' : 'Inactivo'
    }));
  
    const totalProjects = this.report.length;
    const totalCases = this.report.reduce((sum, project) => sum + project.caseCount, 0);
  
    const topProject = this.report.reduce((max, project) => 
    project.caseCount > max.caseCount ? project : max, this.report[0]);
    
    const topProjectText = `${topProject.projectName} (${topProject.caseCount} casos)`;
    
    this._companyInfoService.getCompanyInfo().subscribe({
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
                  { label: 'Total de Proyectos', value: totalProjects, isCurrency: false },
                  { label: 'Total de Casos', value: totalCases, isCurrency: false },
                  { label: 'Proyecto con más casos', value: topProjectText }
                ]
              );
            } catch (error) {
              console.error("Error al generar el PDF:", error);
            }
          },
          error: (error) => {
            console.error('Error al obtener la imagen desde el backend:', error);
          }
        });
      },
      error: (err) => {
        console.error('Error obteniendo la info de la empresa:', err);
      }
    });
  }  
  
}
