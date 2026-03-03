package se.sundsvall.notifier.integration.smssender;

import lombok.Builder;
import se.sundsvall.notifier.api.model.request.Priority;

@Builder(setterPrefix = "with")
public record SmsDto(
	String sender,
	String mobileNumber,
	String message,
	Priority priority) {
}
