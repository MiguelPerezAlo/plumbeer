package plumbeer.dev.web.rest;

import com.codahale.metrics.annotation.Timed;
import plumbeer.dev.domain.Votacion;
import plumbeer.dev.repository.VotacionRepository;
import plumbeer.dev.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Votacion.
 */
@RestController
@RequestMapping("/api")
public class VotacionResource {

    private final Logger log = LoggerFactory.getLogger(VotacionResource.class);
        
    @Inject
    private VotacionRepository votacionRepository;
    
    /**
     * POST  /votacions -> Create a new votacion.
     */
    @RequestMapping(value = "/votacions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Votacion> createVotacion(@RequestBody Votacion votacion) throws URISyntaxException {
        log.debug("REST request to save Votacion : {}", votacion);
        if (votacion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("votacion", "idexists", "A new votacion cannot already have an ID")).body(null);
        }
        Votacion result = votacionRepository.save(votacion);
        return ResponseEntity.created(new URI("/api/votacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("votacion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /votacions -> Updates an existing votacion.
     */
    @RequestMapping(value = "/votacions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Votacion> updateVotacion(@RequestBody Votacion votacion) throws URISyntaxException {
        log.debug("REST request to update Votacion : {}", votacion);
        if (votacion.getId() == null) {
            return createVotacion(votacion);
        }
        Votacion result = votacionRepository.save(votacion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("votacion", votacion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /votacions -> get all the votacions.
     */
    @RequestMapping(value = "/votacions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Votacion> getAllVotacions() {
        log.debug("REST request to get all Votacions");
        return votacionRepository.findAll();
            }

    /**
     * GET  /votacions/:id -> get the "id" votacion.
     */
    @RequestMapping(value = "/votacions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Votacion> getVotacion(@PathVariable Long id) {
        log.debug("REST request to get Votacion : {}", id);
        Votacion votacion = votacionRepository.findOne(id);
        return Optional.ofNullable(votacion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /votacions/:id -> delete the "id" votacion.
     */
    @RequestMapping(value = "/votacions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVotacion(@PathVariable Long id) {
        log.debug("REST request to delete Votacion : {}", id);
        votacionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("votacion", id.toString())).build();
    }
}
