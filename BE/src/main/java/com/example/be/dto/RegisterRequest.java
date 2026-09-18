package com.example.be.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

		@NotBlank(message = "username: non puo' essere vuoto")
		String username,

		@NotBlank(message = "email: non puo' essere vuota")
		@Email(message = "email: formato non valido")
		String email,

		@NotBlank(message = "password: non puo' essere vuota")
		@Size(min = 8, message = "password: minimo 8 caratteri")
		String password) {
}
