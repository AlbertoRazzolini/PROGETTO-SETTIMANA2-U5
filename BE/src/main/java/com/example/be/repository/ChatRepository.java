package com.example.be.repository;

import com.example.be.entities.Chat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

	@Query("""
			SELECT c FROM Chat c
			WHERE (c.utente1.id = :utenteA AND c.utente2.id = :utenteB)
			   OR (c.utente1.id = :utenteB AND c.utente2.id = :utenteA)
			""")
	Optional<Chat> trovaTraUtenti(@Param("utenteA") UUID utenteA, @Param("utenteB") UUID utenteB);

	List<Chat> findByUtente1_IdOrUtente2_Id(UUID utente1Id, UUID utente2Id);

	long countByUtente1_IdOrUtente2_Id(UUID utente1Id, UUID utente2Id);
}
