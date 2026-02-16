package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.DTOs.RegionCreateDTO;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.DTOs.RegionDTO;
import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities.Region;
import org.springframework.stereotype.Component;

@Component
public class RegionMapper {

    /**
     * Convierte una entidad `Region` a un `RegionDTO` (datos básicos).
     *
     * @param region Entidad de región.
     * @return DTO correspondiente.
     */
    public RegionDTO toDTO(Region region) {
        RegionDTO dto = new RegionDTO();
        dto.setId(region.getId());
        dto.setCode(region.getCode());
        dto.setName(region.getName());
        dto.setImage(region.getImage());

        return dto;
    }

    public Region toEntity(RegionDTO dto) {
        Region region = new Region();
        region.setId(dto.getId());
        region.setCode(dto.getCode());
        region.setName(dto.getName());
        return region;
    }

    public Region toEntity(RegionCreateDTO createDTO) {
        Region region = new Region();
        region.setCode(createDTO.getCode());
        region.setName(createDTO.getName());
        //La imagen se hace sola en el service
        return region;
    }
}
