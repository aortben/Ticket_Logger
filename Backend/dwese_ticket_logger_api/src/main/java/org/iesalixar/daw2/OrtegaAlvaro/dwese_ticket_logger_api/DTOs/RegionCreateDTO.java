package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Schema(description = "DTO para la creación de una nueva región") // Describe el objeto globalmente
public class RegionCreateDTO {

    /**
     * Código único de la región.
     */
    @Schema(description = "Código de la región (ej. '01')", example = "01", maxLength = 10)
    @NotEmpty(message = "{msg.region.code.notEmpty}")
    @Size(max = 10, message = "{msg.region.code.size}")
    private String code;

    /**
     * Nombre completo de la región.
     */
    @Schema(description = "Nombre oficial de la región", example = "Andalucía", maxLength = 100)
    @NotEmpty(message = "{msg.region.name.notEmpty}")
    @Size(max = 100, message = "{msg.region.name.size}")
    private String name;

    /**
     * Archivo de imagen subido desde el formulario.
     */
    @Schema(description = "Imagen representativa de la región", type = "string", format = "binary")
    private MultipartFile imageFile;
}
