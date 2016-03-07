package plumbeer.dev.web.rest;

import plumbeer.dev.Application;
import plumbeer.dev.domain.Opinion;
import plumbeer.dev.repository.OpinionRepository;

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
 * Test class for the OpinionResource REST controller.
 *
 * @see OpinionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OpinionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TITULO = "AAAAA";
    private static final String UPDATED_TITULO = "BBBBB";
    
    private static final String DEFAULT_COMENTARIO = "AAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBB";

    private static final ZonedDateTime DEFAULT_FECHA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHA_STR = dateTimeFormatter.format(DEFAULT_FECHA);

    @Inject
    private OpinionRepository opinionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOpinionMockMvc;

    private Opinion opinion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OpinionResource opinionResource = new OpinionResource();
        ReflectionTestUtils.setField(opinionResource, "opinionRepository", opinionRepository);
        this.restOpinionMockMvc = MockMvcBuilders.standaloneSetup(opinionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        opinion = new Opinion();
        opinion.setTitulo(DEFAULT_TITULO);
        opinion.setComentario(DEFAULT_COMENTARIO);
        opinion.setFecha(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    public void createOpinion() throws Exception {
        int databaseSizeBeforeCreate = opinionRepository.findAll().size();

        // Create the Opinion

        restOpinionMockMvc.perform(post("/api/opinions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(opinion)))
                .andExpect(status().isCreated());

        // Validate the Opinion in the database
        List<Opinion> opinions = opinionRepository.findAll();
        assertThat(opinions).hasSize(databaseSizeBeforeCreate + 1);
        Opinion testOpinion = opinions.get(opinions.size() - 1);
        assertThat(testOpinion.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testOpinion.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
        assertThat(testOpinion.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    public void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = opinionRepository.findAll().size();
        // set the field null
        opinion.setTitulo(null);

        // Create the Opinion, which fails.

        restOpinionMockMvc.perform(post("/api/opinions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(opinion)))
                .andExpect(status().isBadRequest());

        List<Opinion> opinions = opinionRepository.findAll();
        assertThat(opinions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkComentarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = opinionRepository.findAll().size();
        // set the field null
        opinion.setComentario(null);

        // Create the Opinion, which fails.

        restOpinionMockMvc.perform(post("/api/opinions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(opinion)))
                .andExpect(status().isBadRequest());

        List<Opinion> opinions = opinionRepository.findAll();
        assertThat(opinions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOpinions() throws Exception {
        // Initialize the database
        opinionRepository.saveAndFlush(opinion);

        // Get all the opinions
        restOpinionMockMvc.perform(get("/api/opinions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(opinion.getId().intValue())))
                .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO.toString())))
                .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO.toString())))
                .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA_STR)));
    }

    @Test
    @Transactional
    public void getOpinion() throws Exception {
        // Initialize the database
        opinionRepository.saveAndFlush(opinion);

        // Get the opinion
        restOpinionMockMvc.perform(get("/api/opinions/{id}", opinion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(opinion.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO.toString()))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO.toString()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA_STR));
    }

    @Test
    @Transactional
    public void getNonExistingOpinion() throws Exception {
        // Get the opinion
        restOpinionMockMvc.perform(get("/api/opinions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOpinion() throws Exception {
        // Initialize the database
        opinionRepository.saveAndFlush(opinion);

		int databaseSizeBeforeUpdate = opinionRepository.findAll().size();

        // Update the opinion
        opinion.setTitulo(UPDATED_TITULO);
        opinion.setComentario(UPDATED_COMENTARIO);
        opinion.setFecha(UPDATED_FECHA);

        restOpinionMockMvc.perform(put("/api/opinions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(opinion)))
                .andExpect(status().isOk());

        // Validate the Opinion in the database
        List<Opinion> opinions = opinionRepository.findAll();
        assertThat(opinions).hasSize(databaseSizeBeforeUpdate);
        Opinion testOpinion = opinions.get(opinions.size() - 1);
        assertThat(testOpinion.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testOpinion.getComentario()).isEqualTo(UPDATED_COMENTARIO);
        assertThat(testOpinion.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void deleteOpinion() throws Exception {
        // Initialize the database
        opinionRepository.saveAndFlush(opinion);

		int databaseSizeBeforeDelete = opinionRepository.findAll().size();

        // Get the opinion
        restOpinionMockMvc.perform(delete("/api/opinions/{id}", opinion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Opinion> opinions = opinionRepository.findAll();
        assertThat(opinions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
