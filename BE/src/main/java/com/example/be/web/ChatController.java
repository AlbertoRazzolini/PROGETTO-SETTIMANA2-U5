package com.example.be.web;

import com.example.be.dto.ChatResponse;
import com.example.be.dto.CreateChatRequest;
import com.example.be.service.ChatService;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

	private final ChatService chatService;

	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}

	@PostMapping
	public ChatResponse apri(Authentication autenticazione, @Validated @RequestBody CreateChatRequest richiesta) {
		return chatService.apri(autenticazione.getName(), richiesta);
	}
}
