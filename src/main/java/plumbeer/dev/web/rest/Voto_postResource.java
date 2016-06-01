package plumbeer.dev.web.rest;

import com.codahale.metrics.annotation.Timed;
import plumbeer.dev.domain.Voto_post;
import plumbeer.dev.repository.Voto_postRepository;
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
 * REST controller for managing Voto_post.
 */
@RestController
@RequestMapping("/api")
public class Voto_postResource {

    private final Logger log = LoggerFactory.getLogger(Voto_postResource.class);
        
    @Inject
    private Voto_postRepository voto_postRepository;
    
    /**
     * POST  /voto_posts -> Create a new voto_post.
     */
    @RequestMapping(value = "/voto_posts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Voto_post> createVoto_post(@RequestBody Voto_post voto_post) throws URISyntaxException {
        log.debug("REST request to save Voto_post : {}", voto_post);
        if (voto_post.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("voto_post", "idexists", "A new voto_post cannot already have an ID")).body(null);
        }
        Voto_post result = voto_postRepository.save(voto_post);
        return ResponseEntity.created(new URI("/api/voto_posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("voto_post", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /voto_posts -> Updates an existing voto_post.
     */
    @RequestMapping(value = "/voto_posts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Voto_post> updateVoto_post(@RequestBody Voto_post voto_post) throws URISyntaxException {
        log.debug("REST request to update Voto_post : {}", voto_post);
        if (voto_post.getId() == null) {
            return createVoto_post(voto_post);
        }
        Voto_post result = voto_postRepository.save(voto_post);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("voto_post", voto_post.getId().toString()))
            .body(result);
    }

    /**
     * GET  /voto_posts -> get all the voto_posts.
     */
    @RequestMapping(value = "/voto_posts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Voto_post> getAllVoto_posts() {
        log.debug("REST request to get all Voto_posts");
        return voto_postRepository.findAll();
            }

    /**
     * GET  /voto_posts/:id -> get the "id" voto_post.
     */
    @RequestMapping(value = "/voto_posts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Voto_post> getVoto_post(@PathVariable Long id) {
        log.debug("REST request to get Voto_post : {}", id);
        Voto_post voto_post = voto_postRepository.findOne(id);
        return Optional.ofNullable(voto_post)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /voto_posts/:id -> delete the "id" voto_post.
     */
    @RequestMapping(value = "/voto_posts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVoto_post(@PathVariable Long id) {
        log.debug("REST request to delete Voto_post : {}", id);
        voto_postRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("voto_post", id.toString())).build();
    }
}
