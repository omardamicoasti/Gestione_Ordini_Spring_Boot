package it.betacom.ordini.service;

import java.util.List;
import java.util.Optional;

import it.betacom.ordini.model.Ordine;

public interface OrdineService {

	void saveOrdine(Ordine ordine);
	List<Ordine> getAll();
	List<Ordine> findByUsername(String user);
	Optional<Ordine> findById(long id);
	void deleteOrdine(Ordine ordine);
	Optional<Ordine> ordinePiuCostoso();
}
