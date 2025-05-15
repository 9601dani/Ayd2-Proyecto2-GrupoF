import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TemplateComponent} from '../template/template.component';
import {ModalComponent} from '../modal/modal.component';
import {LoginComponent} from '../login/login.component';
import {UserService} from '../../../services/user/user.service';
import { CompanyService } from '../../../services/company/company.service';
import { error } from 'console';
import e from 'express';
import { Title } from '@angular/platform-browser';
import { LocalStorageService } from '../../../services/commons/local-storage.service';
import { AlertService } from '../../../services/commons/alert.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatIcon } from '@angular/material/icon';
import { ImagePipe } from '../../../pipes/image.pipe';
import { generateReportPDF, CompanyInfo, ColumnDefinition, getBase64ImageFromUrl } from '../../../utils/pdf-generator.utils';
import { environment } from '../../../../environments/environment';
import { UploadService } from '../../../services/upload/upload.service';


type ReporteSimulado = {
  projectId: number;
  projectName: string;
  totalHours: number;
  totalInvested: number;
};

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, TemplateComponent, ModalComponent, LoginComponent, MatIcon, ImagePipe],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  constructor() {}
  private _companyService: CompanyService = inject(CompanyService);
  private _alertService: AlertService = inject(AlertService);
  private _fb: FormBuilder = inject(FormBuilder);
  private _localStorageService = inject(LocalStorageService)
  private _uploadService:UploadService = inject(UploadService)
  settingsForm!: FormGroup;
  settingModules: any[] = [];
  currentModule: string = '';
  currentSettings: any[] = [];
  originalImages: any[] = [];
  hidePassword: boolean[] = [];
  formFields: { key: string, control: FormControl, setting: any }[] = [];

  ngOnInit(){
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

  updateModuleInfo(module: any) {
      this.settingsForm = this._fb.group({});
      this.formFields = [];
      this.currentModule = module.settingName;
      this.currentSettings = module.settings;
  
      this.currentSettings.forEach(s => {
        const control = new FormControl(s.keyValue, Validators.required);
        this.settingsForm.addControl(s.keyName, control);
        this.formFields.push({ key: s.keyName, control, setting: s });
      });
    }
    
}
