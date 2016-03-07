package plumbeer.dev.web.rest;

import com.codahale.metrics.annotation.Timed;
import plumbeer.dev.domain.Anuncio;
import plumbeer.dev.repository.AnuncioRepository;
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
 * REST controller for managing Anuncio.
 */
@RestController
@RequestMapping("/api")
public class AnuncioResource {

    private final Logger log = LoggerFactory.getLogger(AnuncioResource.class);
        
    @Inject
    private AnuncioRepository anuncioRepository;
    
    /**
     * POST  /anuncios -> Create a new anuncio.
     */
    @RequestMapping(value = "/anuncios",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Anuncio> createAnuncio(@Valid @RequestBody Anuncio anuncio) throws URISyntaxException {
        log.debug("REST request to save Anuncio : {}", anuncio);
        if (anuncio.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("anuncio", "idexists", "A new anuncio cannot already have an ID")).body(null);
        }
        Anuncio result = anuncioRepository.save(anuncio);
        return ResponseEntity.created(new URI("/api/anuncios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("anuncio", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /anuncios -> Updates an existing anuncio.
     */
    @RequestMapping(value = "/anuncios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Anuncio> updateAnuncio(@Valid @RequestBody Anuncio anuncio) throws URISyntaxException {
        log.debug("REST request to update Anuncio : {}", anuncio);
        if (anuncio.getId() == null) {
            return createAnuncio(anuncio);
        }
        Anuncio result = anuncioRepository.save(anuncio);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("anuncio", anuncio.getId().toString()))
            .body(result);
    }

    /**
     * GET  /anuncios -> get all the anuncios.
     */
    @RequestMapping(value = "/anuncios",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Anuncio> getAllAnuncios() {
        log.debug("REST request to get all Anuncios");
        return anuncioRepository.findAll();
            }

    /**
     * GET  /anuncios/:id -> get the "id" anuncio.
     */
    @RequestMapping(value = "/anuncios/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Anuncio> getAnuncio(@PathVariable Long id) {
        log.debug("REST request to get Anuncio : {}", id);
        Anuncio anuncio = anuncioRepository.findOne(id);
        return Optional.ofNullable(anuncio)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /anuncios/:id -> delete the "id" anuncio.
     */
    @RequestMapping(value = "/anuncios/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAnuncio(@PathVariable Long id) {
        log.debug("REST request to delete Anuncio : {}", id);
        anuncioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("anuncio", id.toString())).build();
    }
}
