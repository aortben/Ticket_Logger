import { Routes } from '@angular/router';
import { Regions } from '../features/regions/regions';
import { CreateRegionComponent } from '../features/regions/create-region/create-region';
import { EditRegionComponent } from '../features/regions/edit-region/edit-region';
import { LoginComponent } from '../features/login/login';
import { authGuard } from '../core/guards/auth-guard';
import { HomeComponent } from '../features/home/home';
import { ForbiddenComponent } from '../features/forbidden/forbidden';
import { Error404Component } from '../features/error404/error404';

export const routes: Routes = [
  {
    path: '', // Ruta inicial
    component: HomeComponent,
  },
  {
    path: 'login', // Página de inicio de sesión
    component: LoginComponent,
  },
  {
    path: 'regions/create',
    component: CreateRegionComponent,
    canActivate: [authGuard],
  },
  {
    path: 'regions/edit/:id',
    component: EditRegionComponent,
    canActivate: [authGuard],
  },
  {
    path: 'regions', // Página protegida
    component: Regions,
    canActivate: [authGuard], // Protegida por el guard
  },
  {
    path: 'forbidden',
    component: ForbiddenComponent
  }, // Página 403
  {
    path: '**', // Ruta comodín para 404
    component: Error404Component,
  },
];
