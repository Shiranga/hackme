package se.cambio.hackme.repository;

import se.cambio.hackme.domain.Prize;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Prize entity.
 */
@SuppressWarnings("unused")
public interface PrizeRepository extends JpaRepository<Prize,Long> {

}
