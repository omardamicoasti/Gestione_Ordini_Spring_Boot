package it.betacom.ordini.controller;

import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import it.betacom.ordini.businesscomponent.Carrello;
import it.betacom.ordini.model.Articolo;
import it.betacom.ordini.model.Immagine;
import it.betacom.ordini.model.Ordine;
import it.betacom.ordini.model.OrdineArticolo;
import it.betacom.ordini.model.Utente;
import it.betacom.ordini.security.BCryptEncoder;
import it.betacom.ordini.service.ArticoloService;
import it.betacom.ordini.service.ImmagineService;
import it.betacom.ordini.service.OrdineArticoloService;
import it.betacom.ordini.service.OrdineService;
import it.betacom.ordini.service.UtenteService;

@Controller
@Scope("session")
public class ClientController {

	@Autowired
	UtenteService utenteService;

	@Autowired
	ArticoloService articoloService;

	@Autowired
	OrdineService ordineService;

	@Autowired
	OrdineArticoloService ordineArticoloService;

	@Autowired
	ImmagineService immagineService;

	@RequestMapping(value = "/loginAdmin", method = RequestMethod.GET)
	public ModelAndView loginAdmin() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("loginAdmin");
		return mv;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(HttpSession session) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");

		mv.addObject("_carrello", (Carrello) session.getAttribute("carrello"));
		mv.addObject("utente_log", (Utente) session.getAttribute("utente"));

