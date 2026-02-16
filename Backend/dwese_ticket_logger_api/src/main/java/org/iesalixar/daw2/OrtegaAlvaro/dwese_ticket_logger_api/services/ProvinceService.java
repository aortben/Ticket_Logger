package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.DTOs.ProvinceDTO;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.DTOs.RegionDTO;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.repositories.ProvinceRepository;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.repositories.RegionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private RegionRepository regionRepository;

    public Page<ProvinceDTO> findAll(Pageable pageable, String search) {
        Page<Province> page;
        if (search != null && !search.isBlank()) {
            page = provinceRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            page = provinceRepository.findAll(pageable);
        }
        return page.map(this::convertToDTO);
    }

    public Optional<ProvinceDTO> findById(Long id) {
        return provinceRepository.findById(id).map(this::convertToDTO);
    }

    public ProvinceDTO save(ProvinceDTO provinceDTO) {
        if (provinceRepository.existsByCode(provinceDTO.getCode())) {
            throw new IllegalArgumentException("El código de la provincia ya existe.");
        }
        Province province = convertToEntity(provinceDTO);
        Province savedProvince = provinceRepository.save(province);
        return convertToDTO(savedProvince);
    }

    public ProvinceDTO update(Long id, ProvinceDTO provinceDTO) {
        Province existingProvince = provinceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provincia no encontrada con ID: " + id));

        if (provinceRepository.existsByCodeAndIdNot(provinceDTO.getCode(), id)) {
            throw new IllegalArgumentException("El código de la provincia ya existe en otra provincia.");
        }

        existingProvince.setCode(provinceDTO.getCode());
        existingProvince.setName(provinceDTO.getName());

        // Solo buscamos la región si el ID ha cambiado
        if (!existingProvince.getRegion().getId().equals(provinceDTO.getRegion().getId())) {
            Region region = regionRepository.findById(provinceDTO.getRegion().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Región no encontrada"));
            existingProvince.setRegion(region);
        }

        Province savedProvince = provinceRepository.save(existingProvince);
        return convertToDTO(savedProvince);
    }

    public void deleteById(Long id) {
        if (!provinceRepository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar. Provincia no encontrada.");
        }
        provinceRepository.deleteById(id);
    }

    // --- MAPPERS ---

    private ProvinceDTO convertToDTO(Province province) {
        RegionDTO regionDTO = new RegionDTO(
                province.getRegion().getId(),
                province.getRegion().getCode(),
                province.getRegion().getName(),
                province.getRegion().getImage()
        );
        return new ProvinceDTO(
                province.getId(),
                province.getCode(),
                province.getName(),
                regionDTO
        );
    }

    private Province convertToEntity(ProvinceDTO dto) {
        Region region = regionRepository.findById(dto.getRegion().getId())
                .orElseThrow(() -> new EntityNotFoundException("Región no encontrada con ID: " + dto.getRegion().getId()));

        Province province = new Province();
        // Si el DTO trae ID, lo seteamos (útil para updates internos o lógica futura)
        if(dto.getId() != null) province.setId(dto.getId());

        province.setCode(dto.getCode());
        province.setName(dto.getName());
        province.setRegion(region);
        return province;
    }
}