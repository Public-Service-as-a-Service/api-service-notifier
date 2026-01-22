package se.sundsvall.notifier.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

@RestController("/api/notifier/employee")
@Tag(name = "Employee Resource")
public class EmployeeResource {

	@Operation(summary = "Get employee data", responses = {
		@ApiResponse(
			responseCode = "200",
			description = "Successful Operation",
			useReturnTypeSchema = true),
		@ApiResponse(
			responseCode = "404",
			description = "Not Found",
			content = @Content(schema = @Schema(implementation = Problem.class))),
		@ApiResponse(
			responseCode = "500",
			description = "Internal Server Error",
			content = @Content(schema = @Schema(implementation = Problem.class)))
	})
	@GetMapping()
	public String employee() {
		return "detta är en employee";
	}
}