		return mv;
	}

	/****************** REGISTRAZIONE UTENTE ***************/

	@RequestMapping(value = "/registrazione", method = RequestMethod.GET)
	public ModelAndView registrazioneUtente(HttpSession session) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("registrazione");

		mv.addObject("utente", new Utente());
		mv.addObject("_carrello", (Carrello) session.getAttribute("carrello"));
		mv.addObject("utente_log", (Utente) session.getAttribute("utente"));

		return mv;
	}

	@RequestMapping(value = "/registrazione", method = RequestMethod.POST)
	public ModelAndView registrazioneUtente(Utente utente, BindingResult br) {

		ModelAndView mv = new ModelAndView();

		if (utenteService.findByUsername(utente.getUsername()).isPresent()) {

			mv.addObject("checkUser", "Utente già registrato");
			mv.setViewName("registrazione");

			return mv;
		} else {

			System.out.println(utente.getPassword());
			utente.setPassword(BCryptEncoder.encode(utente.getPassword()));

			System.out.println("**********REGISTRAZIONE********** " + utente.getPassword());
			utenteService.saveUtente(utente);

			return new ModelAndView("redirect:/login");
		}
	}

	/****************** LOGIN UTENTE ***************/

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(HttpSession session) {
		if (session.getAttribute("utente_log") != null) {

			return new ModelAndView("redirect:/logout");
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");
		mv.addObject("_carrello", (Carrello) session.getAttribute("carrello"));
		mv.addObject("utente", new Utente());
		return mv;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView controlloLogin(@RequestParam("username") String user, @RequestParam("password") String pass,
			HttpSession session) {

		if (utenteService.findByUsername(user).isPresent()) {

			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if (encoder.matches(pass, utenteService.findByUsername(user).get().getPassword())) {

				Carrello carrello = new Carrello();
				session.setAttribute("carrello", carrello);
				session.setAttribute("utente_log", utenteService.findByUsername(user).get());

				return new ModelAndView("redirect:/acquisti");

			} else {
				return new ModelAndView("redirect:/");
			}
			
		} else {
			return new ModelAndView("redirect:/");
		}
	}

	/****************** MODIFICA UTENTE ***************/

	@RequestMapping(value = "/modutente", method = RequestMethod.GET)
	public ModelAndView modificaUtente(HttpSession session) {
		if (session.getAttribute("utente_log") == null) {
			return new ModelAndView("redirect:/");
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("modutente");
		Utente utente = (Utente) session.getAttribute("utente_log");

		mv.addObject("utente", utente);
		mv.addObject("_carrello", (Carrello) session.getAttribute("carrello"));
		mv.addObject("utente_log", (Utente) session.getAttribute("utente"));

		return mv;
	}

	@RequestMapping(value = "/modutente", method = RequestMethod.POST)
	public ModelAndView modificaUtente(Utente utente, BindingResult br, HttpSession session) {

		System.out.println(utente.getPassword());
		utente.setPassword(BCryptEncoder.encode(utente.getPassword()));

		System.out.println("**********MODIFICA********** " + utente.getPassword());
		utenteService.saveUtente(utente);
		session.invalidate();

		return new ModelAndView("redirect:/login");

	}

	/****************** LOGOUT UTENTE ***************/

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logoutPage(HttpSession session) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("logout");

		session.setAttribute("utente_log", session.getAttribute("utente_log"));

		return mv;
	}

	@RequestMapping(value = "/logoututente", method = RequestMethod.GET)
	public ModelAndView logoutUtente(HttpSession session) {

		session.invalidate();

		return new ModelAndView("redirect:/");
	}

	/****************** DELETE UTENTE ***************/

	@RequestMapping(value = "/eliminaUtente")
	public ModelAndView deleteUtente(HttpSession session) {
		if (session.getAttribute("utente_log") == null) {
			return new ModelAndView("redirect:/");
		}
		Utente utente = (Utente) session.getAttribute("utente_log");
		utenteService.deleteUtente(utente);
		session.invalidate();

		return new ModelAndView("redirect:/");
	}

	/****************** ACQUISTI **************/

	@RequestMapping(value = "/acquisti", method = RequestMethod.GET)
	public ModelAndView acquisti(HttpSession session) {

		if (session.getAttribute("utente_log") == null) {

			return new ModelAndView("redirect:/login");

		} else {

			ModelAndView mv = new ModelAndView();
			mv.setViewName("acquisti");

			List<Articolo> listaArticoli = articoloService.getArticoliDisponibili();

			mv.addObject("listaArticoli", listaArticoli);
			mv.addObject("_carrello", (Carrello) session.getAttribute("carrello"));
			mv.addObject("utente_log", session.getAttribute("utente_log"));

			return mv;
		}
	}

	@RequestMapping(value = "/add/{id}", method = RequestMethod.GET)
	public ModelAndView aggiungi(HttpSession session, @PathVariable long id) {

		Articolo a = articoloService.findById(id).get();
		Carrello carrello = (Carrello) session.getAttribute("carrello");

		if (a.getIdArticolo() != 0 && a.isDisponibile()) {

			carrello.aggiungiArticolo(String.valueOf(id), a.getMarca(), a.getModello(), a.getPrezzo());

			return new ModelAndView("redirect:/acquisti");

		} else {
			return new ModelAndView("redirect:/");
		}
	}

	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public ModelAndView aggiungi(HttpSession session, @PathVariable String id) {

		Carrello carrello = (Carrello) session.getAttribute("carrello");

		carrello.rimuoviArticolo(id);

		return new ModelAndView("redirect:/carrello");
	}

	private List<String[]> converti(HttpSession session) {

		Carrello carrello = (Carrello) session.getAttribute("carrello");

		Vector<String[]> articoli = new Vector<String[]>();

		Enumeration<String[]> prodotti = carrello.listaProdotti();

		while (prodotti.hasMoreElements()) {

			String[] prodotto = prodotti.nextElement();
			articoli.add(prodotto);
		}

		return articoli;
	}

	@RequestMapping(value = "/carrello", method = RequestMethod.GET)
	public ModelAndView carrello(HttpSession session) {

		try {

			List<String[]> articoli = converti(session);
			ModelAndView mv = new ModelAndView();
			Carrello carrello = (Carrello) session.getAttribute("carrello");

			if (carrello.totaleArticoli() == 0) {

				return new ModelAndView("redirect:/acquisti");
			}

			mv.setViewName("carrello");

			mv.addObject("listaArticoli", articoli);
			mv.addObject("_carrello", (Carrello) session.getAttribute("carrello"));
			mv.addObject("utente_log", session.getAttribute("utente_log"));

			return mv;

		} catch (NullPointerException exc) {

			return new ModelAndView("redirect:/login");
		}
	}

	/****************** CHECKOUT ***************/

	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public ModelAndView salvaOrdine(HttpSession session) {

		Carrello carrello = (Carrello) session.getAttribute("carrello");
		Utente utente = (Utente) session.getAttribute("utente_log");
		Ordine ordine = new Ordine();

		ordine.setTotale(carrello.totaleComplessivo());
		ordine.setData(new Date());
		ordine.setUtente(utente);

		ordineService.saveOrdine(ordine);

		Enumeration<String[]> prodotti = carrello.listaProdotti();
		while (prodotti.hasMoreElements()) {

			String[] prodotto = prodotti.nextElement();
			OrdineArticolo oa = new OrdineArticolo();

			oa.setArticolo(articoloService.findById(Long.valueOf(prodotto[4])).get());
			oa.setOrdine(ordine);
			oa.setQta(Integer.parseInt(prodotto[3]));

			ordineArticoloService.saveOrdineArticolo(oa);
		}

		Carrello nCarrello = new Carrello();
		session.setAttribute("carrello", nCarrello);

		return new ModelAndView("redirect:/visualizza/" + Long.toString(ordine.getIdOrdine()));
	}

	@RequestMapping(value = "/ordini", method = RequestMethod.GET)
	public ModelAndView ordini(HttpSession session) {

		if (session.getAttribute("carrello") != null) {

			ModelAndView mv = new ModelAndView();
			Utente utente = (Utente) session.getAttribute("utente_log");
			mv.setViewName("ordini");

			mv.addObject("ordiniList", ordineService.findByUsername(utente.getUsername()));
			mv.addObject("utente_log", session.getAttribute("utente_log"));

			return mv;
		} else {

			return new ModelAndView("redirect:/login");
		}
	}

	@RequestMapping(value = "/visualizza/{id}", method = RequestMethod.GET)
	public ModelAndView visualizzaOrdine(@PathVariable long id, HttpSession session) {

		ModelAndView mv = new ModelAndView();
		Ordine ordine = ordineService.findById(id).get();
		mv.setViewName("visualizza");

		mv.addObject("idOrdine", ordine.getIdOrdine());
		mv.addObject("totale", ordine.getTotale());
		mv.addObject("utente_log", session.getAttribute("utente_log"));
		mv.addObject("ordineList", ordineArticoloService.getOrdineArticoli(ordine.getIdOrdine()));

		return mv;
	}

	@RequestMapping(value = "/articolo/{id}", method = RequestMethod.GET)
	public ModelAndView dettagliArticolo(@PathVariable long id, HttpSession session) {

		Articolo a = articoloService.findById(id).get();
		Immagine img = immagineService.findById(id).isPresent() ? immagineService.findById(id).get() : null;

		ModelAndView mv = new ModelAndView();
		mv.setViewName("articolo");

		mv.addObject("articolo", a);
		mv.addObject("immagine", img);
		mv.addObject("utente_log", session.getAttribute("utente_log"));
		mv.addObject((Carrello) session.getAttribute("carrello"));

		return mv;
	}
}
