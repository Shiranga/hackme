package se.cambio.hackme.service.mapper;

import se.cambio.hackme.domain.*;
import se.cambio.hackme.service.dto.TeamDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Team and its DTO TeamDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TeamMapper {

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    @Mapping(source = "proposal.id", target = "proposalId")
    @Mapping(source = "proposal.title", target = "proposalTitle")
    TeamDTO teamToTeamDTO(Team team);

    List<TeamDTO> teamsToTeamDTOs(List<Team> teams);

    @Mapping(source = "eventId", target = "event")
    @Mapping(source = "proposalId", target = "proposal")
    Team teamDTOToTeam(TeamDTO teamDTO);

    List<Team> teamDTOsToTeams(List<TeamDTO> teamDTOs);

    default Event eventFromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }

    default Proposal proposalFromId(Long id) {
        if (id == null) {
            return null;
        }
        Proposal proposal = new Proposal();
        proposal.setId(id);
        return proposal;
    }
}
