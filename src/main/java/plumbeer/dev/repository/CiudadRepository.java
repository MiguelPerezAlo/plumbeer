package plumbeer.dev.repository;

import plumbeer.dev.domain.Ciudad;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ciudad entity.
 */
public interface CiudadRepository extends JpaRepository<Ciudad,Long> {

}
