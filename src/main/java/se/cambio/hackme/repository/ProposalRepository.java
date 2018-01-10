package se.cambio.hackme.repository;

import se.cambio.hackme.domain.Proposal;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Proposal entity.
 */
@SuppressWarnings("unused")
public interface ProposalRepository extends JpaRepository<Proposal,Long> {

}
