package com.example.be.web;

import com.example.be.dto.UserResponse;
import com.example.be.service.UserService;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public List<UserResponse> lista(Authentication autenticazione) {
		return userService.listaAltriUtenti(autenticazione.getName());
	}
}
