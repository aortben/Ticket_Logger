package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {

    private Long id;

    @NotEmpty(message = "{msg.location.address.notEmpty}")
    private String address;

    @NotEmpty(message = "{msg.location.city.notEmpty}")
    private String city;

    // Relaciones por ID (Input/Output simplificado)
    @NotNull(message = "{msg.location.supermarket.notNull}")
    private Long supermarketId;

    @NotNull(message = "{msg.location.province.notNull}")
    private Long provinceId;

    // Opcional: Nombres para mostrar en tablas sin hacer peticiones extra (Solo salida)
    private String supermarketName;
    private String provinceName;
}
