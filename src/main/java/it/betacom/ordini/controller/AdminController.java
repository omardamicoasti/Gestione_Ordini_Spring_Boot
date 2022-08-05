package it.betacom.ordini.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import it.betacom.ordini.model.Articolo;
import it.betacom.ordini.model.Immagine;
import it.betacom.ordini.model.Ordine;
import it.betacom.ordini.model.Utente;
import it.betacom.ordini.security.BCryptEncoder;
import it.betacom.ordini.service.ArticoloService;
import it.betacom.ordini.service.ImmagineService;
import it.betacom.ordini.service.OrdineArticoloService;
import it.betacom.ordini.service.OrdineService;
import it.betacom.ordini.service.UtenteService;

@Controller
@Scope("session")
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	OrdineService ordineService;
	@Autowired
	OrdineArticoloService ordineArticoloService;
	@Autowired
	ArticoloService articoloService;
	@Autowired
	UtenteService utenteService;
	@Autowired
	ImmagineService immagineService;
		
	@RequestMapping(value= {"","/adminhome"}, method = RequestMethod.GET)
	public ModelAndView homePage() {
		ModelAndView mv = new ModelAndView();
		Long id = articoloService.getArticoloPiuVenduto();
		Articolo a = id != null ?articoloService.findById(id).get() : new Articolo();
		Ordine o = ordineService.ordinePiuCostoso() != null ? ordineService.ordinePiuCostoso().get() : new Ordine();
		mv.addObject("aPiuVenduto", a);
		mv.addObject("oPiuCostoso", o);
		mv.setViewName("adminHome");
		return mv;
	}
	
	/******************ORDINE***************/
	
	@GetMapping("/gestioneOrdini")
	public ModelAndView gestioneOrdini() {
		List<Ordine> listaOrdini = ordineService.getAll();
		ModelAndView mv = new ModelAndView();
		mv.addObject("listaOrdini", listaOrdini);
		mv.setViewName("gestioneOrdini");
		return mv;
	}
	
	@GetMapping("/eliminaOrdine/{id}")
	public ModelAndView eliminaOrdine(@PathVariable long id) {
		Ordine o = ordineService.findById(id).get();
		ordineService.deleteOrdine(o);
		return new ModelAndView("redirect:/admin/gestioneOrdini");
	}
	
	@GetMapping("/visualizzaOrdine/{id}")
	public ModelAndView visualizzaOrdine(@PathVariable long id) {
		Ordine o = ordineService.findById(id).get();
		ModelAndView mv = new ModelAndView();
		mv.addObject("idOrdine", o.getIdOrdine());
		mv.addObject("totale", o.getTotale());
		mv.addObject("utente", o.getUtente());
		mv.addObject("ordineList", ordineArticoloService.getOrdineArticoli(o.getIdOrdine()));
		mv.setViewName("visualizzaOrdine");
		return mv;
	}
	
	/******************NUOVO ADMIN***************/
	
	@GetMapping("/nuovoAdmin")
	public ModelAndView nuovoAdmin() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("registraAdmin");
		Utente u = new Utente();
		u.setRole("ADMIN");
		mv.addObject("utente",u);
		return mv;
	}
	
	@PostMapping("/registrazioneAdmin")
	public ModelAndView registraAdmin(Utente utente, BindingResult br) {
		ModelAndView mv = new ModelAndView();
		if(utenteService.findByUsername(utente.getUsername()).isPresent()) {
			mv.addObject("checkUser", "Utente admin già registrato");
			mv.setViewName("registraAdmin");
			return mv;
		} else {	
			System.out.println(utente.getPassword());
			utente.setPassword(BCryptEncoder.encode(utente.getPassword()));
			System.out.println("**********REGISTRAZIONE ADMIN********** " + utente.getPassword());
			utenteService.saveUtente(utente);
			return new ModelAndView("redirect:/admin/");
		}
	}
	
	/******************ARTICOLI***************/
	
	@GetMapping("/gestioneArticoli")
	public ModelAndView gestioneArticoli() {
		List<Articolo> listaA = articoloService.getAll();
		List<String[]> lista = new ArrayList<String[]>();
		for(Articolo a : listaA) {
			String path = "";
			if(immagineService.findById(a.getIdArticolo()).isPresent()) {
				path = immagineService.findById(a.getIdArticolo()).get().getPath();
			}
			String [] p = { String.valueOf(a.getIdArticolo()), a.getModello(), a.getMarca(), String.valueOf(a.getPrezzo()), String.valueOf(a.isDisponibile()), path };
			lista.add(p);
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("articolo", new Articolo());
		mv.addObject("lista", lista);
		mv.setViewName("gestioneArticoli");
		return mv;
	}
	
	@GetMapping("/eliminaArticolo/{id}")
	 public ModelAndView eliminaArticolo(@PathVariable long id) {
	  Articolo a = articoloService.findById(id).get();
	  articoloService.deleteArticolo(a);
	  return new ModelAndView("redirect:/admin/gestioneArticoli");
	}
	
	@GetMapping("/dettagliArticolo/{id}")
	public ModelAndView dettagliArticolo(@PathVariable long id) {
		Articolo a = articoloService.findById(id).get();
		Immagine i = immagineService.findById(id).isPresent()? immagineService.findById(id).get() : new Immagine();
		ModelAndView mv = new ModelAndView();
		mv.addObject("articolo",a);
		mv.addObject("immagine",i);
		mv.setViewName("articoloAdmin");
		return mv;
	}
	
	@PostMapping("/salvaArticolo")
	public ModelAndView salvaArticolo(Articolo articolo, BindingResult br) {
		articoloService.saveArticolo(articolo);	
		return new ModelAndView("redirect:/admin/gestioneArticoli");
	}
	
	@PostMapping("/aggiungiArticolo")
	public ModelAndView aggiungiArticolo(Articolo articolo, BindingResult br) {
		articoloService.saveArticolo(articolo);
		return new ModelAndView("redirect:/admin/gestioneArticoli");
	}
	
	@PostMapping("/aggiungiImmagine")
	public ModelAndView aggiungiImmagine(Immagine immagine, BindingResult br, @RequestParam("idArticolo") long idArticolo, @RequestParam("path") String path) {
		immagine.setPath("../img/"+path);
		if(!immagineService.findById(idArticolo).isPresent()) {
			immagine.setArticolo(articoloService.findById(idArticolo).get());
		}
		immagineService.saveImmagine(immagine);
		return new ModelAndView("redirect:/admin/gestioneArticoli");
	}
	
}
