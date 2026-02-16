import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthService } from './auth';
import { environment } from '../environments/environment';
import { RegionDTO } from '../../models/types';

/**
 * Servicio para gestionar las regiones, incluyendo la obtención de datos paginados,
 * creación, actualización y eliminación, con autenticación mediante token.
 */
@Injectable({
  providedIn: 'root',
})
export class RegionService {
  constructor(private http: HttpClient, private authService: AuthService) { }

  /**
   * Obtiene la lista de regiones desde la API con paginación y ordenación.
   */
  fetchRegions(page: number, size: number, sortColumn: string, sortDirection: string): Observable<any> {
    const token = this.authService.getToken();
    if (!token) return throwError(() => new Error('Unauthorized'));

    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', `${sortColumn},${sortDirection}`);

    return this.http.get(`${environment.apiUrl}/regions`, {
      headers: new HttpHeaders({ Authorization: `Bearer ${token}` }),
      params: params
    });
  }

  /**
   * Obtiene una región por su ID.
   */
  getRegionById(id: number): Observable<any> {
    const token = this.authService.getToken();
    if (!token) return throwError(() => new Error('Unauthorized'));

    return this.http.get(`${environment.apiUrl}/regions/${id}`, {
      headers: new HttpHeaders({ Authorization: `Bearer ${token}` })
    });
  }

  /**
   * Crea una nueva región en la API.
   * Se envía como FormData para soportar subida de imágenes.
   */
  createRegion(regionData: FormData): Observable<any> {
    const token = this.authService.getToken();
    if (!token) return throwError(() => new Error('Unauthorized'));

    // No establecemos Content-Type explícitamente, Angular lo hará automáticamente con el boundary correcto para FormData
    return this.http.post(`${environment.apiUrl}/regions`, regionData, {
      headers: new HttpHeaders({ Authorization: `Bearer ${token}` })
    });
  }

  /**
   * Actualiza una región existente.
   * Se envía como FormData para soportar subida de imágenes.
   */
  updateRegion(id: number, regionData: FormData): Observable<any> {
    const token = this.authService.getToken();
    if (!token) return throwError(() => new Error('Unauthorized'));

    return this.http.put(`${environment.apiUrl}/regions/${id}`, regionData, {
      headers: new HttpHeaders({ Authorization: `Bearer ${token}` })
    });
  }

  /**
   * Elimina una región.
   */
  deleteRegion(id: number): Observable<any> {
    const token = this.authService.getToken(); // Obtén el token del AuthService
    if (!token) {
      // Si no hay token, lanza un error indicando que el usuario no está autorizado.
      return throwError(() => new Error('Unauthorized'));
    }

    // Realiza la request DELETE al endpoint de regiones con el token en el encabezado.
    return this.http.delete(`${environment.apiUrl}/regions/` + id, {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      }),
      responseType: 'text' // Mantengo esto para evitar errores de parsing si el backend devuelve texto
    }).pipe(
      tap(response => {
        console.log(response)
      })
    );
  }
}