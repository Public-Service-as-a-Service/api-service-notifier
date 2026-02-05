package se.sundsvall.notifier.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import se.sundsvall.notifier.api.model.request.EmployeeResponse;
import se.sundsvall.notifier.service.EmployeeService;

@RestController
@RequestMapping("/api/notifier/employee")
@Tag(name = "Employee Resource")
public class EmployeeResource {

	private final EmployeeService employeeService;

	public EmployeeResource(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Operation(summary = "Get employee data from specific organization", responses = {
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
	@GetMapping("/{orgId}")
	public ResponseEntity<List<EmployeeResponse>> getEmployeesByOrgId(@PathVariable String orgId) {
		return ResponseEntity.ok(employeeService.getEmployeesByOrg(orgId));
	}

	@Operation(summary = "Get employee data from all organizations", responses = {
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
	@GetMapping("/employees")
	public ResponseEntity<List<EmployeeResponse>> getAllOrganizations() {
		return ResponseEntity.ok(employeeService.getAllEmployees());
	}

	@Operation(summary = "Get employee data from multiple organizations", responses = {
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

	@GetMapping("/ids")
	public ResponseEntity<List<EmployeeResponse>> getAllOrganizationsWithList(@RequestParam List<String> orgIds) {
		return ResponseEntity.ok(employeeService.getEmployeesByOrgList(orgIds));
	}
}
