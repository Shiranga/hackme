package se.cambio.hackme.web.rest;

import se.cambio.hackme.HackMeApp;

import se.cambio.hackme.domain.Prize;
import se.cambio.hackme.repository.PrizeRepository;
import se.cambio.hackme.service.PrizeService;
import se.cambio.hackme.service.dto.PrizeDTO;
import se.cambio.hackme.service.mapper.PrizeMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PrizeResource REST controller.
 *
 * @see PrizeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackMeApp.class)
public class PrizeResourceIntTest {

    private static final String DEFAULT_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_PRIZE = "AAAAAAAAAA";
    private static final String UPDATED_PRIZE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Inject
    private PrizeRepository prizeRepository;

    @Inject
    private PrizeMapper prizeMapper;

    @Inject
    private PrizeService prizeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPrizeMockMvc;

    private Prize prize;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrizeResource prizeResource = new PrizeResource();
        ReflectionTestUtils.setField(prizeResource, "prizeService", prizeService);
        this.restPrizeMockMvc = MockMvcBuilders.standaloneSetup(prizeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prize createEntity(EntityManager em) {
        Prize prize = new Prize()
                .place(DEFAULT_PLACE)
                .prize(DEFAULT_PRIZE)
                .isActive(DEFAULT_IS_ACTIVE);
        return prize;
    }

    @Before
    public void initTest() {
        prize = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrize() throws Exception {
        int databaseSizeBeforeCreate = prizeRepository.findAll().size();

        // Create the Prize
        PrizeDTO prizeDTO = prizeMapper.prizeToPrizeDTO(prize);

        restPrizeMockMvc.perform(post("/api/prizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prizeDTO)))
            .andExpect(status().isCreated());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeCreate + 1);
        Prize testPrize = prizeList.get(prizeList.size() - 1);
        assertThat(testPrize.getPlace()).isEqualTo(DEFAULT_PLACE);
        assertThat(testPrize.getPrize()).isEqualTo(DEFAULT_PRIZE);
        assertThat(testPrize.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createPrizeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = prizeRepository.findAll().size();

        // Create the Prize with an existing ID
        Prize existingPrize = new Prize();
        existingPrize.setId(1L);
        PrizeDTO existingPrizeDTO = prizeMapper.prizeToPrizeDTO(existingPrize);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrizeMockMvc.perform(post("/api/prizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPrizeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPrizes() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get all the prizeList
        restPrizeMockMvc.perform(get("/api/prizes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prize.getId().intValue())))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE.toString())))
            .andExpect(jsonPath("$.[*].prize").value(hasItem(DEFAULT_PRIZE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getPrize() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);

        // Get the prize
        restPrizeMockMvc.perform(get("/api/prizes/{id}", prize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(prize.getId().intValue()))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE.toString()))
            .andExpect(jsonPath("$.prize").value(DEFAULT_PRIZE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPrize() throws Exception {
        // Get the prize
        restPrizeMockMvc.perform(get("/api/prizes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrize() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);
        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();

        // Update the prize
        Prize updatedPrize = prizeRepository.findOne(prize.getId());
        updatedPrize
                .place(UPDATED_PLACE)
                .prize(UPDATED_PRIZE)
                .isActive(UPDATED_IS_ACTIVE);
        PrizeDTO prizeDTO = prizeMapper.prizeToPrizeDTO(updatedPrize);

        restPrizeMockMvc.perform(put("/api/prizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prizeDTO)))
            .andExpect(status().isOk());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate);
        Prize testPrize = prizeList.get(prizeList.size() - 1);
        assertThat(testPrize.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testPrize.getPrize()).isEqualTo(UPDATED_PRIZE);
        assertThat(testPrize.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingPrize() throws Exception {
        int databaseSizeBeforeUpdate = prizeRepository.findAll().size();

        // Create the Prize
        PrizeDTO prizeDTO = prizeMapper.prizeToPrizeDTO(prize);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPrizeMockMvc.perform(put("/api/prizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prizeDTO)))
            .andExpect(status().isCreated());

        // Validate the Prize in the database
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePrize() throws Exception {
        // Initialize the database
        prizeRepository.saveAndFlush(prize);
        int databaseSizeBeforeDelete = prizeRepository.findAll().size();

        // Get the prize
        restPrizeMockMvc.perform(delete("/api/prizes/{id}", prize.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Prize> prizeList = prizeRepository.findAll();
        assertThat(prizeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
