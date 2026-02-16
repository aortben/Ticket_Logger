import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RegionService } from '../../../core/services/region';
import { RegionCreateDTO } from '../../../models/types';
import { Router, RouterModule } from '@angular/router';


@Component({
    selector: 'app-create-entity',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule, // Indispensable para formGroup
        MatInputModule,
        MatButtonModule,
        MatCardModule,
        MatCardModule,
        MatSnackBarModule,
        RouterModule
    ],
    templateUrl: './create-region.html',
    styleUrls: ['./create-region.css']
})
export class CreateRegionComponent {


    private fb = inject(FormBuilder);
    private regionService = inject(RegionService);
    private snackBar = inject(MatSnackBar);


    regionForm: FormGroup;
    isSubmitting = false;


    selectedFile: File | null = null;

    constructor(private router: Router) {
        // Inicializar el formulario con validaciones
        this.regionForm = this.fb.group({
            code: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(10)]],
            name: ['', [Validators.required, Validators.maxLength(100)]]
        });
    }

    onFileSelected(event: any): void {
        this.selectedFile = event.target.files[0] ?? null;
    }

    onSubmit(): void {
        if (this.regionForm.invalid) {
            this.regionForm.markAllAsTouched(); // Muestra los errores si el usuario intenta enviar
            return;
        }


        this.isSubmitting = true;

        const formData = new FormData();
        formData.append('code', this.regionForm.value.code);
        formData.append('name', this.regionForm.value.name);
        if (this.selectedFile) {
            formData.append('imageFile', this.selectedFile);
        }

        this.regionService.createRegion(formData).subscribe({
            next: (response) => {

                this.snackBar.open('¡Creada con éxito!', 'Cerrar', { duration: 3000 });
                this.regionForm.reset(); // Limpiar formulario
                this.selectedFile = null; // Limpiar archivo seleccionado
                this.isSubmitting = false;
                this.router.navigate(['/regions']);
            },
            error: (err) => {
                console.error('Error al crear la región:', err);
                this.snackBar.open(`Error creating region: ${err.status} - ${err.message}`, 'Close', { duration: 5000 });
                this.isSubmitting = false;
            }
        });
    }
}
