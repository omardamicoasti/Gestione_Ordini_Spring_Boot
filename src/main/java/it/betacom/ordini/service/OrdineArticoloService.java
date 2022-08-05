package it.betacom.ordini.service;

import java.util.List;

import it.betacom.ordini.model.OrdineArticolo;

public interface OrdineArticoloService {

	void saveOrdineArticolo(OrdineArticolo oa);
	List<OrdineArticolo> getAll();
	List<String[]> getOrdineArticoli(long id);
}
