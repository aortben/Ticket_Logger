package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupermarketDTO {

    private Long id;

    @NotEmpty(message = "{msg.supermarket.name.notEmpty}")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String name;
}
