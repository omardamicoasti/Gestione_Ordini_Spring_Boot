package it.betacom.ordini.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.betacom.ordini.model.OrdineArticolo;

@Repository
public interface OrdineArticoloRepository extends JpaRepository<OrdineArticolo, Long> {

	@Query(value = "select marca, modello, prezzo, qta from articolo, ordine_articolo "
			+ "where articolo.id_articolo = ordine_articolo.id_articolo and id_ordine = ?1", nativeQuery = true)
	List<String[]> getOrdineArticoli(long id);
}
