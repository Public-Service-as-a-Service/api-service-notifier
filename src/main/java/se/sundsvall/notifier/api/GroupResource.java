package se.sundsvall.notifier.api;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import se.sundsvall.notifier.api.model.request.GroupRequest;
import se.sundsvall.notifier.api.model.request.GroupUpdateRequest;
import se.sundsvall.notifier.api.model.response.GroupResponse;
import se.sundsvall.notifier.service.GroupService;

@RestController
@RequestMapping("/api/notifier/groups")
public class GroupResource {
	private final GroupService groupService;

	public GroupResource(GroupService groupService) {
		this.groupService = groupService;
	}

	@GetMapping
	public ResponseEntity<List<GroupResponse>> getGroups(@RequestParam(required = false) String creatorId) {
		var responses = (creatorId == null)
			? groupService.getAllGroups()
			: groupService.getGroupsByCreatorId(creatorId);

		return ResponseEntity.ok(responses);
	}

	@GetMapping("/{groupId}")
	public ResponseEntity<GroupResponse> getGroupById(@PathVariable Long groupId) {
		var response = groupService.getGroupById(groupId);

		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<Void> createGroup(@Valid @RequestBody GroupRequest groupRequest) {
		var id = groupService.createGroup(groupRequest);

		return ResponseEntity.created(UriComponentsBuilder.fromPath("/api/notifier/groups/{id}")
			.buildAndExpand(id)
			.toUri())
			.build();
	}

	@PutMapping("/{groupId}")
	public ResponseEntity<GroupResponse> updateGroup(@PathVariable Long groupId, @Valid @RequestBody GroupUpdateRequest groupUpdateRequest) {
		var updatedGroup = groupService.updateGroup(groupId, groupUpdateRequest);

		return ResponseEntity.ok(updatedGroup);
	}

	@DeleteMapping("/{groupId}")
	public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
		groupService.deleteGroup(groupId);

		return ResponseEntity.noContent().build();
	}
}
