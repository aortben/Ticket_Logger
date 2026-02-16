package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.services;


import jakarta.persistence.EntityNotFoundException;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.DTOs.RoleDTO;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities.Role;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RoleDTO save(RoleDTO dto) {
        Role role = new Role(dto.getName());
        return convertToDTO(roleRepository.save(role));
    }

    public void deleteById(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Rol no encontrado");
        }
        roleRepository.deleteById(id);
    }

    private RoleDTO convertToDTO(Role role) {
        return new RoleDTO(role.getId(), role.getName());
    }
}
