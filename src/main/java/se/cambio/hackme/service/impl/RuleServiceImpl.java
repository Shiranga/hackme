package se.cambio.hackme.service.impl;

import se.cambio.hackme.service.RuleService;
import se.cambio.hackme.domain.Rule;
import se.cambio.hackme.repository.RuleRepository;
import se.cambio.hackme.service.dto.RuleDTO;
import se.cambio.hackme.service.mapper.RuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Rule.
 */
@Service
@Transactional
public class RuleServiceImpl implements RuleService{

    private final Logger log = LoggerFactory.getLogger(RuleServiceImpl.class);
    
    @Inject
    private RuleRepository ruleRepository;

    @Inject
    private RuleMapper ruleMapper;

    /**
     * Save a rule.
     *
     * @param ruleDTO the entity to save
     * @return the persisted entity
     */
    public RuleDTO save(RuleDTO ruleDTO) {
        log.debug("Request to save Rule : {}", ruleDTO);
        Rule rule = ruleMapper.ruleDTOToRule(ruleDTO);
        rule = ruleRepository.save(rule);
        RuleDTO result = ruleMapper.ruleToRuleDTO(rule);
        return result;
    }

    /**
     *  Get all the rules.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<RuleDTO> findAll() {
        log.debug("Request to get all Rules");
        List<RuleDTO> result = ruleRepository.findAll().stream()
            .map(ruleMapper::ruleToRuleDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one rule by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public RuleDTO findOne(Long id) {
        log.debug("Request to get Rule : {}", id);
        Rule rule = ruleRepository.findOne(id);
        RuleDTO ruleDTO = ruleMapper.ruleToRuleDTO(rule);
        return ruleDTO;
    }

    /**
     *  Delete the  rule by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Rule : {}", id);
        ruleRepository.delete(id);
    }
}
