package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.repositories;


import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // Búsqueda flexible: Busca en dirección O en ciudad
    @Query("SELECT l FROM Location l WHERE " +
            "LOWER(l.address) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(l.city) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Location> findByAddressOrCityContaining(@Param("search") String search, Pageable pageable);
}
