package se.cambio.hackme.service.impl;

import se.cambio.hackme.service.TeamService;
import se.cambio.hackme.domain.Team;
import se.cambio.hackme.repository.TeamRepository;
import se.cambio.hackme.service.dto.TeamDTO;
import se.cambio.hackme.service.mapper.TeamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Team.
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService{

    private final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);
    
    @Inject
    private TeamRepository teamRepository;

    @Inject
    private TeamMapper teamMapper;

    /**
     * Save a team.
     *
     * @param teamDTO the entity to save
     * @return the persisted entity
     */
    public TeamDTO save(TeamDTO teamDTO) {
        log.debug("Request to save Team : {}", teamDTO);
        Team team = teamMapper.teamDTOToTeam(teamDTO);
        team = teamRepository.save(team);
        TeamDTO result = teamMapper.teamToTeamDTO(team);
        return result;
    }

    /**
     *  Get all the teams.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<TeamDTO> findAll() {
        log.debug("Request to get all Teams");
        List<TeamDTO> result = teamRepository.findAll().stream()
            .map(teamMapper::teamToTeamDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one team by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TeamDTO findOne(Long id) {
        log.debug("Request to get Team : {}", id);
        Team team = teamRepository.findOne(id);
        TeamDTO teamDTO = teamMapper.teamToTeamDTO(team);
        return teamDTO;
    }

    /**
     *  Delete the  team by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Team : {}", id);
        teamRepository.delete(id);
    }
}
