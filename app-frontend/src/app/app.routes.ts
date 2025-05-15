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
import { TimeCostByProjectComponent } from './components/admin/reports/time-cost-by-project/time-cost-by-project.component';
import { TimeCostByUserComponent } from './components/admin/reports/time-cost-by-user/time-cost-by-user.component';
import { TimeCostCaseTypeComponent } from './components/admin/reports/time-cost-case-type/time-cost-case-type.component';
import { UsersReportComponent } from './components/admin/reports/users-report/users-report.component';
import { ProjectsReportComponent } from './components/admin/reports/projects-report/projects-report.component';
import { TopUserByCasesComponent } from './components/admin/reports/top-user-by-cases/top-user-by-cases.component';
import { TopUserByPayComponent } from './components/admin/reports/top-user-by-pay/top-user-by-pay.component';
import { TopProjectCompletedComponent } from './components/admin/reports/top-project-completed/top-project-completed.component';
import { TopProjectCancelledComponent } from './components/admin/reports/top-project-cancelled/top-project-cancelled.component';
import { CasesByProjectComponent } from './components/admin/reports/cases-by-project/cases-by-project.component';

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
  {
    path: 'time-cost-by-project',
    component: TimeCostByProjectComponent,
  },
  {
    path: 'time-cost-by-user',
    component: TimeCostByUserComponent,
  },
  {
    path: 'time-cost-by-case-type',
    component: TimeCostCaseTypeComponent,
  },
  {
    path: 'users-report',
    component: UsersReportComponent,
  },
  {
    path: 'projects-report',
    component: ProjectsReportComponent,
  },
  {
    path: 'top-user-by-cases',
    component: TopUserByCasesComponent,
  },
  {
    path: 'top-user-by-pay',
    component: TopUserByPayComponent,
  },
  {
    path: 'top-project-completed',
    component: TopProjectCompletedComponent,
  },
  {
    path: 'top-project-cancelled',
    component: TopProjectCancelledComponent,
  },
  {
    path: 'cases-by-project',
    component: CasesByProjectComponent,
  },
];
