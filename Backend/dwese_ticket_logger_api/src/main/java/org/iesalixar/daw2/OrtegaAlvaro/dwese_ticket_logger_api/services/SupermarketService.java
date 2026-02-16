package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.DTOs.SupermarketDTO;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities.Supermarket;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.repositories.SupermarketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SupermarketService {

    @Autowired
    private SupermarketRepository supermarketRepository;

    public Page<SupermarketDTO> findAll(Pageable pageable, String search) {
        Page<Supermarket> page;
        if (search != null && !search.isBlank()) {
            page = supermarketRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            page = supermarketRepository.findAll(pageable);
        }
        return page.map(this::convertToDTO);
    }

    public Optional<SupermarketDTO> findById(Long id) {
        return supermarketRepository.findById(id).map(this::convertToDTO);
    }

    public SupermarketDTO save(SupermarketDTO dto) {
        if (supermarketRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalArgumentException("Ya existe un supermercado con ese nombre.");
        }
        Supermarket supermarket = convertToEntity(dto);
        Supermarket saved = supermarketRepository.save(supermarket);
        return convertToDTO(saved);
    }

    public SupermarketDTO update(Long id, SupermarketDTO dto) {
        Supermarket existing = supermarketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supermercado no encontrado"));

        if (supermarketRepository.existsByNameIgnoreCaseAndIdNot(dto.getName(), id)) {
            throw new IllegalArgumentException("Ya existe otro supermercado con ese nombre.");
        }

        existing.setName(dto.getName());
        Supermarket saved = supermarketRepository.save(existing);
        return convertToDTO(saved);
    }

    public void deleteById(Long id) {
        if (!supermarketRepository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar. Supermercado no encontrado.");
        }
        supermarketRepository.deleteById(id);
    }

    // --- MAPPERS ---

    private SupermarketDTO convertToDTO(Supermarket entity) {
        return new SupermarketDTO(entity.getId(), entity.getName());
    }

    private Supermarket convertToEntity(SupermarketDTO dto) {
        Supermarket entity = new Supermarket();
        if (dto.getId() != null) entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }
}
