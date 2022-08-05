package it.betacom.ordini.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.betacom.ordini.model.Ordine;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {

	@Query(value = "select * from ordine where username = ?1", nativeQuery = true)
	List<Ordine> findByUsername(String username);
	
	@Query(value = "select * from ordine where totale >= all (select totale from ordine)", nativeQuery = true)
	Optional<Ordine> ordinePiuCostoso();
}
