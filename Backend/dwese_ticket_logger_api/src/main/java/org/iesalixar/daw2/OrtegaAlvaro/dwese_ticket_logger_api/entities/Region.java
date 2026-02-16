package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "region")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // Añadido para evitar problemas con JPA
public class Region {

    // Campo que almacena el identificador único de la región.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que almacena el código de la región.
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    // Campo que almacena el nombre completo de la región.
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // NUEVO: Campo para almacenar el nombre del archivo de imagen
    @Column(name = "image")
    private String image;

    // Relación uno a muchos con la entidad Province.
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Province> provinces;

    /**
     * Constructor personalizado.
     */
    public Region(String code, String name) {
        this.code = code;
        this.name = name;
    }
}