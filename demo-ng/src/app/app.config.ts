import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';

import { routes } from './app.routes';
import { ERROR_LEVEL, LoggerService } from '../lib/my-core';
import { environment } from '../environments/environment.development';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { ajaxWaitInterceptor } from './main';

export const appConfig: ApplicationConfig = {
  providers: [
    LoggerService,
    { provide: ERROR_LEVEL, useValue: environment.ERROR_LEVEL },
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(withInterceptors([ ajaxWaitInterceptor ])),
  ]
};