package se.cambio.hackme.service;

import se.cambio.hackme.service.dto.RuleDTO;
import java.util.List;

/**
 * Service Interface for managing Rule.
 */
public interface RuleService {

    /**
     * Save a rule.
     *
     * @param ruleDTO the entity to save
     * @return the persisted entity
     */
    RuleDTO save(RuleDTO ruleDTO);

    /**
     *  Get all the rules.
     *  
     *  @return the list of entities
     */
    List<RuleDTO> findAll();

    /**
     *  Get the "id" rule.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RuleDTO findOne(Long id);

    /**
     *  Delete the "id" rule.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
