import { Routes } from '@angular/router';
import { HomeComponent } from './components/commons/home/home.component';
import { RegisterComponent } from './components/admin/register/register.component';
import { MyProfileComponent } from './components/commons/my-profile/my-profile.component';

export const routes: Routes = [
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },
    { path: 'reports', component: HomeComponent },
    { path: 'users', component: RegisterComponent},
    { path: 'profile', component: MyProfileComponent}
];
