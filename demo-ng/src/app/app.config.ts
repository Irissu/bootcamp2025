import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { ERROR_LEVEL, LoggerService } from '../lib/my-core';
import { environment } from '../environments/environment.development';

export const appConfig: ApplicationConfig = {
  providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes), LoggerService, {provide: ERROR_LEVEL, useValue: environment.ERROR_LEVEL}]
};
