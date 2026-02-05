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
import se.sundsvall.notifier.api.model.request.OrganizationResponse;
import se.sundsvall.notifier.service.OrganizationService;

@RestController
@RequestMapping("/api/notifier/organization")
@Tag(name = "Organization Resource")
public class OrganizationResource {

	private final OrganizationService organizationService;

	public OrganizationResource(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Operation(summary = "Get spicific organization", responses = {
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
	public ResponseEntity<OrganizationResponse> getSpecificOrganization(@PathVariable String orgId) {
		return ResponseEntity.ok(organizationService.getSpecificOrg(orgId));
	}

	@Operation(summary = "Get all organzations", responses = {
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
	@GetMapping("/organizations")
	public ResponseEntity<List<OrganizationResponse>> getAllOrganizations() {
		return ResponseEntity.ok(organizationService.getAllOrganizations());
	}

	@Operation(summary = "Get multiple organizations with a list of organization ids", responses = {
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
	public ResponseEntity<List<OrganizationResponse>> getOrganizationsWithList(@RequestParam List<String> orgId) {
		return ResponseEntity.ok(organizationService.getOrgsById(orgId));
	}

	@Operation(summary = "Get a organization and all organizations under it", responses = {
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
	@GetMapping("/{orgId}/children")
	public ResponseEntity<List<OrganizationResponse>> getOrganizationAndChildren(@PathVariable String orgId) {
		return ResponseEntity.ok(organizationService.getOrgAndChildrenWithId(orgId));
	}
}
