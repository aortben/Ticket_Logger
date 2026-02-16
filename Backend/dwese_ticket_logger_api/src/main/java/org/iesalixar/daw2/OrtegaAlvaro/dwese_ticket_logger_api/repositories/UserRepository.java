package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
