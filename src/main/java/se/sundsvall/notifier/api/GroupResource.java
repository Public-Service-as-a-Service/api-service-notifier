package se.sundsvall.notifier.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/notifier")
public class GroupResource {

	@GetMapping("/groups")
	public String getGroups() {
		return "Här kommer en grupp";
	}

	@PostMapping("/groups/{group}")
	public String postGroup(@PathVariable("group") String group) {
		return group;
	}

	@PutMapping("/group/{groupId}")
	public String putGroup(@PathVariable("groupId") String groupId) {
		return groupId;
	}

	@DeleteMapping("/group/{groupId}")
	public String deleteGroup(@PathVariable("groupId") String groupId) {
		return groupId;
	}

	@GetMapping("/groups/{groupId}/{memberId}")
	public String getMember(@PathVariable("groupId") Long groupId, @PathVariable("memberId") Long member) {
		return "här kommer en meblem";
	}

	@PostMapping("/groups/{groupId}/member")
	public String postMember(@PathVariable("groupId") Long groupId) {
		return "medlem tillagd";
	}

	@DeleteMapping("/groups/{groupid}/members/{emplyee}")
	public String deleteMember(@PathVariable("groupId") int groupId) {
		return "Borttagen";
	}

}
