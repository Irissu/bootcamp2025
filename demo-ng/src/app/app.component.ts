import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoggerService } from '../lib/my-core';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'iris';

  constructor(_out: LoggerService) {
    _out.log('Esto es un log');
    _out.info('Esto es un info');
    _out.warn('Esto es un warn');
    _out.error('Esto es un error');
  }
}
