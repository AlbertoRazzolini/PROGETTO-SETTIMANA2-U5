package com.example.be.repository;

import com.example.be.entities.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

	List<Message> findByChatIdOrderByInviatoIlAsc(UUID chatId);

	List<Message> findByChatIdAndMittenteIdNotAndLettoFalse(UUID chatId, UUID mittenteId);

	long countByMittenteId(UUID mittenteId);

	@Query("""
			SELECT COUNT(m) FROM Message m
			WHERE (m.chat.utente1.id = :utenteId OR m.chat.utente2.id = :utenteId)
			  AND m.mittente.id <> :utenteId
			""")
	long contaRicevuti(@Param("utenteId") UUID utenteId);
}
