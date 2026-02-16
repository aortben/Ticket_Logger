package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.role.name.notEmpty}")
    @Size(max = 50, message = "{msg.role.name.size}")
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    // Relación muchos a muchos con User.
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore // Seguridad extra: Evita cargar todos los usuarios si se serializa el rol por accidente.
    private Set<User> users;

    public Role(String name) {
        this.name = name;
    }
}
