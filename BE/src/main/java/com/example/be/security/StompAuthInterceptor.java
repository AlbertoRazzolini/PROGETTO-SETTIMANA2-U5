package com.example.be.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * Autentica la sessione WebSocket una sola volta, al frame CONNECT, leggendo
 * il JWT dall'header Authorization (non dalla query string: finirebbe nei log
 * dei proxy). La scadenza del token non chiude una connessione gia' aperta.
 */
@Component
public class StompAuthInterceptor implements ChannelInterceptor {

	private static final Logger log = LoggerFactory.getLogger(StompAuthInterceptor.class);

	private static final String PREFISSO = "Bearer ";

	private final JwtService jwt;

	public StompAuthInterceptor(JwtService jwt) {
		this.jwt = jwt;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {
			return message;
		}

		String intestazione = accessor.getFirstNativeHeader("Authorization");
		if (intestazione == null || !intestazione.startsWith(PREFISSO)) {
			log.warn("CONNECT senza Authorization: sessione anonima");
			return message;
		}

		String token = intestazione.substring(PREFISSO.length());
		if (!jwt.valido(token)) {
			log.warn("CONNECT con token non valido: sessione anonima");
			return message;
		}

		String username = jwt.estraiUsername(token);
		accessor.setUser(() -> username);
		log.info("CONNECT accettato utente={}", username);

		return message;
	}
}
