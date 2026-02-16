package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidad Supermarket preparada para API REST.
 */
@Entity
@Table(name = "supermarkets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supermarket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.supermarket.name.notEmpty}")
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Relación uno a muchos con Location.
     * IMPORTANTE: Añadimos @JsonIgnore para romper el ciclo de serialización JSON.
     * Cuando pidas un supermercado, no te traerá todas sus ubicaciones incrustadas.
     */
    @OneToMany(mappedBy = "supermarket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Location> locations;

    public Supermarket(String name) {
        this.name = name;
    }
}