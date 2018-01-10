package se.cambio.hackme.service.mapper;

import se.cambio.hackme.domain.*;
import se.cambio.hackme.service.dto.PrizeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Prize and its DTO PrizeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PrizeMapper {

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    PrizeDTO prizeToPrizeDTO(Prize prize);

    List<PrizeDTO> prizesToPrizeDTOs(List<Prize> prizes);

    @Mapping(source = "eventId", target = "event")
    Prize prizeDTOToPrize(PrizeDTO prizeDTO);

    List<Prize> prizeDTOsToPrizes(List<PrizeDTO> prizeDTOs);

    default Event eventFromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
