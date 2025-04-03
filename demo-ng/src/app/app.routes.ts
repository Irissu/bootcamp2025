import { Routes, UrlSegment } from '@angular/router';
import { HomeComponent, PageNotFoundComponent } from './main';
import { DemosComponent } from './examples';
import { FormulariosComponent } from './examples/formularios/formularios.component';
import { LoginFormComponent, RegisterUserComponent } from './security';
import { ContactosAddComponent, ContactosEditComponent, ContactosListComponent, ContactosViewComponent } from './contactos';


export function graficoFiles(url: UrlSegment[]) {
  return url.length === 1 && url[0].path.endsWith('.svg') ? ({consumed: url}) : null;
}

export const routes: Routes = [
    /* rutas contactos */
    { path: 'contactos', children: [
      { path: '', component: ContactosListComponent},
      { path: 'add', component: ContactosAddComponent},
      { path: ':id/edit', component: ContactosEditComponent},
      { path: ':id', component: ContactosViewComponent},
      { path: ':id/:kk', component: ContactosViewComponent},
      ]},
  {path: '', component: HomeComponent, pathMatch: 'full' },
  {path: 'inicio', component: HomeComponent, },
  { path: 'login', component: LoginFormComponent },
  { path: 'registro', component: RegisterUserComponent },
  {matcher: graficoFiles, loadComponent: () => import('./examples/grafico-svg/grafico-svg.component') },
  /* {path: 'config', loadChildren: () => import('./config/config.module') }, */
  { path: 'config', loadChildren: () => import('./config/config.module').then(m => m.ConfigModule) },
  {path: '404.html', component: PageNotFoundComponent},
  {path: '**', component: PageNotFoundComponent},

];

