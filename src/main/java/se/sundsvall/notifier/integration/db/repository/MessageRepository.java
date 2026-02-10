package se.sundsvall.notifier.integration.db.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.notifier.integration.db.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findAllBySender(String sender);

	Long id(Long id);
}
