/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/no-unused-vars */
import { HttpClient, HttpContext, HttpContextToken, HttpErrorResponse } from '@angular/common/http';
import { Injectable, Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges, forwardRef } from '@angular/core';
import { inject } from '@angular/core';
import { LoggerService } from '@my/core';
import { Observable, Subscription } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthService } from '../security';
import { Router, ActivatedRoute, ParamMap, RouterLink } from '@angular/router';
import { NavigationService, NotificationService } from '../common-services';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ErrorMessagePipe, TypeValidator } from '@my/core';

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
export class IdiomasDAOService extends RESTDAOService<any, any> {
  constructor() {
    super('idiomas/v1');
  }
}

@Injectable({
  providedIn: 'root'
})
export class IdiomasViewModelService {
  protected modo: ModoCRUD = 'list';
  protected listado: any[] = [];
  protected elemento: any = {};
  protected idOriginal: any = null;
  protected listURL = '/idiomas';

  constructor(
    protected notify: NotificationService,
    protected out: LoggerService,
    protected dao: IdiomasDAOService,
    public auth: AuthService,
    protected router: Router,
    private navigation: NavigationService
  ) {}

  public get Modo(): ModoCRUD { return this.modo; }
  public get Listado() { return this.listado; }
  public get Elemento() { return this.elemento; }

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

  public cancel(): void {
    this.clear();
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

  clear() {
    this.elemento = {};
    this.idOriginal = undefined;
    this.listado = [];
  }

  handleError(err: HttpErrorResponse) {
    let msg = '';
    switch (err.status) {
      case 0: msg = err.message; break;
      case 404: this.router.navigateByUrl('/404.html'); return;
      default:
        msg = `ERROR ${err.status}: ${err.error?.['title'] ?? err.statusText}.${err.error?.['detail'] ? ` Detalles: ${err.error['detail']}` : ''}`;
        break;
    }
    this.notify.add(msg);
  }
}

@Component({
  selector: 'app-idiomas',
  templateUrl: './tmpl-anfitrion.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [
    forwardRef(() => IdiomasAddComponent),
    forwardRef(() => IdiomasEditComponent),
    forwardRef(() => IdiomasViewComponent),
    forwardRef(() => IdiomasListComponent),
  ],
})
export class IdiomasComponent implements OnInit, OnDestroy {
  constructor(protected vm: IdiomasViewModelService) { }
  public get VM(): IdiomasViewModelService { return this.vm; }
  ngOnInit(): void { this.vm.list(); }
  ngOnDestroy(): void { this.vm.clear(); }
}

@Component({
  selector: 'app-idiomas-list',
  templateUrl: './tmpl-list.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [RouterLink],
})
export class IdiomasListComponent implements OnInit, OnDestroy {
  constructor(protected vm: IdiomasViewModelService) { }
  public get VM(): IdiomasViewModelService { return this.vm; }
  ngOnInit(): void { this.vm.list(); }
  ngOnDestroy(): void { this.vm.clear(); }
}

@Component({
  selector: 'app-idiomas-add',
  templateUrl: './tmpl-form.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [FormsModule, TypeValidator, ErrorMessagePipe],
})
export class IdiomasAddComponent implements OnInit {
  constructor(protected vm: IdiomasViewModelService) { }
  public get VM(): IdiomasViewModelService { return this.vm; }
  ngOnInit(): void { this.vm.add(); }
}

@Component({
  selector: 'app-idiomas-edit',
  templateUrl: './tmpl-form.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [FormsModule, TypeValidator, ErrorMessagePipe],
})
export class IdiomasEditComponent implements OnInit, OnDestroy {
  private obs$?: Subscription;
  constructor(protected vm: IdiomasViewModelService,
    protected route: ActivatedRoute, protected router: Router) { }
  public get VM(): IdiomasViewModelService { return this.vm; }
  ngOnInit(): void {
    this.obs$ = this.route.paramMap.subscribe(
      (params: ParamMap) => {
        const id = parseInt(params?.get('id') ?? '');
        if (id) {
          this.vm.edit(id);
        } else {
          this.router.navigate(['/404.html']);
        }
      });
  }
  ngOnDestroy(): void { this.obs$!.unsubscribe(); }
}

@Component({
  selector: 'app-idiomas-view',
  templateUrl: './tmpl-view.component.html',
  styleUrls: ['./componente.component.css'],
  standalone: true,
  imports: [DatePipe],
})
export class IdiomasViewComponent implements OnChanges {
  @Input() id?: string;
  constructor(protected vm: IdiomasViewModelService, protected router: Router) { }
  public get VM(): IdiomasViewModelService { return this.vm; }
  ngOnChanges(changes: SimpleChanges): void {
    if (this.id) {
      this.vm.view(+this.id);
    } else {
      this.router.navigate(['/404.html']);
    }
  }
}

export const IDIOMAS_COMPONENTES = [
  IdiomasComponent,
  IdiomasListComponent,
  IdiomasAddComponent,
  IdiomasEditComponent,
  IdiomasViewComponent,
];