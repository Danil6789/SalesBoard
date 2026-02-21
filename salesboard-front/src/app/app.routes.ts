// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { AdvertsComponent } from './components/adverts/adverts.component';
import { ProfileComponent } from './components/profile/profile.component';
import { CreateAdvertComponent } from './components/create-advert/create-advert.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'adverts', component: AdvertsComponent },
  { 
    path: 'profile', 
    component: ProfileComponent,
    canActivate: [authGuard] 
  },
  { 
    path: 'create-advert', 
    component: CreateAdvertComponent,
    canActivate: [authGuard] 
  },
  { path: '**', redirectTo: '' }
];