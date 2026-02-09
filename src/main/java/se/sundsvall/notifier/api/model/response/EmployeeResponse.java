package se.sundsvall.notifier.api.model.response;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record EmployeeResponse(
	String personId,
	String orgId,
	String firstName,
	String lastName,
	String email,
	String workMobile,
	String workPhone,
	String workTitle) {}
