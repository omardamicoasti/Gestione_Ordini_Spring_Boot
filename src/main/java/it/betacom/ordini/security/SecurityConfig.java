package it.betacom.ordini.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import it.betacom.ordini.repository.UtenteRepository;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private UtenteRepository utenteRepository;
	
	public SecurityConfig(UtenteRepository utenteRepository) {
		this.utenteRepository = utenteRepository;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.authorizeRequests()
			.antMatchers("/")
			.permitAll()
			.antMatchers("/admin/**")
			.hasRole("ADMIN")
			.and()
			.formLogin()
			.loginPage("/loginAdmin")
			.permitAll()
			.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logoutAdmin"))
			.logoutSuccessUrl("/admin/");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(new UtentiDetailsService(this.utenteRepository));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
