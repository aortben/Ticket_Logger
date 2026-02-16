import { Component, OnInit, ViewChild, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegionService } from '../../core/services/region';
import { Router, RouterLink } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

/**
 * Componente que muestra una tabla con la lista de regiones.
 * La tabla permite paginación y ordenación de datos.
 */
@Component({
  selector: 'app-regions',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatPaginatorModule, MatSortModule, MatButtonModule, MatSnackBarModule, RouterLink],
  templateUrl: './regions.html',
  styleUrls: ['./regions.css']
})
export class Regions implements OnInit {
  /**
   * Columnas que se mostrarán en la tabla.
   * Se incluyen las columnas 'id' y 'name' con capacidad de ordenación, y 'actions'.
   */
  displayedColumns: string[] = ['id', 'code', 'name', 'actions'];

  /**
   * Fuente de datos para la tabla, que se llenará con datos de la API.
   */
  dataSource = new MatTableDataSource<any>([]);

  /**
   * Número total de elementos en la base de datos.
   */
  totalElements: number = 0;

  /**
   * Número total de páginas disponibles en la paginación.
   */
  totalPages: number = 0;

  /**
   * Página actual en la que se encuentra el usuario.
   */
  currentPage: number = 0;

  /**
   * Cantidad de elementos por página.
   */
  pageSize: number = 10;

  /**
   * Columna por la que se ordenarán los datos de forma predeterminada.
   */
  sortColumn: string = 'name';

  /**
   * Dirección de la ordenación predeterminada.
   * Puede ser 'asc' (ascendente) o 'desc' (descendente).
   */
  sortDirection: string = 'asc';

  /**
   * Variable que almacena mensajes de error en caso de fallos en la carga de datos.
   */
  error: string | null = null;

  /**
   * Referencia al paginador de Angular Material.
   * Se inicializa automáticamente con '@ViewChild'.
   */
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  /**
   * Referencia al componente de ordenación de Angular Material.
   * Permite la ordenación de la tabla.
   */
  @ViewChild(MatSort) sort!: MatSort;

  private snackBar = inject(MatSnackBar);

  /**
   * Constructor del componente.
   * @param regionService Servicio para obtener datos de las regiones.
   * @param router Servicio de enrutamiento para la navegación en caso de error de autenticación.
   */
  constructor(
    private regionService: RegionService,
    private router: Router,
  ) { }

  /**
   * Método de inicialización del componente.
   * Se ejecuta una vez que el componente ha sido creado y se utiliza para cargar los datos iniciales.
   */
  ngOnInit() {
    this.fetchRegions(this.currentPage, this.pageSize, this.sortColumn, this.sortDirection);
  }

  /**
   * Método que se ejecuta después de que la vista ha sido inicializada.
   * Se usa para asociar el paginador y la ordenación a la tabla.
   */
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    // Suscripción al evento de cambio de ordenación
    this.sort.sortChange.subscribe((sort: Sort) => this.handleSortEvent(sort));
  }

  /**
   * Obtiene la lista de regiones paginadas y ordenadas desde la API.
   * @param page Número de la página a solicitar.
   * @param size Número de elementos por página.
   * @param sortColumn Nombre de la columna por la que se ordenarán los datos.
   * @param sortDirection Dirección de orden ('asc' o 'desc').
   */
  fetchRegions(page: number, size: number, sortColumn: string, sortDirection: string) {


    this.regionService.fetchRegions(page, size, sortColumn, sortDirection).subscribe({
      next: (res: any) => {


        // Se actualiza la fuente de datos con los nuevos registros
        this.dataSource.data = res.content;
        this.totalElements = res.totalElements;
        this.totalPages = res.totalPages;
        this.currentPage = res.number;
        this.pageSize = res.size;

        // Se actualiza el paginador y la vista
        setTimeout(() => {
          this.paginator.length = this.totalElements;
          this.paginator.pageIndex = this.currentPage;
          this.paginator.pageSize = this.pageSize;
        });
      },
      error: (err) => {
        console.error('Error al obtener datos:', err);

        // Si el error es de autenticación, se redirige al usuario a la página de acceso denegado
        if (err.status === 403) {
          this.router.navigate(['/forbidden']);
        } else {
          this.error = 'Error al cargar las regiones';
        }
      }
    });
  }

  /**
   * Maneja el evento de cambio de página.
   * Llama nuevamente al servicio con la nueva página seleccionada.
   * @param event Evento de paginación que contiene la nueva página y el tamaño seleccionado.
   */
  handlePageEvent(event: PageEvent) {

    this.fetchRegions(event.pageIndex, event.pageSize, this.sortColumn, this.sortDirection);
  }

  /**
   * Maneja el evento de cambio de ordenación en la tabla.
   * Llama nuevamente al servicio con la nueva columna y dirección de ordenación.
   * @param sort Evento de ordenación que contiene la columna y la dirección ('asc' o 'desc').
   */
  handleSortEvent(sort: Sort) {


    this.sortColumn = sort.active;
    this.sortDirection = sort.direction || 'asc'; // Si no hay dirección, por defecto 'asc'

    this.fetchRegions(this.currentPage, this.pageSize, this.sortColumn, this.sortDirection);
  }

  editRegion(id: number) {
    this.router.navigate(['/regions/edit', id]);
  }

  deleteRegion(id: number) {
    if (confirm('¿Estás seguro de que deseas eliminar esta región?')) {
      this.regionService.deleteRegion(id).subscribe({
        next: () => {
          this.snackBar.open('Región eliminada con éxito', 'Cerrar', { duration: 3000 });
          this.fetchRegions(this.currentPage, this.pageSize, this.sortColumn, this.sortDirection);
        },
        error: (err) => {
          console.error('Error al eliminar:', err);
          this.snackBar.open(`Error deleting region: ${err.status} - ${err.message}`, 'Close', { duration: 5000 });
        }
      });
    }
  }
}