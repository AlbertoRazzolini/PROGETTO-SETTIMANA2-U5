package com.example.be.web;

import com.example.be.dto.SendMessageRequest;
import com.example.be.service.MessageService;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/** Il client pubblica su /app/chat.send: il prefisso lo aggiunge la configurazione. */
@Controller
public class ChatMessageController {

	private static final Logger log = LoggerFactory.getLogger(ChatMessageController.class);

	private final MessageService messaggi;

	public ChatMessageController(MessageService messaggi) {
		this.messaggi = messaggi;
	}

	@MessageMapping("/chat.send")
	public void invia(SendMessageRequest richiesta, Principal mittente) {
		if (mittente == null) {
			log.warn("messaggio da una sessione anonima: scartato");
			return;
		}
		messaggi.invia(mittente.getName(), richiesta);
	}
}
