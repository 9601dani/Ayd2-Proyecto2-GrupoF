import { Routes } from '@angular/router';
import { HomeComponent } from './components/commons/home/home.component';
import { RegisterComponent } from './components/admin/register/register.component';
import { ProjectComponent } from './components/commons/project/project.component';
import { DisabledProjectComponent } from './components/commons/disabled-project/disabled-project.component';
import { MyProfileComponent } from './components/commons/my-profile/my-profile.component';
import { CaseTypeComponent } from './components/admin/case-type/case-type.component';
import { CompanySettingsComponent } from './components/company/company-settings/company-settings.component';
import { AdminProjectComponent } from './components/admin/admin-project/admin-project.component';
import { CaseDetailComponent } from './components/admin/case-detail/case-detail.component';
import { ReportComponent } from './components/admin/reports/report/report.component';
import { ProjectsCasesComponent } from './components/admin/reports/projects-cases/projects-cases.component';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'users', component: RegisterComponent },
  { path: 'projects', component: ProjectComponent },
  { path: 'disabled-projects', component: DisabledProjectComponent },
  { path: 'profile', component: MyProfileComponent },
  { path: 'cases-creation', component: CaseTypeComponent },
  { path: 'project/:id', component: AdminProjectComponent },
  { path: 'profile', component: MyProfileComponent },
  { path: 'company-settings', component: CompanySettingsComponent },
  { path: 'case/:id', component: CaseDetailComponent },
  {
    path: 'reports',
    component: ReportComponent,
  },
  {
    path: 'projects-cases',
    component: ProjectsCasesComponent,
  },
];
