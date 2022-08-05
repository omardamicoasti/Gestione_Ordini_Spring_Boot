package it.betacom.ordini.service;

import java.util.Optional;

import it.betacom.ordini.model.Immagine;

public interface ImmagineService {

	void saveImmagine(Immagine immagine);
	Optional<Immagine> findById(long id);
}
