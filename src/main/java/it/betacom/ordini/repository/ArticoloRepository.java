package it.betacom.ordini.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.betacom.ordini.model.Articolo;

@Repository
public interface ArticoloRepository extends JpaRepository<Articolo, Long> {

	@Query(value = "select * from articolo where disponibile = 1", nativeQuery = true)
	public List<Articolo> getArticoliDisponibili();

	@Query(value = "select id_articolo from ordine_articolo group by id_articolo having sum(qta) >= "
			+ "all(select sum(qta) somma from ordine_articolo group by id_articolo)", nativeQuery = true)
	Long getArticoloPiuVenduto();
	
	@Modifying
	@Transactional
	@Query(value = "update articolo set disponibile = 1 where id_articolo = ?1", nativeQuery = true)
	void disponibile(long id);
	
	@Modifying
	@Transactional
	@Query(value = "update articolo set disponibile = 0 where id_articolo = ?1", nativeQuery = true)
	void nonDisponibile(long id);
	
}
