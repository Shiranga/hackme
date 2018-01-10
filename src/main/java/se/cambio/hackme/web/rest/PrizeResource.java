package se.cambio.hackme.web.rest;

import com.codahale.metrics.annotation.Timed;
import se.cambio.hackme.service.PrizeService;
import se.cambio.hackme.web.rest.util.HeaderUtil;
import se.cambio.hackme.service.dto.PrizeDTO;

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
 * REST controller for managing Prize.
 */
@RestController
@RequestMapping("/api")
public class PrizeResource {

    private final Logger log = LoggerFactory.getLogger(PrizeResource.class);
        
    @Inject
    private PrizeService prizeService;

    /**
     * POST  /prizes : Create a new prize.
     *
     * @param prizeDTO the prizeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new prizeDTO, or with status 400 (Bad Request) if the prize has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prizes")
    @Timed
    public ResponseEntity<PrizeDTO> createPrize(@RequestBody PrizeDTO prizeDTO) throws URISyntaxException {
        log.debug("REST request to save Prize : {}", prizeDTO);
        if (prizeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("prize", "idexists", "A new prize cannot already have an ID")).body(null);
        }
        PrizeDTO result = prizeService.save(prizeDTO);
        return ResponseEntity.created(new URI("/api/prizes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("prize", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prizes : Updates an existing prize.
     *
     * @param prizeDTO the prizeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated prizeDTO,
     * or with status 400 (Bad Request) if the prizeDTO is not valid,
     * or with status 500 (Internal Server Error) if the prizeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prizes")
    @Timed
    public ResponseEntity<PrizeDTO> updatePrize(@RequestBody PrizeDTO prizeDTO) throws URISyntaxException {
        log.debug("REST request to update Prize : {}", prizeDTO);
        if (prizeDTO.getId() == null) {
            return createPrize(prizeDTO);
        }
        PrizeDTO result = prizeService.save(prizeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("prize", prizeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prizes : get all the prizes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of prizes in body
     */
    @GetMapping("/prizes")
    @Timed
    public List<PrizeDTO> getAllPrizes() {
        log.debug("REST request to get all Prizes");
        return prizeService.findAll();
    }

    /**
     * GET  /prizes/:id : get the "id" prize.
     *
     * @param id the id of the prizeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prizeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/prizes/{id}")
    @Timed
    public ResponseEntity<PrizeDTO> getPrize(@PathVariable Long id) {
        log.debug("REST request to get Prize : {}", id);
        PrizeDTO prizeDTO = prizeService.findOne(id);
        return Optional.ofNullable(prizeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prizes/:id : delete the "id" prize.
     *
     * @param id the id of the prizeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prizes/{id}")
    @Timed
    public ResponseEntity<Void> deletePrize(@PathVariable Long id) {
        log.debug("REST request to delete Prize : {}", id);
        prizeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("prize", id.toString())).build();
    }

}
