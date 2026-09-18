package com.example.be.security;

import com.example.be.entities.User;
import com.example.be.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository utenti;

	public CustomUserDetailsService(UserRepository utenti) {
		this.utenti = utenti;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User utente = utenti.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("utente non trovato: " + username));

		return org.springframework.security.core.userdetails.User
				.withUsername(utente.getUsername())
				.password(utente.getPassword())
				.authorities(java.util.List.of())
				.build();
	}
}
