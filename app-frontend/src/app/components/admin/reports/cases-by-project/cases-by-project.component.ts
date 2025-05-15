import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProjectService } from '../../../../services/project/project.service';
import { TemplateComponent } from '../../../commons/template/template.component';
import { MatIcon } from '@angular/material/icon';
import { UploadService } from '../../../../services/upload/upload.service';
import { LocalStorageService } from '../../../../services/commons/local-storage.service';
import { CommonService } from '../../../../services/commons/common.service';
import { ColumnDefinition, generateReportPDF } from '../../../../utils/pdf-generator.utils';

@Component({
  selector: 'app-cases-by-project',
  standalone: true,
  imports: [CommonModule, FormsModule, TemplateComponent, MatIcon],
  templateUrl: './cases-by-project.component.html',
  styleUrl: './cases-by-project.component.scss'
})
export class CasesByProjectComponent implements OnInit {
  projects: any[] = [];
  cases: any[] = [];
  selectedProjectId: number | null = null;

  constructor(private _projectService: ProjectService,
    private _uploadService: UploadService,
    private _localStorageService: LocalStorageService,
    private _commonService: CommonService
  ) {}

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this._projectService.getAllProjects().subscribe({
      next: (data) => (this.projects = data),
      error: (err) => console.error(err)
    });
  }

  loadCases(): void {
    if (this.selectedProjectId !== null) {
      this._projectService.getCasesByFkProject(this.selectedProjectId).subscribe({
        next: (data) => (this.cases = data),
        error: (err) => console.error(err)
      });
    }
  }

  async exportToPDF(): Promise<void> {
    const logoId = this._localStorageService.getItem(this._localStorageService.COMPANY_LOGO);
    const currency = this._localStorageService.getItem(this._localStorageService.COMPANY_CURRENCY);
    const username = this._localStorageService.getItem(this._localStorageService.USER_NAME);
  
    const fecha = new Date();
    const fechaFormateada = `${fecha.getDate().toString().padStart(2, '0')}/${
      (fecha.getMonth() + 1).toString().padStart(2, '0')
    }/${fecha.getFullYear()}`;
  
    const dataForReport = this.cases.map(c => ({
      id: c.id,
      name: c.name,
      description: c.description,
      caseType: c.fkCaseType,
      limitDate: new Date(c.limitDate).toLocaleDateString(),
      status: c.isCancelled ? 'Cancelado' : c.isEnabled ? 'Activo' : 'Inactivo'
    }));
  
    const columns: ColumnDefinition<typeof dataForReport[0]>[] = [
      { header: 'ID', field: 'id' },
      { header: 'Nombre', field: 'name' },
      { header: 'Descripción', field: 'description' },
      { header: 'Tipo', field: 'caseType' },
      { header: 'Fecha Límite', field: 'limitDate' },
      { header: 'Estado', field: 'status' }
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
                  { label: 'Total Casos', value: this.cases.length, isCurrency: false }
                ]
              );
            } catch (err) {
              console.error('Error al generar PDF:', err);
            }
          },
          error: (err) => {
            console.error('Error al obtener el logo:', err);
          }
        });
      },
      error: (err) => {
        console.error('Error al obtener la información de la empresa:', err);
      }
    });
  }
  
}
