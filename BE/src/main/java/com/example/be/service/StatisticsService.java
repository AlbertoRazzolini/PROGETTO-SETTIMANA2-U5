package com.example.be.service;

import com.example.be.dto.StatisticsResponse;
import com.example.be.entities.User;
import com.example.be.exception.EmailNonInviataException;
import com.example.be.exception.RisorsaNonTrovataException;
import com.example.be.repository.ChatRepository;
import com.example.be.repository.MessageRepository;
import com.example.be.repository.UserRepository;
import jakarta.mail.MessagingException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class StatisticsService {

	private static final Logger log = LoggerFactory.getLogger(StatisticsService.class);

	private final MessageRepository messaggi;
	private final ChatRepository chats;
	private final UserRepository utenti;
	private final EmailService emailService;
	private final SpringTemplateEngine motore;

	public StatisticsService(MessageRepository messaggi, ChatRepository chats, UserRepository utenti,
			EmailService emailService, SpringTemplateEngine motore) {
		this.messaggi = messaggi;
		this.chats = chats;
		this.utenti = utenti;
		this.emailService = emailService;
		this.motore = motore;
	}

	public StatisticsResponse calcola(String username) {
		User utente = trovaUtente(username);
		return calcola(utente);
	}

	public void inviaViaEmail(String username) {
		User utente = trovaUtente(username);
		StatisticsResponse statistiche = calcola(utente);

		var ctx = new Context();
		ctx.setVariables(Map.of(
				"username", utente.getUsername(),
				"messaggiInviati", statistiche.messaggiInviati(),
				"messaggiRicevuti", statistiche.messaggiRicevuti(),
				"chatAperte", statistiche.chatAperte()));
		String html = motore.process("email/statistiche", ctx);

		String testoAlternativo = "Ciao %s, messaggi inviati: %d, ricevuti: %d, chat aperte: %d".formatted(
				utente.getUsername(), statistiche.messaggiInviati(), statistiche.messaggiRicevuti(),
				statistiche.chatAperte());

		try {
			emailService.inviaHtml(utente.getEmail(), "Le tue statistiche", testoAlternativo, html);
			log.info("statistiche inviate via email a {}", utente.getUsername());
		} catch (MessagingException | MailException ex) {
			log.error("invio email fallito per {}", utente.getUsername(), ex);
			throw new EmailNonInviataException("invio email fallito", ex);
		}
	}

	private StatisticsResponse calcola(User utente) {
		int inviati = (int) messaggi.countByMittenteId(utente.getId());
		int ricevuti = (int) messaggi.contaRicevuti(utente.getId());
		int chatAperte = (int) chats.countByUtente1_IdOrUtente2_Id(utente.getId(), utente.getId());
		return new StatisticsResponse(inviati, ricevuti, chatAperte);
	}

	private User trovaUtente(String username) {
		return utenti.findByUsername(username)
				.orElseThrow(() -> new RisorsaNonTrovataException("utente non trovato: " + username));
	}
}
