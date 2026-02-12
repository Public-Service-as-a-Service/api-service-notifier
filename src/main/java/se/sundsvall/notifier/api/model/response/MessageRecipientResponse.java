package se.sundsvall.notifier.api.model.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record MessageRecipientResponse(
	Long employeeId,
	String orgId,
	String workTitle,
	String deliveryStatus,
	LocalDateTime receivedAt) {
}
