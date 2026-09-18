package com.example.be.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record SendMessageRequest(

		@NotNull(message = "chatId: non puo' essere vuoto")
		UUID chatId,

		@NotBlank(message = "contenuto: non puo' essere vuoto")
		String contenuto) {
}
