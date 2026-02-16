package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.services;


import jakarta.persistence.EntityNotFoundException;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.DTOs.LocationDTO;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities.Location;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities.Supermarket;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.repositories.LocationRepository;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.repositories.ProvinceRepository;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.repositories.SupermarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private SupermarketRepository supermarketRepository;

    public Page<LocationDTO> findAll(Pageable pageable, String search) {
        Page<Location> page;
        if (search != null && !search.isBlank()) {
            page = locationRepository.findByAddressOrCityContaining(search, pageable);
        } else {
            page = locationRepository.findAll(pageable);
        }
        return page.map(this::convertToDTO);
    }

    public Optional<LocationDTO> findById(Long id) {
        return locationRepository.findById(id).map(this::convertToDTO);
    }

    public LocationDTO save(LocationDTO dto) {
        Location location = convertToEntity(dto);
        Location saved = locationRepository.save(location);
        return convertToDTO(saved);
    }

    public LocationDTO update(Long id, LocationDTO dto) {
        Location existing = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ubicación no encontrada"));

        // Actualizamos campos simples
        existing.setAddress(dto.getAddress());
        existing.setCity(dto.getCity());

        // Actualizamos relaciones si cambiaron
        if (!existing.getProvince().getId().equals(dto.getProvinceId())) {
            Province p = provinceRepository.findById(dto.getProvinceId())
                    .orElseThrow(() -> new EntityNotFoundException("Provincia no encontrada"));
            existing.setProvince(p);
        }

        if (!existing.getSupermarket().getId().equals(dto.getSupermarketId())) {
            Supermarket s = supermarketRepository.findById(dto.getSupermarketId())
                    .orElseThrow(() -> new EntityNotFoundException("Supermercado no encontrado"));
            existing.setSupermarket(s);
        }

        Location saved = locationRepository.save(existing);
        return convertToDTO(saved);
    }

    public void deleteById(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar. Ubicación no encontrada.");
        }
        locationRepository.deleteById(id);
    }

    // --- MAPPERS ---

    private LocationDTO convertToDTO(Location entity) {
        return new LocationDTO(
                entity.getId(),
                entity.getAddress(),
                entity.getCity(),
                entity.getSupermarket().getId(),
                entity.getProvince().getId(),
                entity.getSupermarket().getName(),
                entity.getProvince().getName()
        );
    }

    private Location convertToEntity(LocationDTO dto) {
        Province province = provinceRepository.findById(dto.getProvinceId())
                .orElseThrow(() -> new EntityNotFoundException("Provincia no encontrada con ID: " + dto.getProvinceId()));

        Supermarket supermarket = supermarketRepository.findById(dto.getSupermarketId())
                .orElseThrow(() -> new EntityNotFoundException("Supermercado no encontrado con ID: " + dto.getSupermarketId()));

        Location location = new Location();
        if (dto.getId() != null) location.setId(dto.getId());

        location.setAddress(dto.getAddress());
        location.setCity(dto.getCity());
        location.setProvince(province);
        location.setSupermarket(supermarket);

        return location;
    }
}
