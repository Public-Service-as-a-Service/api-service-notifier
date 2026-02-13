package se.sundsvall.notifier.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import se.sundsvall.notifier.api.model.response.EmployeeWithOrgNameResponse;
import se.sundsvall.notifier.service.EmployeeService;

@RestController
@RequestMapping("/api/notifier/employee")
@Tag(name = "Employee Resource")
@ApiResponses({
	@ApiResponse(
		responseCode = "200",
		description = "Successful Operation",
		useReturnTypeSchema = true),
	@ApiResponse(
		responseCode = "500",
		description = "Internal Server Error",
		content = @Content(schema = @Schema(implementation = Problem.class)))
})
public class EmployeeResource {

	private final EmployeeService employeeService;

	public EmployeeResource(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Operation(summary = "Get employee data from specific organization")
	@GetMapping("/{orgId}")
	public ResponseEntity<List<EmployeeWithOrgNameResponse>> getEmployeesByOrgId(@PathVariable String orgId) {
		return ResponseEntity.ok(employeeService.getEmployeesByOrg(orgId));
	}

	@Operation(summary = "Get employee data from all organizations")
	@GetMapping("/employees")
	public ResponseEntity<List<EmployeeWithOrgNameResponse>> getAllOrganizations() {
		return ResponseEntity.ok(employeeService.getAllEmployees());
	}

	@GetMapping("/ids")
	public ResponseEntity<List<EmployeeWithOrgNameResponse>> getAllOrganizationsWithList(@RequestParam List<String> orgIds) {
		return ResponseEntity.ok(employeeService.getEmployeesByOrgList(orgIds));
	}

	@Operation(summary = "Searches for employees matching search term")
	@GetMapping("/employees/search")
	public ResponseEntity<Page<EmployeeWithOrgNameResponse>> getEmployeesPartialSearch(@RequestParam String search, Pageable page) {
		return ResponseEntity.ok(employeeService.getEmployeesWithSearch(search, page));
	}
}
