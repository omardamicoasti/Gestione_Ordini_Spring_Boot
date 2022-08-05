package it.betacom.ordini.service;

import java.util.List;
import java.util.Optional;

import it.betacom.ordini.model.Utente;

public interface UtenteService {

	void saveUtente(Utente utente);
	List<Utente> getAll();
	Optional<Utente> findByUsername(String username);
	void deleteUtente(Utente utente);
}
