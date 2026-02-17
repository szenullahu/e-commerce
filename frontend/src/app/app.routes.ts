import { Routes } from '@angular/router';
import {LoginComponent} from './features/auth/login/login.component';
import {HomeComponent} from './features/home/home.component';
import {RegisterComponent} from './features/auth/register/register.component';
import {UserComponent} from './features/user/user.component';
import {AdminUserComponent} from './features/admin/admin-user/admin-user.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent},
  { path: 'register', component: RegisterComponent},
  { path: 'profile', component: UserComponent},
  { path: 'admin-user', component: AdminUserComponent},
  { path: '**', redirectTo: ''}
];
