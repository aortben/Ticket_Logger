package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {

    /**
     * Identificador único de la región.
     */
    private Long id;

    /**
     * Código de la región.
     */
    private String code;

    /**
     * Nombre completo de la región.
     */
    private String name;

    /**
     * Nombre de la imagen asociada.
     */
    private String image;
}