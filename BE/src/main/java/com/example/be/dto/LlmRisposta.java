package com.example.be.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/** Risposta di /chat/completions: i campi sconosciuti vanno ignorati, non fatti fallire la lettura. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LlmRisposta(String id, String model, List<Scelta> choices) {

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Scelta(int index, Messaggio message, String finish_reason) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Messaggio(String role, String content) {
	}

	/** Il contenuto sta dentro choices, che e' un elenco - e puo' essere vuoto. */
	public String primoTesto() {
		if (choices == null || choices.isEmpty()) {
			return "";
		}
		var messaggio = choices.getFirst().message();
		if (messaggio == null || messaggio.content() == null) {
			return "";
		}
		return messaggio.content();
	}
}
