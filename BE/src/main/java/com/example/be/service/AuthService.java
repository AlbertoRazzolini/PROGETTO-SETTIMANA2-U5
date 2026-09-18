package com.example.be.service;

import com.example.be.dto.LoginRequest;
import com.example.be.dto.LoginResponse;
import com.example.be.dto.RegisterRequest;
import com.example.be.dto.UserResponse;
import com.example.be.entities.User;
import com.example.be.exception.DatoGiaRegistratoException;
import com.example.be.repository.UserRepository;
import com.example.be.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UserRepository utenti;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwt;

	public AuthService(UserRepository utenti, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwt) {
		this.utenti = utenti;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwt = jwt;
	}

	public UserResponse registra(RegisterRequest richiesta) {
		if (utenti.existsByUsername(richiesta.username())) {
			throw new DatoGiaRegistratoException("username gia' in uso");
		}
		if (utenti.existsByEmail(richiesta.email())) {
			throw new DatoGiaRegistratoException("email gia' registrata");
		}

		User utente = new User(richiesta.username(), richiesta.email(),
				passwordEncoder.encode(richiesta.password()));
		return UserResponse.di(utenti.save(utente));
	}

	public LoginResponse login(LoginRequest richiesta) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(richiesta.username(), richiesta.password()));

		User utente = utenti.findByUsername(richiesta.username())
				.orElseThrow(() -> new IllegalStateException("utente autenticato ma non trovato: " + richiesta.username()));

		String token = jwt.genera(utente.getUsername());
		return new LoginResponse(token, UserResponse.di(utente));
	}
}
