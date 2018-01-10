package se.cambio.hackme.service;

import se.cambio.hackme.service.dto.PrizeDTO;
import java.util.List;

/**
 * Service Interface for managing Prize.
 */
public interface PrizeService {

    /**
     * Save a prize.
     *
     * @param prizeDTO the entity to save
     * @return the persisted entity
     */
    PrizeDTO save(PrizeDTO prizeDTO);

    /**
     *  Get all the prizes.
     *  
     *  @return the list of entities
     */
    List<PrizeDTO> findAll();

    /**
     *  Get the "id" prize.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PrizeDTO findOne(Long id);

    /**
     *  Delete the "id" prize.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
