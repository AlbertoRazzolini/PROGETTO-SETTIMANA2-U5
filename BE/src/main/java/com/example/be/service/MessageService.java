package com.example.be.service;

import com.example.be.dto.MessageResponse;
import com.example.be.dto.SendMessageRequest;
import com.example.be.entities.Chat;
import com.example.be.entities.Message;
import com.example.be.entities.User;
import com.example.be.exception.AccessoNegatoException;
import com.example.be.exception.RisorsaNonTrovataException;
import com.example.be.repository.ChatRepository;
import com.example.be.repository.MessageRepository;
import com.example.be.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	private static final Logger log = LoggerFactory.getLogger(MessageService.class);

	private final MessageRepository messaggi;
	private final ChatRepository chats;
	private final UserRepository utenti;
	private final SimpMessagingTemplate template;

	public MessageService(MessageRepository messaggi, ChatRepository chats, UserRepository utenti,
			SimpMessagingTemplate template) {
		this.messaggi = messaggi;
		this.chats = chats;
		this.utenti = utenti;
		this.template = template;
	}

	/**
	 * Prima si salva, poi si pubblica: cosi' un client che ricarica la
	 * cronologia via REST subito dopo trova comunque il messaggio.
	 */
	public MessageResponse invia(String usernameMittente, SendMessageRequest richiesta) {
		User mittente = utenti.findByUsername(usernameMittente)
				.orElseThrow(() -> new RisorsaNonTrovataException("utente non trovato: " + usernameMittente));

		Chat chat = chats.findById(richiesta.chatId())
				.orElseThrow(() -> new RisorsaNonTrovataException("chat non trovata: " + richiesta.chatId()));

		boolean partecipante = chat.getUtente1().getId().equals(mittente.getId())
				|| chat.getUtente2().getId().equals(mittente.getId());
		if (!partecipante) {
			throw new AccessoNegatoException("non fai parte di questa chat");
		}

		Message salvato = messaggi.save(new Message(chat, mittente, richiesta.contenuto()));

		MessageResponse risposta = MessageResponse.di(salvato);
		template.convertAndSend("/topic/chat/" + chat.getId(), risposta);

		log.info("messaggio {} in chat {} da {}", salvato.getId(), chat.getId(), usernameMittente);
		return risposta;
	}
}
