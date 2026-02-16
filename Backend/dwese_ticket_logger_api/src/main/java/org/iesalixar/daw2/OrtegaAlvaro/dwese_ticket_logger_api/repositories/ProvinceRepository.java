package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.entities.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    Page<Province> findAll(Pageable pageable);

    // Búsqueda por nombre (ignorando mayúsculas/minúsculas)
    Page<Province> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Validaciones de unicidad
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
}
