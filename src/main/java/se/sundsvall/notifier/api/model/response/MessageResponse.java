package se.sundsvall.notifier.api.model.response;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record MessageResponse(
	Long id,
	String title,
	String content,
	String sender,
	LocalDateTime createdAt,
	Set<MessageRecipientDto> recipients) {
}
