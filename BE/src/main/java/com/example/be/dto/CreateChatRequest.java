package com.example.be.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateChatRequest(

		@NotNull(message = "altroUtenteId: non puo' essere vuoto")
		UUID altroUtenteId) {
}
