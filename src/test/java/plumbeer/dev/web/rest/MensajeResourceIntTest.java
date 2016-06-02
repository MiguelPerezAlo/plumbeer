package plumbeer.dev.web.rest;

import plumbeer.dev.Application;
import plumbeer.dev.domain.Mensaje;
import plumbeer.dev.repository.MensajeRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MensajeResource REST controller.
 *
 * @see MensajeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MensajeResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_ASUNTO = "AAAAA";
    private static final String UPDATED_ASUNTO = "BBBBB";
    
    private static final String DEFAULT_CUERPO = "AAAAA";
    private static final String UPDATED_CUERPO = "BBBBB";

    private static final ZonedDateTime DEFAULT_FECHA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHA_STR = dateTimeFormatter.format(DEFAULT_FECHA);

    private static final Boolean DEFAULT_LEIDO = false;
    private static final Boolean UPDATED_LEIDO = true;

    @Inject
    private MensajeRepository mensajeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMensajeMockMvc;

    private Mensaje mensaje;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MensajeResource mensajeResource = new MensajeResource();
        ReflectionTestUtils.setField(mensajeResource, "mensajeRepository", mensajeRepository);
        this.restMensajeMockMvc = MockMvcBuilders.standaloneSetup(mensajeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        mensaje = new Mensaje();
        mensaje.setAsunto(DEFAULT_ASUNTO);
        mensaje.setCuerpo(DEFAULT_CUERPO);
        mensaje.setFecha(DEFAULT_FECHA);
        mensaje.setLeido(DEFAULT_LEIDO);
    }

    @Test
    @Transactional
    public void createMensaje() throws Exception {
        int databaseSizeBeforeCreate = mensajeRepository.findAll().size();

        // Create the Mensaje

        restMensajeMockMvc.perform(post("/api/mensajes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mensaje)))
                .andExpect(status().isCreated());

        // Validate the Mensaje in the database
        List<Mensaje> mensajes = mensajeRepository.findAll();
        assertThat(mensajes).hasSize(databaseSizeBeforeCreate + 1);
        Mensaje testMensaje = mensajes.get(mensajes.size() - 1);
        assertThat(testMensaje.getAsunto()).isEqualTo(DEFAULT_ASUNTO);
        assertThat(testMensaje.getCuerpo()).isEqualTo(DEFAULT_CUERPO);
        assertThat(testMensaje.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testMensaje.getLeido()).isEqualTo(DEFAULT_LEIDO);
    }

    @Test
    @Transactional
    public void getAllMensajes() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get all the mensajes
        restMensajeMockMvc.perform(get("/api/mensajes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(mensaje.getId().intValue())))
                .andExpect(jsonPath("$.[*].asunto").value(hasItem(DEFAULT_ASUNTO.toString())))
                .andExpect(jsonPath("$.[*].cuerpo").value(hasItem(DEFAULT_CUERPO.toString())))
                .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA_STR)))
                .andExpect(jsonPath("$.[*].leido").value(hasItem(DEFAULT_LEIDO.booleanValue())));
    }

    @Test
    @Transactional
    public void getMensaje() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get the mensaje
        restMensajeMockMvc.perform(get("/api/mensajes/{id}", mensaje.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(mensaje.getId().intValue()))
            .andExpect(jsonPath("$.asunto").value(DEFAULT_ASUNTO.toString()))
            .andExpect(jsonPath("$.cuerpo").value(DEFAULT_CUERPO.toString()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA_STR))
            .andExpect(jsonPath("$.leido").value(DEFAULT_LEIDO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMensaje() throws Exception {
        // Get the mensaje
        restMensajeMockMvc.perform(get("/api/mensajes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMensaje() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

		int databaseSizeBeforeUpdate = mensajeRepository.findAll().size();

        // Update the mensaje
        mensaje.setAsunto(UPDATED_ASUNTO);
        mensaje.setCuerpo(UPDATED_CUERPO);
        mensaje.setFecha(UPDATED_FECHA);
        mensaje.setLeido(UPDATED_LEIDO);

        restMensajeMockMvc.perform(put("/api/mensajes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mensaje)))
                .andExpect(status().isOk());

        // Validate the Mensaje in the database
        List<Mensaje> mensajes = mensajeRepository.findAll();
        assertThat(mensajes).hasSize(databaseSizeBeforeUpdate);
        Mensaje testMensaje = mensajes.get(mensajes.size() - 1);
        assertThat(testMensaje.getAsunto()).isEqualTo(UPDATED_ASUNTO);
        assertThat(testMensaje.getCuerpo()).isEqualTo(UPDATED_CUERPO);
        assertThat(testMensaje.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testMensaje.getLeido()).isEqualTo(UPDATED_LEIDO);
    }

    @Test
    @Transactional
    public void deleteMensaje() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

		int databaseSizeBeforeDelete = mensajeRepository.findAll().size();

        // Get the mensaje
        restMensajeMockMvc.perform(delete("/api/mensajes/{id}", mensaje.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Mensaje> mensajes = mensajeRepository.findAll();
        assertThat(mensajes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
