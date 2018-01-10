package se.cambio.hackme.service.mapper;

import se.cambio.hackme.domain.*;
import se.cambio.hackme.service.dto.RuleDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Rule and its DTO RuleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RuleMapper {

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    RuleDTO ruleToRuleDTO(Rule rule);

    List<RuleDTO> rulesToRuleDTOs(List<Rule> rules);

    @Mapping(source = "eventId", target = "event")
    Rule ruleDTOToRule(RuleDTO ruleDTO);

    List<Rule> ruleDTOsToRules(List<RuleDTO> ruleDTOs);

    default Event eventFromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
