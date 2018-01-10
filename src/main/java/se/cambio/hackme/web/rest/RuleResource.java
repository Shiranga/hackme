package se.cambio.hackme.web.rest;

import com.codahale.metrics.annotation.Timed;
import se.cambio.hackme.service.RuleService;
import se.cambio.hackme.web.rest.util.HeaderUtil;
import se.cambio.hackme.service.dto.RuleDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Rule.
 */
@RestController
@RequestMapping("/api")
public class RuleResource {

    private final Logger log = LoggerFactory.getLogger(RuleResource.class);
        
    @Inject
    private RuleService ruleService;

    /**
     * POST  /rules : Create a new rule.
     *
     * @param ruleDTO the ruleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ruleDTO, or with status 400 (Bad Request) if the rule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rules")
    @Timed
    public ResponseEntity<RuleDTO> createRule(@RequestBody RuleDTO ruleDTO) throws URISyntaxException {
        log.debug("REST request to save Rule : {}", ruleDTO);
        if (ruleDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("rule", "idexists", "A new rule cannot already have an ID")).body(null);
        }
        RuleDTO result = ruleService.save(ruleDTO);
        return ResponseEntity.created(new URI("/api/rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("rule", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rules : Updates an existing rule.
     *
     * @param ruleDTO the ruleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ruleDTO,
     * or with status 400 (Bad Request) if the ruleDTO is not valid,
     * or with status 500 (Internal Server Error) if the ruleDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rules")
    @Timed
    public ResponseEntity<RuleDTO> updateRule(@RequestBody RuleDTO ruleDTO) throws URISyntaxException {
        log.debug("REST request to update Rule : {}", ruleDTO);
        if (ruleDTO.getId() == null) {
            return createRule(ruleDTO);
        }
        RuleDTO result = ruleService.save(ruleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("rule", ruleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rules : get all the rules.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rules in body
     */
    @GetMapping("/rules")
    @Timed
    public List<RuleDTO> getAllRules() {
        log.debug("REST request to get all Rules");
        return ruleService.findAll();
    }

    /**
     * GET  /rules/:id : get the "id" rule.
     *
     * @param id the id of the ruleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ruleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rules/{id}")
    @Timed
    public ResponseEntity<RuleDTO> getRule(@PathVariable Long id) {
        log.debug("REST request to get Rule : {}", id);
        RuleDTO ruleDTO = ruleService.findOne(id);
        return Optional.ofNullable(ruleDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rules/:id : delete the "id" rule.
     *
     * @param id the id of the ruleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rules/{id}")
    @Timed
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        log.debug("REST request to delete Rule : {}", id);
        ruleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("rule", id.toString())).build();
    }

}
