package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.controllers;

import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.DTOs.ProvinceDTO;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.services.ProvinceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private ProvinceService provinceService;

    // GET: Listar con paginación, filtro y orden
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProvinces(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "idAsc") String sort) {

        logger.info("Solicitando provincias. Página: {}, Búsqueda: {}, Orden: {}", page, search, sort);

        Pageable pageable = PageRequest.of(page - 1, size, getSort(sort));
        Page<ProvinceDTO> provincePage = provinceService.findAll(pageable, search);

        // Estructura de respuesta para facilitar el consumo en Angular
        Map<String, Object> response = new HashMap<>();
        response.put("data", provincePage.getContent());
        response.put("currentPage", provincePage.getNumber() + 1);
        response.put("totalItems", provincePage.getTotalElements());
        response.put("totalPages", provincePage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // GET: Obtener una provincia por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProvinceDTO> getProvinceById(@PathVariable Long id) {
        return provinceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Crear nueva provincia
    @PostMapping
    public ResponseEntity<?> createProvince(@Valid @RequestBody ProvinceDTO provinceDTO) {
        logger.info("Creando provincia: {}", provinceDTO.getCode());
        try {
            ProvinceDTO savedProvince = provinceService.save(provinceDTO);
            return new ResponseEntity<>(savedProvince, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Error de validación de negocio (código duplicado)
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al crear la provincia"));
        }
    }

    // PUT: Actualizar provincia
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProvince(@PathVariable Long id, @Valid @RequestBody ProvinceDTO provinceDTO) {
        logger.info("Actualizando provincia ID: {}", id);
        try {
            ProvinceDTO updatedProvince = provinceService.update(id, provinceDTO);
            return ResponseEntity.ok(updatedProvince);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE: Eliminar provincia
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProvince(@PathVariable Long id) {
        logger.info("Eliminando provincia ID: {}", id);
        try {
            provinceService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Helper para mantener tu lógica de ordenación
    private Sort getSort(String sort) {
        return switch (sort) {
            case "nameAsc" -> Sort.by("name").ascending();
            case "nameDesc" -> Sort.by("name").descending();
            case "codeAsc" -> Sort.by("code").ascending();
            case "codeDesc" -> Sort.by("code").descending();
            case "idDesc" -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };
    }
}
