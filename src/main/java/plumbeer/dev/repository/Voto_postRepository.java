package plumbeer.dev.repository;

import plumbeer.dev.domain.Voto_post;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Voto_post entity.
 */
public interface Voto_postRepository extends JpaRepository<Voto_post,Long> {

    @Query("select voto_post from Voto_post voto_post where voto_post.votante.login = ?#{principal.username}")
    List<Voto_post> findByVotanteIsCurrentUser();

}
