import {Component, inject, OnInit} from '@angular/core';
import {TemplateComponent} from '../../commons/template/template.component';
import { CompanyService } from '../../../services/company/company.service';
import {KeyValuePipe, NgClass} from '@angular/common';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ImagePipe} from '../../../pipes/image.pipe';
import {AlertService} from '../../../services/commons/alert.service';

@Component({
  selector: 'app-company-settings',
  standalone: true,
  imports: [
    TemplateComponent,
    NgClass,
    ReactiveFormsModule,
    KeyValuePipe,
    ImagePipe
  ],
  templateUrl: './company-settings.component.html',
  styleUrl: './company-settings.component.scss'
})
export class CompanySettingsComponent implements OnInit {

  private _companyService: CompanyService = inject(CompanyService);
  private _alertService: AlertService = inject(AlertService);
  private _fb: FormBuilder = inject(FormBuilder);
  settingsForm!: FormGroup;
  settingModules: any[] = [];
  currentModule: string = '';
  currentSettings: any[] = [];
  originalImages: any[] = [];
  hidePassword: boolean[] = [];
  formFields: { key: string, control: FormControl, setting: any }[] = [];


  ngOnInit(): void {
    this.settingsForm = this._fb.group({});
    this.findAllSettings();
  }

  findAllSettings() {
    this._companyService.findAllSettings().subscribe({
      next: (response: any) => {
        console.log(response);
        this.settingModules = response;
        const module = this.settingModules[0];
        this.updateModuleInfo(module);

      },
      error: (error: any) => {
        console.info(error);
      }
    });
  }

  changeModule(settingName: string) {
    const module = this.settingModules.find(s => s.settingName === settingName);
    this.updateModuleInfo(module);
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


  deleteImage(rowIndex: number, key: string) {
    this.currentSettings[rowIndex].keyValue = this.originalImages.filter(m => m.key === key)[0].value;
  }

  onFileSelected(event: Event, rowIndex: number, key: string) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      // this.editProfileForm.get("image")?.setValue(file);
      const value = this.settingsForm.get(key)?.getRawValue();
      this.settingsForm.get(key)?.setValue(file);
      const previewUrl = URL.createObjectURL(file);
      this.currentSettings[rowIndex].keyValue = previewUrl;
      if(this.originalImages.filter(m => m.key === key).length === 0) {
        this.originalImages.push({key, value})
      }
    }
  }

  updateCompanySettings(data: any) {
    this._companyService.updateCompanySettings(data).subscribe({
      next: (response: any) => {
        this._alertService.success('Éxito!', 'Datos actualizados exitosamente!');
        this._companyService.emitSettingBehavior();
      }, error: (error: any) => {
        this._alertService.error('Error!', error.error.message);
      }
    })
  }

  togglePasswordVisibility(index: number) {
    this.hidePassword[index] = !this.hidePassword[index];
  }

  onSubmit() {
    if (this.settingsForm.invalid) {
      this._alertService.error('Error!', "Por favor, complete los campos.");
      return;
    }

    const formValues = this.settingsForm.value;

    const resultArray = Object.entries(formValues).map(([keyName, keyValue]: [string, any]) => {
       return { keyName, keyValue };
    });


    const formData = new FormData();
    resultArray.forEach(r => formData.append(r.keyName, r.keyValue));

    this.updateCompanySettings(formData);
  }

}
