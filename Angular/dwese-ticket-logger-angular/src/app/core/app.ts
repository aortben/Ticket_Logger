import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Footer } from '../layout/footer/footer';
import { HeaderComponent } from '../layout/header/header';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Footer, HeaderComponent],
  template: `<app-header></app-header>
  <main>
    <router-outlet></router-outlet>
  </main>
  <app-footer></app-footer>`
})
export class App {
  protected readonly title = signal('dwese-ticket-logger-angular');
}
