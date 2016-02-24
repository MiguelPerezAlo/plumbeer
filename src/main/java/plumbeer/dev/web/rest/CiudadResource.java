package plumbeer.dev.web.rest;

import com.codahale.metrics.annotation.Timed;
import plumbeer.dev.domain.Ciudad;
import plumbeer.dev.repository.CiudadRepository;
import plumbeer.dev.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Ciudad.
 */
@RestController
@RequestMapping("/api")
public class CiudadResource {

    private final Logger log = LoggerFactory.getLogger(CiudadResource.class);
        
    @Inject
    private CiudadRepository ciudadRepository;
    
    /**
     * POST  /ciudads -> Create a new ciudad.
     */
    @RequestMapping(value = "/ciudads",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ciudad> createCiudad(@Valid @RequestBody Ciudad ciudad) throws URISyntaxException {
        log.debug("REST request to save Ciudad : {}", ciudad);
        if (ciudad.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ciudad", "idexists", "A new ciudad cannot already have an ID")).body(null);
        }
        Ciudad result = ciudadRepository.save(ciudad);
        return ResponseEntity.created(new URI("/api/ciudads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ciudad", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ciudads -> Updates an existing ciudad.
     */
    @RequestMapping(value = "/ciudads",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ciudad> updateCiudad(@Valid @RequestBody Ciudad ciudad) throws URISyntaxException {
        log.debug("REST request to update Ciudad : {}", ciudad);
        if (ciudad.getId() == null) {
            return createCiudad(ciudad);
        }
        Ciudad result = ciudadRepository.save(ciudad);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ciudad", ciudad.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ciudads -> get all the ciudads.
     */
    @RequestMapping(value = "/ciudads",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ciudad> getAllCiudads() {
        log.debug("REST request to get all Ciudads");
        return ciudadRepository.findAll();
            }

    /**
     * GET  /ciudads/:id -> get the "id" ciudad.
     */
    @RequestMapping(value = "/ciudads/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ciudad> getCiudad(@PathVariable Long id) {
        log.debug("REST request to get Ciudad : {}", id);
        Ciudad ciudad = ciudadRepository.findOne(id);
        return Optional.ofNullable(ciudad)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ciudads/:id -> delete the "id" ciudad.
     */
    @RequestMapping(value = "/ciudads/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCiudad(@PathVariable Long id) {
        log.debug("REST request to delete Ciudad : {}", id);
        ciudadRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ciudad", id.toString())).build();
    }
}
