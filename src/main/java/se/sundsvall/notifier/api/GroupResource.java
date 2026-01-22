package se.sundsvall.notifier.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

@RestController("/api/notifier")
@Tag(name = "Group Resource")

@ApiResponse(
	responseCode = "200",
	description = "Successful Operation",
	useReturnTypeSchema = true)
@ApiResponse(
	responseCode = "400",
	description = "Bad Request",
	content = @Content(schema = @Schema(implementation = Problem.class)))
@ApiResponse(
	responseCode = "500",
	description = "Internal Server Error",
	content = @Content(schema = @Schema(implementation = Problem.class)))

public class GroupResource {

	@Operation(summary = "Get information about groups")
	@GetMapping("/groups")
	public String getGroups() {
		return "Här kommer en grupp";
	}

	@Operation(summary = "")
	@PostMapping("/groups/{group}")
	public String postGroup(@PathVariable("group") Long group) {
		return "grupp tillagd";
	}

	@Operation(summary = "Update group information")
	@PutMapping("/groups/{groupId}")
	public String putGroup(@PathVariable("groupId") Long groupId) {
		return "en specific grupp";
	}

	@Operation(summary = "Delete group using groupId")
	@DeleteMapping("/groups/{groupId}")
	public String deleteGroup(@PathVariable("groupId") Long groupId) {
		return "grupp borttagen";
	}

	@Operation(summary = "Get group member from specific group")
	@GetMapping("/groups/{groupId}/{memberId}")
	public String getMember(@PathVariable("groupId") Long groupId, @PathVariable("memberId") Long member) {
		return "här kommer en medlem";
	}

	@Operation(summary = "Add member to a group")
	@PostMapping("/groups/{groupId}/members")
	public String postMember(@PathVariable("groupId") Long groupId) {
		return "medlem tillagd";
	}

	@Operation(summary = "Delete an emplyee from group")
	@DeleteMapping("/groups/{groupid}/members/{employee}")
	public String deleteMember(@PathVariable("groupId") int groupId) {
		return "Borttagen";
	}

}
