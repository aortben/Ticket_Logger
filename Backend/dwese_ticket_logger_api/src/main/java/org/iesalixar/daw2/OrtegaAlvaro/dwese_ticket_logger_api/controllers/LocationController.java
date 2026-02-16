package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.controllers;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.DTOs.LocationDTO;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.services.LocationService;
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
@RequestMapping("/api/locations")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllLocations(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "idAsc") String sort) {

        Pageable pageable = PageRequest.of(page - 1, size, getSort(sort));
        Page<LocationDTO> pageResult = locationService.findAll(pageable, search);

        Map<String, Object> response = new HashMap<>();
        response.put("data", pageResult.getContent());
        response.put("currentPage", pageResult.getNumber() + 1);
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        return locationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createLocation(@Valid @RequestBody LocationDTO dto) {
        logger.info("Creando ubicación en: {}", dto.getCity());
        try {
            LocationDTO saved = locationService.save(dto);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            // Si el front envía un ID de provincia/supermercado que no existe
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLocation(@PathVariable Long id, @Valid @RequestBody LocationDTO dto) {
        logger.info("Actualizando ubicación ID: {}", id);
        try {
            LocationDTO updated = locationService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
        try {
            locationService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private Sort getSort(String sort) {
        return switch (sort) {
            case "cityAsc" -> Sort.by("city").ascending();
            case "cityDesc" -> Sort.by("city").descending();
            case "addressAsc" -> Sort.by("address").ascending();
            case "addressDesc" -> Sort.by("address").descending();
            case "idDesc" -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };
    }
}
