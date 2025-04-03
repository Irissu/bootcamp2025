import { Routes, UrlSegment } from '@angular/router';
import { HomeComponent, PageNotFoundComponent } from './main';
import { LoginFormComponent, RegisterUserComponent } from './security';
import { ActoresAddComponent, ActoresEditComponent, ActoresListComponent, ActoresViewComponent } from './actores';
import { IdiomasAddComponent, IdiomasEditComponent, IdiomasListComponent, IdiomasViewComponent } from './idiomas';

export function graficoFiles(url: UrlSegment[]) {
  return url.length === 1 && url[0].path.endsWith('.svg') ? ({consumed: url}) : null;
}

export const routes: Routes = [
   /* rutas actores */
   { path: 'actores', children: [
    { path: '', component: ActoresListComponent},
    { path: 'add', component: ActoresAddComponent},
    { path: ':id/edit', component: ActoresEditComponent},
    { path: ':id', component: ActoresViewComponent},
    { path: ':id/:kk', component: ActoresViewComponent},
    ]},
     /* rutas idiomas */
     { path: 'idiomas', children: [
      { path: '', component: IdiomasListComponent},
      { path: 'add', component: IdiomasAddComponent},
      { path: ':id/edit', component: IdiomasEditComponent},
      { path: ':id', component: IdiomasViewComponent},
      { path: ':id/:kk', component: IdiomasViewComponent},
      ]},
  {path: '', component: HomeComponent, pathMatch: 'full' },
  {path: 'inicio', component: HomeComponent, },
  { path: 'login', component: LoginFormComponent },
  { path: 'registro', component: RegisterUserComponent },
  { path: '404.html', component: PageNotFoundComponent },
  { path: '**', component: PageNotFoundComponent }
];
