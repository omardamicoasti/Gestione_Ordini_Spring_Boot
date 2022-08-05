package it.betacom.ordini.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.betacom.ordini.model.Immagine;
import it.betacom.ordini.repository.ImmagineRepository;
import it.betacom.ordini.service.ImmagineService;

@Service
public class ImmagineServiceImpl implements ImmagineService {

	@Autowired
	ImmagineRepository ir;

	@Override
	public void saveImmagine(Immagine immagine) {
		ir.save(immagine);
	}

	@Override
	public Optional<Immagine> findById(long id) {
		return ir.findById(id);
	}
}
