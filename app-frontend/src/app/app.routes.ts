import { Routes } from '@angular/router';
import { HomeComponent } from './components/commons/home/home.component';
import { RegisterComponent } from './components/admin/register/register.component';
import { ProjectComponent } from './components/commons/project/project.component';
import { DisabledProjectComponent } from './components/commons/disabled-project/disabled-project.component';
import { MyProfileComponent } from './components/commons/my-profile/my-profile.component';
import { CasesComponent } from './components/admin/cases/cases.component';

export const routes: Routes = [
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },
    { path: 'reports', component: HomeComponent },
    { path: 'users', component: RegisterComponent},
    { path: 'projects', component: ProjectComponent},
    { path: 'disabled-projects', component: DisabledProjectComponent},
    { path: 'profile', component: MyProfileComponent},
    { path: 'cases-creation', component: CasesComponent}
];
