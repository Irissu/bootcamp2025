
import { CommonModule } from '@angular/common';
import { Component, computed, OnDestroy, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Unsubscribable } from 'rxjs';
import { CapitalizePipe, ElipsisPipe } from '../../../lib/my-core/pipes/cadenas.pipe';
import { NotificationService, NotificationType } from '../../common-services';
import { SizerComponent } from '../../../lib/my-core/components/sizer.component';
import { LoggerService } from '../../../lib/my-core';
import { CardComponent, FormButtonsComponent } from '../../common-components';
import GraficoSvgComponent from '../grafico-svg/grafico-svg.component';

@Component({
  selector: 'app-demos',
  imports: [FormsModule, CommonModule, ElipsisPipe, CapitalizePipe, SizerComponent, FormButtonsComponent, CardComponent, GraficoSvgComponent, ],
  templateUrl: './demos.component.html',
  styleUrl: './demos.component.css'
})
export class DemosComponent implements OnInit, OnDestroy {
  private fecha = new Date('2025-03-31');
  public readonly nombre = signal<string>('mundo')
  public readonly fontSize = signal<number>(24)
  public readonly listado = signal([
    { id: 1, nombre: 'Madrid'},
    { id: 2, nombre: 'OVIEDO'},
    { id: 3, nombre: 'barcelona'},
    { id: 4, nombre: 'ciudad Real'},
  ])
  public readonly idProvincia = signal<number>(2)

  public readonly resultado = signal<string>('')
  public readonly visible = signal<boolean>(true)
  public readonly invisible = computed<boolean>(() => !this.visible())
  public readonly estetica = signal({ importante: true, urgente: true, error: false })

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  constructor(public vm: NotificationService, out: LoggerService) { }

  public get Fecha(): string { return this.fecha.toISOString(); }
  public set Fecha(value: string) {
    this.fecha = new Date(value);
  }

  saluda() {
    this.resultado.set(`Hola ${this.nombre()}`);
  }
  despide() {
    this.resultado.set(`Adios ${this.nombre()}`);
  }
  di(algo: string) {
    this.resultado.set(`Dice ${algo}`);
  }
  cambia() {
    this.visible.update(valor => !valor);
    this.estetica.update(valor => ({ ...valor, importante: !valor.importante }));
    this.estetica.update(valor => ({ ...valor, error: !valor.error }));
  }

  add(provincia: string) {
    const id = this.listado()[this.listado().length - 1].id + 1;
    this.listado.update(valor => [...valor, { id, nombre: provincia }]);
    this.idProvincia.set(id);
  }

  calcula(a: number, b:number) { return a + b; }


  private suscriptor: Unsubscribable | undefined;
  //private suscriptor?: Unsubscribable;

  ngOnInit(): void {
    this.suscriptor = this.vm.Notificacion.subscribe({
      next: n => {
        if (n.Type !== NotificationType.error) { return; }
        window.alert(`Suscripción: ${n.Message}`);
        this.vm.remove(this.vm.Listado.length - 1);
      },
      complete: () => this.suscriptor?.unsubscribe()
    });
  }

  ngOnDestroy(): void {
    if (this.suscriptor) {
      this.suscriptor.unsubscribe();
    }
  }

}