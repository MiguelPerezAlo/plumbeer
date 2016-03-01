package plumbeer.dev.repository;

import plumbeer.dev.domain.Categoria;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Categoria entity.
 */
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

}
