package com.example.be.service;

import com.example.be.dto.LlmCorpo;
import com.example.be.dto.LlmRisposta;
import com.example.be.dto.MessageResponse;
import com.example.be.dto.SuggestionResponse;
import com.example.be.exception.ServizioIaNonDisponibileException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

/**
 * Suggerisce il prossimo messaggio leggendo la cronologia recente della chat.
 * La risposta NON viene mai salvata: se l'utente la accetta, la invia lui
 * stesso con il normale flusso STOMP, e a quel punto diventa un messaggio
 * come un altro.
 */
@Service
public class AiSuggestionService {

	private static final Logger log = LoggerFactory.getLogger(AiSuggestionService.class);

	private static final int MESSAGGI_DI_CONTESTO = 10;

	private static final String ISTRUZIONI = """
			Sei un assistente che aiuta a continuare una conversazione di chat in italiano.
			Ti viene fornita la cronologia recente, con il nome di chi ha scritto ogni riga.
			Suggerisci UN SOLO messaggio breve, naturale e pertinente che l'utente indicato
			possa inviare per continuare la conversazione.
			Rispondi SOLO con il testo del messaggio, senza virgolette e senza spiegazioni.
			""";

	private final MessageService messageService;
	private final RestClient client;
	private final String modello;
	private final int maxTokens;
	private final boolean ragionamento;

	public AiSuggestionService(MessageService messageService, RestClient llmClient,
			@Value("${app.llm.model}") String modello,
			@Value("${app.llm.max-tokens}") int maxTokens,
			@Value("${app.llm.reasoning:false}") boolean ragionamento) {
		this.messageService = messageService;
		this.client = llmClient;
		this.modello = modello;
		this.maxTokens = maxTokens;
		this.ragionamento = ragionamento;
	}

	public SuggestionResponse suggerisci(String username, UUID chatId) {
		List<MessageResponse> cronologia = messageService.cronologia(username, chatId);

		String trascrizione = cronologia.stream()
				.skip(Math.max(0, cronologia.size() - MESSAGGI_DI_CONTESTO))
				.map(m -> m.mittente().username() + ": " + m.contenuto())
				.reduce((a, b) -> a + "\n" + b)
				.orElse("(nessun messaggio precedente)");

		String testoUtente = trascrizione
				+ "\n\nSuggerisci il prossimo messaggio da inviare come " + username + ".";

		LlmCorpo corpo = LlmCorpo.di(modello, maxTokens, ragionamento, ISTRUZIONI, testoUtente);
		LlmRisposta risposta = chiama(corpo);

		return new SuggestionResponse(risposta.primoTesto().trim());
	}

	private LlmRisposta chiama(LlmCorpo corpo) {
		try {
			return client.post()
					.uri("/chat/completions")
					.body(corpo)
					.retrieve()
					.onStatus(HttpStatusCode::isError, (richiesta, risposta) -> {
						log.error("errore dal servizio IA: {}", risposta.getStatusCode());
						throw new ServizioIaNonDisponibileException("il servizio di suggerimento non e' disponibile");
					})
					.body(LlmRisposta.class);
		} catch (ResourceAccessException ex) {
			log.error("servizio IA non raggiungibile: {}", ex.getMessage());
			throw new ServizioIaNonDisponibileException("il servizio di suggerimento non e' raggiungibile");
		}
	}
}
