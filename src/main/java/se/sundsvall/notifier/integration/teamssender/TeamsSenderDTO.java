package se.sundsvall.notifier.integration.teamssender;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record TeamsSenderDTO(
	String recipient,
	String message) {}
