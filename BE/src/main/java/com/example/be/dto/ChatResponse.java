package com.example.be.dto;

import com.example.be.entities.Chat;
import java.util.UUID;

public record ChatResponse(UUID id, UserResponse utente1, UserResponse utente2) {

	public static ChatResponse di(Chat chat) {
		return new ChatResponse(chat.getId(), UserResponse.di(chat.getUtente1()), UserResponse.di(chat.getUtente2()));
	}
}
