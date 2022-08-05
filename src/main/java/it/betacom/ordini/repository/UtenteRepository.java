package it.betacom.ordini.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.betacom.ordini.model.Utente;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {

	@Query(value = "select * from utente where username = ?1", nativeQuery = true)
	Optional<Utente> findByUsername(String username);
}
