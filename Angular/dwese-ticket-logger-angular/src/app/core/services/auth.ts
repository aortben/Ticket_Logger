import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap, map } from 'rxjs';
import { jwtDecode } from 'jwt-decode'; 
import { environment } from '../environments/environment'; // Ajusta la ruta si es necesario
import { NotificationService } from './notification';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  // BehaviorSubject almacena el token y permite a otros componentes reaccionar cuando cambia.
  private token = new BehaviorSubject<string | null>(null);

  constructor(
    private http: HttpClient,
    private router: Router,
    private notificationService: NotificationService // Servicio de WebSockets para notificaciones
  ) {}

  login(username: string, password: string): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(
      `${environment.apiUrl}/v1/authenticate`,
      { username, password },
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    ).pipe(
      tap(response => {
        this.setToken(response.token); // Guardamos el token
        console.log('✅ Login exitoso, conectando a WebSockets...');

        // Iniciar WebSocket usando el método connect del otro servicio
        this.notificationService.connect();
      })
    );
  }

  /**
   * Almacena el token de autenticación en el BehaviorSubject.
   * @param token - Token recibido tras una autenticación exitosa.
   */
  setToken(token: string): void {
    this.token.next(token); // Actualiza el valor del token.
  }

  /**
   * Obtiene el token actual almacenado en el BehaviorSubject.
   * @returns El token actual o null si no está definido.
   */
  getToken(): string | null {
    return this.token.value;
  }

  /**
   * Devuelve un observable que emite el estado de autenticación basado en la existencia del token.
   * @returns Observable<boolean>
   */
  isLoggedIn(): Observable<boolean> {
    // Verifica si el token existe y emite un valor booleano.
    return this.token.asObservable().pipe(map((token: string | null) => !!token));
  }

  /**
   * Cierra sesión: borra el token, desconecta WebSockets y redirige al usuario.
   */
  logout(): void {
    console.log('🚪 Cerrando sesión y desconectando de WebSockets...');
    this.token.next(null);
    
    // Desconecta WebSockets para que no siga escuchando sin usuario
    this.notificationService.disconnect(); 
    
    this.router.navigate(['/']);
  }

  /**
   * Extrae el nombre de usuario desde el token JWT.
   * @returns Nombre de usuario o `null` si el token es inválido.
   */
  getUsername(): string | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const decodedToken: any = jwtDecode(token);
      return decodedToken.sub || null; // `sub` es el campo estándar en JWT para el username.
    } catch (error) {
      console.error('❌ Error al decodificar el token:', error);
      return null;
    }
  }
}