package it.betacom.ordini.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.betacom.ordini.model.Articolo;
import it.betacom.ordini.service.ArticoloService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "")
public class ArticoloRestController {

	@Autowired
	ArticoloService as;
	
	@GetMapping("/articoli")
	public List<Articolo> getArticoli() {
		return as.getAll();
	}
}
