package com.example.be.service;

import com.example.be.dto.UserResponse;
import com.example.be.entities.User;
import com.example.be.exception.RisorsaNonTrovataException;
import com.example.be.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository utenti;

	public UserService(UserRepository utenti) {
		this.utenti = utenti;
	}

	public List<UserResponse> listaAltriUtenti(String usernameLoggato) {
		User me = utenti.findByUsername(usernameLoggato)
				.orElseThrow(() -> new RisorsaNonTrovataException("utente loggato non trovato"));

		return utenti.findByIdNotOrderByUsernameAsc(me.getId()).stream()
				.map(UserResponse::di)
				.toList();
	}
}
