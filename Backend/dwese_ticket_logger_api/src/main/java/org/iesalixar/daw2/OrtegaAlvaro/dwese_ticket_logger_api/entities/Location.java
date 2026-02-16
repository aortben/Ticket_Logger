package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase `Location` refactorizada para API REST.
 */
@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.location.address.notEmpty}")
    @Column(name = "address", nullable = false)
    private String address;

    @NotEmpty(message = "{msg.location.city.notEmpty}")
    @Column(name = "city", nullable = false)
    private String city;

    // Relación con Supermercado
    @NotNull(message = "{msg.location.supermarket.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supermarket_id", nullable = false)
    // IMPORTANTE: Permite que se serialice el objeto Supermercado ignorando el proxy de Hibernate
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Supermarket supermarket;

    // Relación con Provincia
    @NotNull(message = "{msg.location.province.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    // IMPORTANTE: Permite que se serialice el objeto Provincia ignorando el proxy de Hibernate
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Province province;

    public Location(String address, String city, Supermarket supermarket, Province province) {
        this.address = address;
        this.city = city;
        this.supermarket = supermarket;
        this.province = province;
    }

    // --- Helpers para el JSON (Frontend Friendly) ---

    // Genera un campo "supermarketId" en el JSON
    public Long getSupermarketId() {
        return supermarket != null ? supermarket.getId() : null;
    }

    // Genera un campo "provinceId" en el JSON
    public Long getProvinceId() {
        return province != null ? province.getId() : null;
    }
}