package plumbeer.dev.web.rest;

import plumbeer.dev.Application;
import plumbeer.dev.domain.Voto_post;
import plumbeer.dev.repository.Voto_postRepository;

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
 * Test class for the Voto_postResource REST controller.
 *
 * @see Voto_postResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Voto_postResourceIntTest {


    private static final Boolean DEFAULT_POSITIVO = false;
    private static final Boolean UPDATED_POSITIVO = true;
    
    private static final String DEFAULT_MOTIVO = "";
    private static final String UPDATED_MOTIVO = "";

    @Inject
    private Voto_postRepository voto_postRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVoto_postMockMvc;

    private Voto_post voto_post;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Voto_postResource voto_postResource = new Voto_postResource();
        ReflectionTestUtils.setField(voto_postResource, "voto_postRepository", voto_postRepository);
        this.restVoto_postMockMvc = MockMvcBuilders.standaloneSetup(voto_postResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        voto_post = new Voto_post();
        voto_post.setPositivo(DEFAULT_POSITIVO);
        voto_post.setMotivo(DEFAULT_MOTIVO);
    }

    @Test
    @Transactional
    public void createVoto_post() throws Exception {
        int databaseSizeBeforeCreate = voto_postRepository.findAll().size();

        // Create the Voto_post

        restVoto_postMockMvc.perform(post("/api/voto_posts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(voto_post)))
                .andExpect(status().isCreated());

        // Validate the Voto_post in the database
        List<Voto_post> voto_posts = voto_postRepository.findAll();
        assertThat(voto_posts).hasSize(databaseSizeBeforeCreate + 1);
        Voto_post testVoto_post = voto_posts.get(voto_posts.size() - 1);
        assertThat(testVoto_post.getPositivo()).isEqualTo(DEFAULT_POSITIVO);
        assertThat(testVoto_post.getMotivo()).isEqualTo(DEFAULT_MOTIVO);
    }

    @Test
    @Transactional
    public void getAllVoto_posts() throws Exception {
        // Initialize the database
        voto_postRepository.saveAndFlush(voto_post);

        // Get all the voto_posts
        restVoto_postMockMvc.perform(get("/api/voto_posts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(voto_post.getId().intValue())))
                .andExpect(jsonPath("$.[*].positivo").value(hasItem(DEFAULT_POSITIVO.booleanValue())))
                .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO.toString())));
    }

    @Test
    @Transactional
    public void getVoto_post() throws Exception {
        // Initialize the database
        voto_postRepository.saveAndFlush(voto_post);

        // Get the voto_post
        restVoto_postMockMvc.perform(get("/api/voto_posts/{id}", voto_post.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(voto_post.getId().intValue()))
            .andExpect(jsonPath("$.positivo").value(DEFAULT_POSITIVO.booleanValue()))
            .andExpect(jsonPath("$.motivo").value(DEFAULT_MOTIVO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVoto_post() throws Exception {
        // Get the voto_post
        restVoto_postMockMvc.perform(get("/api/voto_posts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVoto_post() throws Exception {
        // Initialize the database
        voto_postRepository.saveAndFlush(voto_post);

		int databaseSizeBeforeUpdate = voto_postRepository.findAll().size();

        // Update the voto_post
        voto_post.setPositivo(UPDATED_POSITIVO);
        voto_post.setMotivo(UPDATED_MOTIVO);

        restVoto_postMockMvc.perform(put("/api/voto_posts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(voto_post)))
                .andExpect(status().isOk());

        // Validate the Voto_post in the database
        List<Voto_post> voto_posts = voto_postRepository.findAll();
        assertThat(voto_posts).hasSize(databaseSizeBeforeUpdate);
        Voto_post testVoto_post = voto_posts.get(voto_posts.size() - 1);
        assertThat(testVoto_post.getPositivo()).isEqualTo(UPDATED_POSITIVO);
        assertThat(testVoto_post.getMotivo()).isEqualTo(UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    public void deleteVoto_post() throws Exception {
        // Initialize the database
        voto_postRepository.saveAndFlush(voto_post);

		int databaseSizeBeforeDelete = voto_postRepository.findAll().size();

        // Get the voto_post
        restVoto_postMockMvc.perform(delete("/api/voto_posts/{id}", voto_post.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Voto_post> voto_posts = voto_postRepository.findAll();
        assertThat(voto_posts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
