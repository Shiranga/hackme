package se.cambio.hackme.service.impl;

import se.cambio.hackme.service.ProposalService;
import se.cambio.hackme.domain.Proposal;
import se.cambio.hackme.repository.ProposalRepository;
import se.cambio.hackme.service.dto.ProposalDTO;
import se.cambio.hackme.service.mapper.ProposalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Proposal.
 */
@Service
@Transactional
public class ProposalServiceImpl implements ProposalService{

    private final Logger log = LoggerFactory.getLogger(ProposalServiceImpl.class);
    
    @Inject
    private ProposalRepository proposalRepository;

    @Inject
    private ProposalMapper proposalMapper;

    /**
     * Save a proposal.
     *
     * @param proposalDTO the entity to save
     * @return the persisted entity
     */
    public ProposalDTO save(ProposalDTO proposalDTO) {
        log.debug("Request to save Proposal : {}", proposalDTO);
        Proposal proposal = proposalMapper.proposalDTOToProposal(proposalDTO);
        proposal = proposalRepository.save(proposal);
        ProposalDTO result = proposalMapper.proposalToProposalDTO(proposal);
        return result;
    }

    /**
     *  Get all the proposals.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ProposalDTO> findAll() {
        log.debug("Request to get all Proposals");
        List<ProposalDTO> result = proposalRepository.findAll().stream()
            .map(proposalMapper::proposalToProposalDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }


    /**
     *  get all the proposals where Team is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ProposalDTO> findAllWhereTeamIsNull() {
        log.debug("Request to get all proposals where Team is null");
        return StreamSupport
            .stream(proposalRepository.findAll().spliterator(), false)
            .filter(proposal -> proposal.getTeam() == null)
            .map(proposalMapper::proposalToProposalDTO)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one proposal by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ProposalDTO findOne(Long id) {
        log.debug("Request to get Proposal : {}", id);
        Proposal proposal = proposalRepository.findOne(id);
        ProposalDTO proposalDTO = proposalMapper.proposalToProposalDTO(proposal);
        return proposalDTO;
    }

    /**
     *  Delete the  proposal by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Proposal : {}", id);
        proposalRepository.delete(id);
    }
}
