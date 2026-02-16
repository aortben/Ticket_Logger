package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities.Supermarket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupermarketRepository extends JpaRepository<Supermarket, Long> {

    // Búsqueda para el filtro
    Page<Supermarket> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Para evitar crear dos supermercados con el mismo nombre
    boolean existsByNameIgnoreCase(String name);

    // Para validar al editar (que no pongas el nombre de otro existente)
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
