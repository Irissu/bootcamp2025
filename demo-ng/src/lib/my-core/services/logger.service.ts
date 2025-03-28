import { Inject, Injectable, InjectionToken, Optional } from '@angular/core';
export const ERROR_LEVEL = new InjectionToken<string>('ERROR_LEVEL');

@Injectable({
  providedIn: 'root'
})
export class LoggerService {
private level: number =   Number.MAX_VALUE;
  constructor(@Inject(ERROR_LEVEL) @Optional() level? : number) {
    if(level || level === 0)
      this.level = level;
   }


  public error(message : string) : void {
    if (this.level > 0)
    console.error(message);
  }

  public warn(message : string) : void {
    if (this.level > 1)
    console.warn(message);
  }

  public info(message : string) : void {
    if (this.level > 2)
      if (console.info) {
        console.info(message);
      } else {
        console.log(message)
      }
  }

  public log(message : string) : void {
    if (this.level > 3)
    console.log(message);
  }







}
