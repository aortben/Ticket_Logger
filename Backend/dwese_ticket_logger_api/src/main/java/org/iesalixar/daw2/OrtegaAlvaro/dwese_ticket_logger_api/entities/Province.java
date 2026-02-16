package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "province")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Province {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.province.code.notEmpty}")
    @Size(max = 10, message = "{msg.province.code.size}")
    @Column(name = "code", nullable = false, length = 10, unique = true)
    private String code;

    @NotEmpty(message = "{msg.province.name.notEmpty}")
    @Size(max = 100, message = "{msg.province.name.size}")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Relación ManyToOne con Region
    @NotNull(message = "{msg.province.region.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita error al serializar el proxy Lazy
    private Region region;

    // Relación con Location (Descomentada)
    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // IMPORTANTE: Corta el bucle infinito. No envía la lista de localidades al pedir una provincia.
    private List<Location> locations;

    public Province(String code, String name, Region region) {
        this.code = code;
        this.name = name;
        this.region = region;
    }

    // Campo calculado útil para el JSON
    public Long getRegionId() {
        return region != null ? region.getId() : 0;
    }
}
