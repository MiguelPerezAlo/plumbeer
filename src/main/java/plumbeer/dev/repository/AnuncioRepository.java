package plumbeer.dev.repository;

import plumbeer.dev.domain.Anuncio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Anuncio entity.
 */
public interface AnuncioRepository extends JpaRepository<Anuncio,Long> {

    @Query("select anuncio from Anuncio anuncio where anuncio.user.login = ?#{principal.username}")
    List<Anuncio> findByUserIsCurrentUser();

}
