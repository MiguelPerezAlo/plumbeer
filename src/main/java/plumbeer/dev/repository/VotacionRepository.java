package plumbeer.dev.repository;

import plumbeer.dev.domain.Votacion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Votacion entity.
 */
public interface VotacionRepository extends JpaRepository<Votacion,Long> {

    @Query("select votacion from Votacion votacion where votacion.votante.login = ?#{principal.username}")
    List<Votacion> findByVotanteIsCurrentUser();

    @Query("select votacion from Votacion votacion where votacion.votado.login = ?#{principal.username}")
    List<Votacion> findByVotadoIsCurrentUser();

}
