package plumbeer.dev.repository;

import plumbeer.dev.domain.Opinion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Opinion entity.
 */
public interface OpinionRepository extends JpaRepository<Opinion,Long> {

    @Query("select opinion from Opinion opinion where opinion.user.login = ?#{principal.username}")
    List<Opinion> findByUserIsCurrentUser();

}
