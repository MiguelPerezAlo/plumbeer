package plumbeer.dev.web.rest;

import com.codahale.metrics.annotation.Timed;
import plumbeer.dev.domain.Mensaje;
import plumbeer.dev.domain.User;
import plumbeer.dev.repository.MensajeRepository;
import plumbeer.dev.repository.UserRepository;
import plumbeer.dev.security.SecurityUtils;
import plumbeer.dev.web.rest.util.HeaderUtil;
import plumbeer.dev.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Mensaje.
 */
@RestController
@RequestMapping("/api")
public class MensajeResource {

    private final Logger log = LoggerFactory.getLogger(MensajeResource.class);

    @Inject
    private MensajeRepository mensajeRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /mensajes -> Create a new mensaje.
     */
    @RequestMapping(value = "/mensajes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mensaje> createMensaje(@RequestBody Mensaje mensaje) throws URISyntaxException {
        log.debug("REST request to save Mensaje : {}", mensaje);
        if (mensaje.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("mensaje", "idexists", "A new mensaje cannot already have an ID")).body(null);
        }
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        mensaje.setEmisor(user);
        mensaje.setLeido(false);
        ZonedDateTime z = ZonedDateTime.now(ZoneId.systemDefault());
        mensaje.setFecha(z);
        Mensaje result = mensajeRepository.save(mensaje);
        return ResponseEntity.created(new URI("/api/mensajes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mensaje", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mensajes -> Updates an existing mensaje.
     */
    @RequestMapping(value = "/mensajes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mensaje> updateMensaje(@RequestBody Mensaje mensaje) throws URISyntaxException {
        log.debug("REST request to update Mensaje : {}", mensaje);
        if (mensaje.getId() == null) {
            return createMensaje(mensaje);
        }
        Mensaje result = mensajeRepository.save(mensaje);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mensaje", mensaje.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mensajes -> get all the mensajes.
     */
    @RequestMapping(value = "/mensajes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Mensaje>> getAllMensajes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Mensajes");
        Page<Mensaje> page = mensajeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mensajes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mensajes/:id -> get the "id" mensaje.
     */
    @RequestMapping(value = "/mensajes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mensaje> getMensaje(@PathVariable Long id) {
        log.debug("REST request to get Mensaje : {}", id);
        Mensaje mensaje = mensajeRepository.findOne(id);
        return Optional.ofNullable(mensaje)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mensajes/:id -> delete the "id" mensaje.
     */
    @RequestMapping(value = "/mensajes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMensaje(@PathVariable Long id) {
        log.debug("REST request to delete Mensaje : {}", id);
        mensajeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mensaje", id.toString())).build();
    }
}
