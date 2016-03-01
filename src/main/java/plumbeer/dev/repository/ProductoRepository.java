package plumbeer.dev.repository;

import plumbeer.dev.domain.Producto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Producto entity.
 */
public interface ProductoRepository extends JpaRepository<Producto,Long> {

    @Query("select producto from Producto producto where producto.user.login = ?#{principal.username}")
    List<Producto> findByUserIsCurrentUser();

}
