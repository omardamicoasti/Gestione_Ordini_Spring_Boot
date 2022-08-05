package it.betacom.ordini.service;

import java.util.List;
import java.util.Optional;

import it.betacom.ordini.model.Articolo;

public interface ArticoloService {

	void saveArticolo(Articolo articolo);
	List<Articolo> getAll();
	Optional<Articolo> findById(long id);
	List<Articolo> getArticoliDisponibili();
	void disponibile(long id);
	void nonDisponibile(long id);
	void deleteArticolo(Articolo articolo);
	Long getArticoloPiuVenduto();
}
