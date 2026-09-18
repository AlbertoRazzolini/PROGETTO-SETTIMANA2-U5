package com.example.be.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "chats")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Setter(AccessLevel.NONE)
	private UUID id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "utente1_id", nullable = false, updatable = false)
	private User utente1;

	@ManyToOne(optional = false)
	@JoinColumn(name = "utente2_id", nullable = false, updatable = false)
	private User utente2;

	public Chat(User utente1, User utente2) {
		this.utente1 = utente1;
		this.utente2 = utente2;
	}
}
