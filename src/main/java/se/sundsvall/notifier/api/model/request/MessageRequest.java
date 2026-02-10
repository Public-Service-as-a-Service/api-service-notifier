package se.sundsvall.notifier.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record MessageRequest(
	@Schema(description = "Titel", example = "Viktigt meddelande") @NotBlank String title,
	@Schema(description = "innehållet på meddelandet", example = "Ryssen kommer! göm er! ps grattis på födelsedagen Pelle!") @NotBlank String content,
	@Schema(description = "Avsändarens e-post", example = "test@sundsvall.se") @NotBlank String sender,
	Long groupId,
	@Schema(description = "Lista på mottagare id", example = "[1, 2, 3]") @NotEmpty(message = "recipientEmployeeIds must contain at least one id") Set<Long> recipientEmployeeIds,
	@NotNull Boolean sendSms,
	@NotNull Boolean sendTeams) {}
