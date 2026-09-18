package com.example.be.dto;

import com.example.be.entities.User;
import java.util.UUID;

public record UserResponse(UUID id, String username, String email) {

	public static UserResponse di(User utente) {
		return new UserResponse(utente.getId(), utente.getUsername(), utente.getEmail());
	}
}
