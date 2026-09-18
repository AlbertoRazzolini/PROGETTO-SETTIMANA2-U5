package com.example.be.web;

import com.example.be.dto.ChatResponse;
import com.example.be.dto.CreateChatRequest;
import com.example.be.dto.MessageResponse;
import com.example.be.service.ChatService;
import com.example.be.service.MessageService;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

	private final ChatService chatService;
	private final MessageService messageService;

	public ChatController(ChatService chatService, MessageService messageService) {
		this.chatService = chatService;
		this.messageService = messageService;
	}

	@PostMapping
	public ChatResponse apri(Authentication autenticazione, @Validated @RequestBody CreateChatRequest richiesta) {
		return chatService.apri(autenticazione.getName(), richiesta);
	}

	@GetMapping("/{chatId}/messages")
	public List<MessageResponse> messaggi(Authentication autenticazione, @PathVariable UUID chatId) {
		return messageService.cronologia(autenticazione.getName(), chatId);
	}
}
