package se.cambio.hackme.web.rest;

import se.cambio.hackme.HackMeApp;

import se.cambio.hackme.domain.Proposal;
import se.cambio.hackme.repository.ProposalRepository;
import se.cambio.hackme.service.ProposalService;
import se.cambio.hackme.service.dto.ProposalDTO;
import se.cambio.hackme.service.mapper.ProposalMapper;

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
 * Test class for the ProposalResource REST controller.
 *
 * @see ProposalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HackMeApp.class)
public class ProposalResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Inject
    private ProposalRepository proposalRepository;

    @Inject
    private ProposalMapper proposalMapper;

    @Inject
    private ProposalService proposalService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProposalMockMvc;

    private Proposal proposal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProposalResource proposalResource = new ProposalResource();
        ReflectionTestUtils.setField(proposalResource, "proposalService", proposalService);
        this.restProposalMockMvc = MockMvcBuilders.standaloneSetup(proposalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposal createEntity(EntityManager em) {
        Proposal proposal = new Proposal()
                .title(DEFAULT_TITLE)
                .filePath(DEFAULT_FILE_PATH)
                .comment(DEFAULT_COMMENT)
                .isActive(DEFAULT_IS_ACTIVE);
        return proposal;
    }

    @Before
    public void initTest() {
        proposal = createEntity(em);
    }

    @Test
    @Transactional
    public void createProposal() throws Exception {
        int databaseSizeBeforeCreate = proposalRepository.findAll().size();

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.proposalToProposalDTO(proposal);

        restProposalMockMvc.perform(post("/api/proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isCreated());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeCreate + 1);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProposal.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testProposal.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testProposal.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createProposalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proposalRepository.findAll().size();

        // Create the Proposal with an existing ID
        Proposal existingProposal = new Proposal();
        existingProposal.setId(1L);
        ProposalDTO existingProposalDTO = proposalMapper.proposalToProposalDTO(existingProposal);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProposalMockMvc.perform(post("/api/proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingProposalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalRepository.findAll().size();
        // set the field null
        proposal.setTitle(null);

        // Create the Proposal, which fails.
        ProposalDTO proposalDTO = proposalMapper.proposalToProposalDTO(proposal);

        restProposalMockMvc.perform(post("/api/proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isBadRequest());

        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProposals() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList
        restProposalMockMvc.perform(get("/api/proposals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get the proposal
        restProposalMockMvc.perform(get("/api/proposals/{id}", proposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(proposal.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProposal() throws Exception {
        // Get the proposal
        restProposalMockMvc.perform(get("/api/proposals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Update the proposal
        Proposal updatedProposal = proposalRepository.findOne(proposal.getId());
        updatedProposal
                .title(UPDATED_TITLE)
                .filePath(UPDATED_FILE_PATH)
                .comment(UPDATED_COMMENT)
                .isActive(UPDATED_IS_ACTIVE);
        ProposalDTO proposalDTO = proposalMapper.proposalToProposalDTO(updatedProposal);

        restProposalMockMvc.perform(put("/api/proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isOk());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProposal.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testProposal.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testProposal.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Create the Proposal
        ProposalDTO proposalDTO = proposalMapper.proposalToProposalDTO(proposal);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProposalMockMvc.perform(put("/api/proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposalDTO)))
            .andExpect(status().isCreated());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);
        int databaseSizeBeforeDelete = proposalRepository.findAll().size();

        // Get the proposal
        restProposalMockMvc.perform(delete("/api/proposals/{id}", proposal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
