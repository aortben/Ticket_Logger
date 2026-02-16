package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "roles")
@EqualsAndHashCode(exclude = "roles")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.user.username.notEmpty}")
    @Size(max = 50, message = "{msg.user.username.size}")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * SEGURIDAD CRÍTICA:
     * @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
     * Esto permite recibir el password en el JSON (registro/login) pero NUNCA
     * devolverlo en la respuesta. Así el campo 'password' no aparecerá en Postman.
     */
    @NotEmpty(message = "{msg.user.password.notEmpty}")
    @Size(min = 8, message = "{msg.user.password.size}")
    @Column(name = "password", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "{msg.user.enabled.notNull}")
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @NotEmpty(message = "{msg.user.firstName.notEmpty}")
    @Size(max = 50, message = "{msg.user.firstName.size}")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotEmpty(message = "{msg.user.lastName.notEmpty}")
    @Size(max = 50, message = "{msg.user.lastName.size}")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Size(max = 255, message = "{msg.user.image.size}")
    @Column(name = "image", length = 255)
    private String image;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "last_password_change_date")
    private LocalDateTime lastPasswordChangeDate;

    // Relación ManyToMany con Roles.
    // IMPORTANTE: Revisar la entidad Role para evitar recursividad si Role tiene List<User>.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public void setPassword(String password) {
        this.password = password;
        this.lastPasswordChangeDate = LocalDateTime.now();
    }
}
