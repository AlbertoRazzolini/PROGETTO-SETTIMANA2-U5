package com.example.be.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "messages")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Setter(AccessLevel.NONE)
	private UUID id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "chat_id", nullable = false, updatable = false)
	private Chat chat;

	@ManyToOne(optional = false)
	@JoinColumn(name = "mittente_id", nullable = false, updatable = false)
	private User mittente;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String contenuto;

	@Column(nullable = false, updatable = false)
	private Instant inviatoIl = Instant.now();

	@Column(nullable = false)
	private boolean letto = false;

	private Instant lettoIl;

	public Message(Chat chat, User mittente, String contenuto) {
		this.chat = chat;
		this.mittente = mittente;
		this.contenuto = contenuto;
	}
}
