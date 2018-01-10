package se.cambio.hackme.service.impl;

import se.cambio.hackme.service.PrizeService;
import se.cambio.hackme.domain.Prize;
import se.cambio.hackme.repository.PrizeRepository;
import se.cambio.hackme.service.dto.PrizeDTO;
import se.cambio.hackme.service.mapper.PrizeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Prize.
 */
@Service
@Transactional
public class PrizeServiceImpl implements PrizeService{

    private final Logger log = LoggerFactory.getLogger(PrizeServiceImpl.class);
    
    @Inject
    private PrizeRepository prizeRepository;

    @Inject
    private PrizeMapper prizeMapper;

    /**
     * Save a prize.
     *
     * @param prizeDTO the entity to save
     * @return the persisted entity
     */
    public PrizeDTO save(PrizeDTO prizeDTO) {
        log.debug("Request to save Prize : {}", prizeDTO);
        Prize prize = prizeMapper.prizeDTOToPrize(prizeDTO);
        prize = prizeRepository.save(prize);
        PrizeDTO result = prizeMapper.prizeToPrizeDTO(prize);
        return result;
    }

    /**
     *  Get all the prizes.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<PrizeDTO> findAll() {
        log.debug("Request to get all Prizes");
        List<PrizeDTO> result = prizeRepository.findAll().stream()
            .map(prizeMapper::prizeToPrizeDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one prize by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PrizeDTO findOne(Long id) {
        log.debug("Request to get Prize : {}", id);
        Prize prize = prizeRepository.findOne(id);
        PrizeDTO prizeDTO = prizeMapper.prizeToPrizeDTO(prize);
        return prizeDTO;
    }

    /**
     *  Delete the  prize by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Prize : {}", id);
        prizeRepository.delete(id);
    }
}
