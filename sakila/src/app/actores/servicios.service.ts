/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/no-unused-vars */
import { HttpClient, HttpContext, HttpContextToken, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { LoggerService } from '@my/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthService } from '../security';
import { Router } from '@angular/router';
import { NavigationService, NotificationService } from '../common-services';

export type ModoCRUD = 'list' | 'add' | 'edit' | 'view' | 'delete';
export const AUTH_REQUIRED = new HttpContextToken<boolean>(() => false);

export abstract class RESTDAOService<T, K> {
  protected baseUrl = environment.apiUrl;
  protected http: HttpClient = inject(HttpClient)
  constructor(entidad: string, protected option = {}) {
  this.baseUrl += entidad;
  }
  query(): Observable<T[]> {
  return this.http.get<T[]>(this.baseUrl, this.option);
  }
  get(id: K): Observable<T> {
  return this.http.get<T>(`${this.baseUrl}/${id}`, this.option);
  }
  add(item: T): Observable<T> {
  return this.http.post<T>(this.baseUrl, item, this.option);
  }
  change(id: K, item: T): Observable<T> {
  return this.http.put<T>(`${this.baseUrl}/${id}`, item, this.option);
  }
  remove(id: K): Observable<T> {
  return this.http.delete<T>(`${this.baseUrl}/${id}`, this.option);
  }
  }
  
@Injectable({
  providedIn: 'root'
})
export class ActoresDAOService extends RESTDAOService<any, any> {

  constructor() { 
    super('actores',  { context: new HttpContext().set(AUTH_REQUIRED, true) });
   }

  }

@Injectable({
  providedIn: 'root'
})
export class ActoresViewModelService {
    protected modo: ModoCRUD = 'list';
    protected listado: any[] = [];
    protected elemento: any = {};
    protected idOriginal: any = null;
    protected listURL = '/actores';

    constructor(
      protected notify: NotificationService,
      protected out: LoggerService,
      protected dao: ActoresDAOService, 
      public auth: AuthService, 
      protected router: Router, 
      private navigation: NavigationService
    ) { }

    public get Modo(): ModoCRUD { return this.modo; }
    public get Listado() { return this.listado; }
    public get Elemento() { return this.elemento; }

    // comando para operaciones con la entidad
    public list(): void {
      this.dao.query().subscribe({
      next: data => {
      this.listado = data;
      this.modo = 'list';
      },
      error: err => this.handleError(err)
      });
      }

      public add(): void {
        this.elemento = {};
        this.modo = 'add';
      }
      public edit(key: any): void {
        this.dao.get(key).subscribe({
          next: data => {
            this.elemento = data;
            this.idOriginal = key;
            this.modo = 'edit';
          },
          error: err => this.handleError(err)
        });
      }

      public view(key: any): void {
        this.dao.get(key).subscribe({
        next: data => {
        this.elemento = data;
        this.modo = 'view';
        },
        error: err => this.handleError(err)
        });
        }
        public delete(key: any): void {
        if (!window.confirm('¿Seguro?')) { return; }
        this.dao.remove(key).subscribe({
        next: () => this.list(),
        error: err => this.handleError(err)
        });
        }

        // comandos para cerrar la vista de detalle y/o formulario
        public cancel(): void {
          /*this.elemento = {};
          this.idOriginal = null;
          this.list(); */
          this.clear()
          this.router.navigateByUrl(this.listURL);
          }
          public send(): void {
          switch (this.modo) {
          case 'add':
          this.dao.add(this.elemento).subscribe({
          next: () => this.cancel(),
          error: err => this.handleError(err)
          });
          break;
          case 'edit':
          this.dao.change(this.idOriginal, this.elemento).subscribe({
          next: () => this.cancel(),
          error: err => this.handleError(err)
          });
          break;
          case 'view':
          this.cancel();
          break;
          }
          }
          // comando para liberar memoria cuando no sea necesaria
          clear() {
            this.elemento = {};
            this.idOriginal = undefined;
             this.listado = [];
             }

             // manipulador de errores para su notificacion
             handleError(err: HttpErrorResponse) {
              let msg = ''
              switch (err.status) {
              case 0: msg = err.message; break;
              /*case 404: msg = `ERROR ${err.status}: ${err.statusText}`; break;*/
              case 404: this.router.navigateByUrl('/404.html'); return;
              default:
              msg = `ERROR ${err.status}: ${err.error?.['title'] ??
              err.statusText}.${err.error?.['detail'] ? ` Detalles: ${err.error['detail']}` : ''}`
              break;
              }
              this.notify.add(msg)
              }

}
  

