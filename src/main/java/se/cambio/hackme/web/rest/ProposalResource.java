package se.cambio.hackme.web.rest;

import com.codahale.metrics.annotation.Timed;
import se.cambio.hackme.service.ProposalService;
import se.cambio.hackme.web.rest.util.HeaderUtil;
import se.cambio.hackme.service.dto.ProposalDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Proposal.
 */
@RestController
@RequestMapping("/api")
public class ProposalResource {

    private final Logger log = LoggerFactory.getLogger(ProposalResource.class);
        
    @Inject
    private ProposalService proposalService;

    /**
     * POST  /proposals : Create a new proposal.
     *
     * @param proposalDTO the proposalDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new proposalDTO, or with status 400 (Bad Request) if the proposal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/proposals")
    @Timed
    public ResponseEntity<ProposalDTO> createProposal(@Valid @RequestBody ProposalDTO proposalDTO) throws URISyntaxException {
        log.debug("REST request to save Proposal : {}", proposalDTO);
        if (proposalDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("proposal", "idexists", "A new proposal cannot already have an ID")).body(null);
        }
        ProposalDTO result = proposalService.save(proposalDTO);
        return ResponseEntity.created(new URI("/api/proposals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("proposal", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /proposals : Updates an existing proposal.
     *
     * @param proposalDTO the proposalDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated proposalDTO,
     * or with status 400 (Bad Request) if the proposalDTO is not valid,
     * or with status 500 (Internal Server Error) if the proposalDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/proposals")
    @Timed
    public ResponseEntity<ProposalDTO> updateProposal(@Valid @RequestBody ProposalDTO proposalDTO) throws URISyntaxException {
        log.debug("REST request to update Proposal : {}", proposalDTO);
        if (proposalDTO.getId() == null) {
            return createProposal(proposalDTO);
        }
        ProposalDTO result = proposalService.save(proposalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("proposal", proposalDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /proposals : get all the proposals.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of proposals in body
     */
    @GetMapping("/proposals")
    @Timed
    public List<ProposalDTO> getAllProposals(@RequestParam(required = false) String filter) {
        if ("team-is-null".equals(filter)) {
            log.debug("REST request to get all Proposals where team is null");
            return proposalService.findAllWhereTeamIsNull();
        }
        log.debug("REST request to get all Proposals");
        return proposalService.findAll();
    }

    /**
     * GET  /proposals/:id : get the "id" proposal.
     *
     * @param id the id of the proposalDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the proposalDTO, or with status 404 (Not Found)
     */
    @GetMapping("/proposals/{id}")
    @Timed
    public ResponseEntity<ProposalDTO> getProposal(@PathVariable Long id) {
        log.debug("REST request to get Proposal : {}", id);
        ProposalDTO proposalDTO = proposalService.findOne(id);
        return Optional.ofNullable(proposalDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /proposals/:id : delete the "id" proposal.
     *
     * @param id the id of the proposalDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/proposals/{id}")
    @Timed
    public ResponseEntity<Void> deleteProposal(@PathVariable Long id) {
        log.debug("REST request to delete Proposal : {}", id);
        proposalService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("proposal", id.toString())).build();
    }

}
