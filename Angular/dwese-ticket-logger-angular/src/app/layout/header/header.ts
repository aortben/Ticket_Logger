import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthService } from '../../core/services/auth';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { NotificationsComponent } from '../../shared/notifications/notifications';

@Component({
  selector: 'app-header',
  imports: [RouterLink, CommonModule, NotificationsComponent],
  templateUrl: './header.html',
  styleUrls: ['./header.css'],
})
export class HeaderComponent implements OnInit, OnDestroy {
  isLoggedIn = false; // Estado local de autenticación
  private subscription: Subscription | null = null;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    // Suscriptor al estado de autenticación
    this.subscription = this.authService.isLoggedIn().subscribe((loggedIn: boolean) => {
      this.isLoggedIn = loggedIn;
    });
  }

  logout() {
    this.authService.logout(); // Llamar al método de logout del servicio
  }

  ngOnDestroy() {
    // Cancelar la suscripción al destruir el componente
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
