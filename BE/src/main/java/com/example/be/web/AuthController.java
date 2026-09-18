package com.example.be.web;

import com.example.be.dto.LoginRequest;
import com.example.be.dto.LoginResponse;
import com.example.be.dto.RegisterRequest;
import com.example.be.dto.UserResponse;
import com.example.be.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService auth;

	public AuthController(AuthService auth) {
		this.auth = auth;
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse registra(@Validated @RequestBody RegisterRequest richiesta) {
		return auth.registra(richiesta);
	}

	@PostMapping("/login")
	public LoginResponse login(@Validated @RequestBody LoginRequest richiesta) {
		return auth.login(richiesta);
	}
}
