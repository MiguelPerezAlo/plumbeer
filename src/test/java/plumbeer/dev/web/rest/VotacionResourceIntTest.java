package plumbeer.dev.web.rest;

import plumbeer.dev.Application;
import plumbeer.dev.domain.Votacion;
import plumbeer.dev.repository.VotacionRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the VotacionResource REST controller.
 *
 * @see VotacionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class VotacionResourceIntTest {


    private static final Boolean DEFAULT_POSITIVO = false;
    private static final Boolean UPDATED_POSITIVO = true;
    
    private static final String DEFAULT_MOTIVO = "";
    private static final String UPDATED_MOTIVO = "";

    @Inject
    private VotacionRepository votacionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVotacionMockMvc;

    private Votacion votacion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VotacionResource votacionResource = new VotacionResource();
        ReflectionTestUtils.setField(votacionResource, "votacionRepository", votacionRepository);
        this.restVotacionMockMvc = MockMvcBuilders.standaloneSetup(votacionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        votacion = new Votacion();
        votacion.setPositivo(DEFAULT_POSITIVO);
        votacion.setMotivo(DEFAULT_MOTIVO);
    }

    @Test
    @Transactional
    public void createVotacion() throws Exception {
        int databaseSizeBeforeCreate = votacionRepository.findAll().size();

        // Create the Votacion

        restVotacionMockMvc.perform(post("/api/votacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(votacion)))
                .andExpect(status().isCreated());

        // Validate the Votacion in the database
        List<Votacion> votacions = votacionRepository.findAll();
        assertThat(votacions).hasSize(databaseSizeBeforeCreate + 1);
        Votacion testVotacion = votacions.get(votacions.size() - 1);
        assertThat(testVotacion.getPositivo()).isEqualTo(DEFAULT_POSITIVO);
        assertThat(testVotacion.getMotivo()).isEqualTo(DEFAULT_MOTIVO);
    }

    @Test
    @Transactional
    public void getAllVotacions() throws Exception {
        // Initialize the database
        votacionRepository.saveAndFlush(votacion);

        // Get all the votacions
        restVotacionMockMvc.perform(get("/api/votacions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(votacion.getId().intValue())))
                .andExpect(jsonPath("$.[*].positivo").value(hasItem(DEFAULT_POSITIVO.booleanValue())))
                .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO.toString())));
    }

    @Test
    @Transactional
    public void getVotacion() throws Exception {
        // Initialize the database
        votacionRepository.saveAndFlush(votacion);

        // Get the votacion
        restVotacionMockMvc.perform(get("/api/votacions/{id}", votacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(votacion.getId().intValue()))
            .andExpect(jsonPath("$.positivo").value(DEFAULT_POSITIVO.booleanValue()))
            .andExpect(jsonPath("$.motivo").value(DEFAULT_MOTIVO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVotacion() throws Exception {
        // Get the votacion
        restVotacionMockMvc.perform(get("/api/votacions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVotacion() throws Exception {
        // Initialize the database
        votacionRepository.saveAndFlush(votacion);

		int databaseSizeBeforeUpdate = votacionRepository.findAll().size();

        // Update the votacion
        votacion.setPositivo(UPDATED_POSITIVO);
        votacion.setMotivo(UPDATED_MOTIVO);

        restVotacionMockMvc.perform(put("/api/votacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(votacion)))
                .andExpect(status().isOk());

        // Validate the Votacion in the database
        List<Votacion> votacions = votacionRepository.findAll();
        assertThat(votacions).hasSize(databaseSizeBeforeUpdate);
        Votacion testVotacion = votacions.get(votacions.size() - 1);
        assertThat(testVotacion.getPositivo()).isEqualTo(UPDATED_POSITIVO);
        assertThat(testVotacion.getMotivo()).isEqualTo(UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    public void deleteVotacion() throws Exception {
        // Initialize the database
        votacionRepository.saveAndFlush(votacion);

		int databaseSizeBeforeDelete = votacionRepository.findAll().size();

        // Get the votacion
        restVotacionMockMvc.perform(delete("/api/votacions/{id}", votacion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Votacion> votacions = votacionRepository.findAll();
        assertThat(votacions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
