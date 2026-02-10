package se.sundsvall.notifier.api.model.response;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record GroupResponse(
	Long id,
	String name,
	String description,
	String creatorId,
	Set<EmployeeResponse> employees,
	LocalDateTime createdAt) {
}
