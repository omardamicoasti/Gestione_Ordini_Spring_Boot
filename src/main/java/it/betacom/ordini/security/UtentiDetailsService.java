package it.betacom.ordini.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.betacom.ordini.model.Utente;
import it.betacom.ordini.repository.UtenteRepository;

@Configuration
public class UtentiDetailsService implements UserDetailsService {
	
	private UtenteRepository utenteRepo;

	public UtentiDetailsService(UtenteRepository utenteRepository) {
		this.utenteRepo = utenteRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		try {
			
			Utente utente = utenteRepo.findByUsername(username).get();
			
			if(utente != null) {
				
				System.out.println("--------PWD USERDETAILS-------- " + utente.getPassword());
				
				return User.withUsername(utente.getUsername())
							.accountLocked(!utente.isEnabled())
							.password(utente.getPassword())
							.roles(utente.getRole())
							.build();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		throw new UsernameNotFoundException(username);
	}

}
