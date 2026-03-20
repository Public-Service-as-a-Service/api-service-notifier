package se.sundsvall.notifier.integration.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.notifier.integration.db.entity.MessageRecipient;

public interface MessageRecipientRepository extends JpaRepository<MessageRecipient, Long> {
	Page<MessageRecipient> findByMessageId(Long messageId, Pageable pageable);
}
