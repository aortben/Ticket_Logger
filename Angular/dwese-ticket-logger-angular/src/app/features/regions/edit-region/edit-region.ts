import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RegionService } from '../../../core/services/region';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

@Component({
    selector: 'app-edit-region',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatInputModule,
        MatButtonModule,
        MatCardModule,
        MatCardModule,
        MatSnackBarModule,
        RouterModule
    ],
    templateUrl: './edit-region.html',
    styleUrls: ['./edit-region.css']
})
export class EditRegionComponent implements OnInit {

    private fb = inject(FormBuilder);
    private regionService = inject(RegionService);
    private snackBar = inject(MatSnackBar);
    private route = inject(ActivatedRoute);
    private router = inject(Router);

    regionForm: FormGroup;
    isSubmitting = false;
    regionId: number | null = null;
    selectedFile: File | null = null;

    constructor() {
        this.regionForm = this.fb.group({
            code: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(10)]],
            name: ['', [Validators.required, Validators.maxLength(100)]]
        });
    }

    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            const id = params.get('id');
            if (id) {
                this.regionId = +id;
                this.loadRegion(this.regionId);
            }
        });
    }

    loadRegion(id: number): void {
        this.regionService.getRegionById(id).subscribe({
            next: (region) => {
                this.regionForm.patchValue({
                    code: region.code,
                    name: region.name
                });
            },
            error: (err) => {
                console.error('Error al cargar la región:', err);
                this.snackBar.open('Error al cargar la región', 'Cerrar', { duration: 3000 });
                this.router.navigate(['/regions']);
            }
        });
    }

    onFileSelected(event: any): void {
        this.selectedFile = event.target.files[0] ?? null;
    }

    onSubmit(): void {
        if (this.regionForm.invalid) {
            this.regionForm.markAllAsTouched();
            return;
        }

        if (!this.regionId) return;

        this.isSubmitting = true;

        const formData = new FormData();
        formData.append('code', this.regionForm.value.code);
        formData.append('name', this.regionForm.value.name);
        if (this.selectedFile) {
            formData.append('imageFile', this.selectedFile);
        }

        this.regionService.updateRegion(this.regionId, formData).subscribe({
            next: (response) => {

                this.snackBar.open('¡Actualizada con éxito!', 'Cerrar', { duration: 3000 });
                this.isSubmitting = false;
                this.router.navigate(['/regions']);
            },
            error: (error) => {
                console.error('❌ Error al actualizar:', error);
                this.snackBar.open(`Error updating region: ${error.status} - ${error.message}`, 'Close', { duration: 5000 });
                this.isSubmitting = false;
            }
        });
    }
}
