package com.example.be.repository;

import com.example.be.entities.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

	List<Message> findByChatIdOrderByInviatoIlAsc(UUID chatId);
}
