package com.example.be.service;

import com.example.be.dto.ChatResponse;
import com.example.be.dto.CreateChatRequest;
import com.example.be.entities.Chat;
import com.example.be.entities.User;
import com.example.be.exception.RisorsaNonTrovataException;
import com.example.be.repository.ChatRepository;
import com.example.be.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

	private final ChatRepository chats;
	private final UserRepository utenti;

	public ChatService(ChatRepository chats, UserRepository utenti) {
		this.chats = chats;
		this.utenti = utenti;
	}

	public ChatResponse apri(String usernameLoggato, CreateChatRequest richiesta) {
		User me = utenti.findByUsername(usernameLoggato)
				.orElseThrow(() -> new RisorsaNonTrovataException("utente loggato non trovato"));

		if (me.getId().equals(richiesta.altroUtenteId())) {
			throw new IllegalArgumentException("non puoi aprire una chat con te stesso");
		}

		User altro = utenti.findById(richiesta.altroUtenteId())
				.orElseThrow(() -> new RisorsaNonTrovataException("utente non trovato: " + richiesta.altroUtenteId()));

		Chat chat = chats.trovaTraUtenti(me.getId(), altro.getId())
				.orElseGet(() -> chats.save(new Chat(me, altro)));

		return ChatResponse.di(chat);
	}

	public List<ChatResponse> listaChat(String usernameLoggato) {
		User me = utenti.findByUsername(usernameLoggato)
				.orElseThrow(() -> new RisorsaNonTrovataException("utente loggato non trovato"));

		return chats.findByUtente1_IdOrUtente2_Id(me.getId(), me.getId()).stream()
				.map(ChatResponse::di)
				.toList();
	}
}
