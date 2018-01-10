package se.cambio.hackme.service;

import se.cambio.hackme.service.dto.ProposalDTO;
import java.util.List;

/**
 * Service Interface for managing Proposal.
 */
public interface ProposalService {

    /**
     * Save a proposal.
     *
     * @param proposalDTO the entity to save
     * @return the persisted entity
     */
    ProposalDTO save(ProposalDTO proposalDTO);

    /**
     *  Get all the proposals.
     *  
     *  @return the list of entities
     */
    List<ProposalDTO> findAll();
    /**
     *  Get all the ProposalDTO where Team is null.
     *
     *  @return the list of entities
     */
    List<ProposalDTO> findAllWhereTeamIsNull();

    /**
     *  Get the "id" proposal.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProposalDTO findOne(Long id);

    /**
     *  Delete the "id" proposal.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
