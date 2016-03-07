package plumbeer.dev.web.rest;

import plumbeer.dev.Application;
import plumbeer.dev.domain.Anuncio;
import plumbeer.dev.repository.AnuncioRepository;

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
 * Test class for the AnuncioResource REST controller.
 *
 * @see AnuncioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AnuncioResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TITULO = "A";
    private static final String UPDATED_TITULO = "B";
    
    private static final String DEFAULT_CONTENIDO = "A";
    private static final String UPDATED_CONTENIDO = "B";

    private static final ZonedDateTime DEFAULT_PUBLICACION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_PUBLICACION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_PUBLICACION_STR = dateTimeFormatter.format(DEFAULT_PUBLICACION);

    @Inject
    private AnuncioRepository anuncioRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAnuncioMockMvc;

    private Anuncio anuncio;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnuncioResource anuncioResource = new AnuncioResource();
        ReflectionTestUtils.setField(anuncioResource, "anuncioRepository", anuncioRepository);
        this.restAnuncioMockMvc = MockMvcBuilders.standaloneSetup(anuncioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        anuncio = new Anuncio();
        anuncio.setTitulo(DEFAULT_TITULO);
        anuncio.setContenido(DEFAULT_CONTENIDO);
        anuncio.setPublicacion(DEFAULT_PUBLICACION);
    }

    @Test
    @Transactional
    public void createAnuncio() throws Exception {
        int databaseSizeBeforeCreate = anuncioRepository.findAll().size();

        // Create the Anuncio

        restAnuncioMockMvc.perform(post("/api/anuncios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(anuncio)))
                .andExpect(status().isCreated());

        // Validate the Anuncio in the database
        List<Anuncio> anuncios = anuncioRepository.findAll();
        assertThat(anuncios).hasSize(databaseSizeBeforeCreate + 1);
        Anuncio testAnuncio = anuncios.get(anuncios.size() - 1);
        assertThat(testAnuncio.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testAnuncio.getContenido()).isEqualTo(DEFAULT_CONTENIDO);
        assertThat(testAnuncio.getPublicacion()).isEqualTo(DEFAULT_PUBLICACION);
    }

    @Test
    @Transactional
    public void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = anuncioRepository.findAll().size();
        // set the field null
        anuncio.setTitulo(null);

        // Create the Anuncio, which fails.

        restAnuncioMockMvc.perform(post("/api/anuncios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(anuncio)))
                .andExpect(status().isBadRequest());

        List<Anuncio> anuncios = anuncioRepository.findAll();
        assertThat(anuncios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContenidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = anuncioRepository.findAll().size();
        // set the field null
        anuncio.setContenido(null);

        // Create the Anuncio, which fails.

        restAnuncioMockMvc.perform(post("/api/anuncios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(anuncio)))
                .andExpect(status().isBadRequest());

        List<Anuncio> anuncios = anuncioRepository.findAll();
        assertThat(anuncios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnuncios() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncios
        restAnuncioMockMvc.perform(get("/api/anuncios?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(anuncio.getId().intValue())))
                .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO.toString())))
                .andExpect(jsonPath("$.[*].contenido").value(hasItem(DEFAULT_CONTENIDO.toString())))
                .andExpect(jsonPath("$.[*].publicacion").value(hasItem(DEFAULT_PUBLICACION_STR)));
    }

    @Test
    @Transactional
    public void getAnuncio() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get the anuncio
        restAnuncioMockMvc.perform(get("/api/anuncios/{id}", anuncio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(anuncio.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO.toString()))
            .andExpect(jsonPath("$.contenido").value(DEFAULT_CONTENIDO.toString()))
            .andExpect(jsonPath("$.publicacion").value(DEFAULT_PUBLICACION_STR));
    }

    @Test
    @Transactional
    public void getNonExistingAnuncio() throws Exception {
        // Get the anuncio
        restAnuncioMockMvc.perform(get("/api/anuncios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnuncio() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

		int databaseSizeBeforeUpdate = anuncioRepository.findAll().size();

        // Update the anuncio
        anuncio.setTitulo(UPDATED_TITULO);
        anuncio.setContenido(UPDATED_CONTENIDO);
        anuncio.setPublicacion(UPDATED_PUBLICACION);

        restAnuncioMockMvc.perform(put("/api/anuncios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(anuncio)))
                .andExpect(status().isOk());

        // Validate the Anuncio in the database
        List<Anuncio> anuncios = anuncioRepository.findAll();
        assertThat(anuncios).hasSize(databaseSizeBeforeUpdate);
        Anuncio testAnuncio = anuncios.get(anuncios.size() - 1);
        assertThat(testAnuncio.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testAnuncio.getContenido()).isEqualTo(UPDATED_CONTENIDO);
        assertThat(testAnuncio.getPublicacion()).isEqualTo(UPDATED_PUBLICACION);
    }

    @Test
    @Transactional
    public void deleteAnuncio() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

		int databaseSizeBeforeDelete = anuncioRepository.findAll().size();

        // Get the anuncio
        restAnuncioMockMvc.perform(delete("/api/anuncios/{id}", anuncio.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Anuncio> anuncios = anuncioRepository.findAll();
        assertThat(anuncios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
