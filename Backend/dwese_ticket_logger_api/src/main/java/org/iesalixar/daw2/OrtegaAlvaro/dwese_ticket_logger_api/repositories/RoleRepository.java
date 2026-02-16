package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
