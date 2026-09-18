package com.example.be.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

		@NotBlank(message = "username: non puo' essere vuoto")
		String username,

		@NotBlank(message = "password: non puo' essere vuota")
		String password) {
}
