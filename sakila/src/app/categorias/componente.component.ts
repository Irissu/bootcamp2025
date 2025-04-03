/* eslint-disable @typescript-eslint/no-unused-vars */
import { DatePipe } from '@angular/common';
import { Component, forwardRef, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { ActivatedRoute, ParamMap, Router, RouterLink } from '@angular/router';
import { ActoresViewModelService } from './servicios.service';
import { Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { ErrorMessagePipe, TypeValidator } from '@my/core';

@Component({
  selector: 'app-actores',
  templateUrl: './tmpl-anfitrion.component.html',
  styleUrl: './componente.component.css',
  imports: [
    forwardRef(() => ActoresAddComponent),
    forwardRef(() => ActoresEditComponent),
    forwardRef(() => ActoresViewComponent),
    forwardRef(() => ActoresListComponent),
    ],
})
export class ActoresComponent implements  OnInit, OnDestroy {
  constructor(protected vm: ActoresViewModelService) { }
  public get VM(): ActoresViewModelService { return this.vm; }
  ngOnInit(): void { this.vm.list(); }
  ngOnDestroy(): void { this.vm.clear(); }
}

@Component({
  selector: 'app-actores-list',
  templateUrl: './tmpl-list.component.html',
  styleUrls: ['./componente.component.css'],
  imports: [RouterLink]
})
export class ActoresListComponent implements OnInit, OnDestroy {
  constructor(protected vm: ActoresViewModelService) { }
  public get VM():ActoresViewModelService { return this.vm; }
  ngOnInit(): void { this.vm.list(); }
  ngOnDestroy(): void { this.vm.clear(); }

}
@Component({
  selector: 'app-actores-add',
  templateUrl: './tmpl-form.component.html',
  styleUrls: ['./componente.component.css'],
  imports: [FormsModule, TypeValidator, ErrorMessagePipe]
})
export class ActoresAddComponent implements OnInit {
  constructor(protected vm: ActoresViewModelService) { }
  public get VM(): ActoresViewModelService { return this.vm; }
  ngOnInit(): void { this.vm.add(); }
}
@Component({
  selector: 'app-actores-edit',
  templateUrl: './tmpl-form.component.html',
  styleUrls: ['./componente.component.css'],
  imports: [FormsModule, TypeValidator, ErrorMessagePipe]
})
export class ActoresEditComponent implements OnInit, OnDestroy {
  private obs$?: Subscription;
  constructor(protected vm: ActoresViewModelService,
    protected route: ActivatedRoute, protected router: Router) { }
  public get VM(): ActoresViewModelService { return this.vm; }
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
  selector: 'app-actores-view',
  templateUrl: './tmpl-view.component.html',
  styleUrls: ['./componente.component.css'],
  imports: [DatePipe]
})
export class ActoresViewComponent implements OnChanges {
  @Input() id?: string;
  constructor(protected vm: ActoresViewModelService, protected router: Router) { }
  public get VM(): ActoresViewModelService { return this.vm; }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.id) {
      this.vm.view(+this.id);
    } else {
      this.router.navigate(['/404.html']);
    }
  }
}

export const ACTORES_COMPONENTES = [
  ActoresComponent, ActoresListComponent, ActoresAddComponent,
  ActoresEditComponent, ActoresViewComponent,
];
