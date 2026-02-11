package se.sundsvall.notifier.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.zalando.problem.Problem;
import se.sundsvall.notifier.api.model.request.MessageRequest;
import se.sundsvall.notifier.api.model.response.MessageResponse;
import se.sundsvall.notifier.service.MessageService;

@RestController
@RequestMapping("/api/notifier")
@Tag(name = "Message Resource")
@ApiResponse(
	responseCode = "401",
	description = "Bad Request",
	content = @Content(schema = @Schema(implementation = Problem.class)))
@ApiResponse(
	responseCode = "500",
	description = "Internal Server Error",
	content = @Content(schema = @Schema(implementation = Problem.class)))
public class MessageResource {

	private final MessageService messageService;

	public MessageResource(MessageService messageService) {
		this.messageService = messageService;
	}

	@Operation(description = "Create a new message")
	@ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	@PostMapping("/messages")
	public ResponseEntity<Void> sendMessage(@RequestBody @Valid MessageRequest message) {
		messageService.createMessage(message);
		return ResponseEntity.noContent().build();
	}

	@Operation(description = "Get message from specific user")
	@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true)
	@GetMapping("/messages/senders/{sender}")
	public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable @Valid @Email String sender) {
		var messageHistory = messageService.getMessages(sender);
		return ResponseEntity.ok(messageHistory);
	}

	// Denna endpoint är mer för clean up under utveckling
	@DeleteMapping("/messages/{id}")
	public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
		messageService.deleteMessages(id);
		return ResponseEntity.noContent().build();
	}
}
