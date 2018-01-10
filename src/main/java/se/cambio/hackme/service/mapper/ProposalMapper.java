package se.cambio.hackme.service.mapper;

import se.cambio.hackme.domain.*;
import se.cambio.hackme.service.dto.ProposalDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Proposal and its DTO ProposalDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProposalMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProposalDTO proposalToProposalDTO(Proposal proposal);

    List<ProposalDTO> proposalsToProposalDTOs(List<Proposal> proposals);

    @Mapping(target = "team", ignore = true)
    @Mapping(source = "categoryId", target = "category")
    Proposal proposalDTOToProposal(ProposalDTO proposalDTO);

    List<Proposal> proposalDTOsToProposals(List<ProposalDTO> proposalDTOs);

    default Category categoryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Category category = new Category();
        category.setId(id);
        return category;
    }
}
