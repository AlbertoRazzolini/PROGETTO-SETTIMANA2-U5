package com.example.be.dto;

import com.example.be.entities.Message;
import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
		UUID id,
		UUID chatId,
		UserResponse mittente,
		String contenuto,
		Instant inviatoIl,
		boolean letto) {

	public static MessageResponse di(Message messaggio) {
		return new MessageResponse(
				messaggio.getId(),
				messaggio.getChat().getId(),
				UserResponse.di(messaggio.getMittente()),
				messaggio.getContenuto(),
				messaggio.getInviatoIl(),
				messaggio.isLetto());
	}
}
