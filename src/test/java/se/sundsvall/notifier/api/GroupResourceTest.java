package se.sundsvall.notifier.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.notifier.Application;
import se.sundsvall.notifier.api.model.request.GroupRequest;
import se.sundsvall.notifier.api.model.request.GroupUpdateRequest;
import se.sundsvall.notifier.api.model.response.GroupResponse;
import se.sundsvall.notifier.service.GroupService;

@ActiveProfiles("junit")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupResourceTest {

	@MockitoBean
	private GroupService groupService;

	@Autowired
	private WebTestClient webTestClient;

	private static final String BASE_PATH = "/api/notifier/groups";

	@Test
	void getGroups_withoutCreatorId_ok_returnsAllGroups() {
		var response1 = GroupResponse.builder().withId(1L).withName("G1").build();
		var response2 = GroupResponse.builder().withId(2L).withName("G2").build();

		when(groupService.getAllGroups()).thenReturn(List.of(response1, response2));

		webTestClient.get()
			.uri(BASE_PATH)
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(GroupResponse.class)
			.contains(response1, response2);

		verify(groupService).getAllGroups();
		verifyNoMoreInteractions(groupService);
	}

	@Test
	void getGroups_withCreatorId_ok_returnsGroupsByCreator() {
		var creatorId = "creator-123";
		var response = GroupResponse.builder().withId(1L).withCreatorId(creatorId).build();

		when(groupService.getGroupsByCreatorId(creatorId)).thenReturn(List.of(response));

		webTestClient.get()
			.uri(uriBuilder -> uriBuilder.path(BASE_PATH)
				.queryParam("creatorId", creatorId)
				.build())
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(GroupResponse.class)
			.contains(response);

		verify(groupService).getGroupsByCreatorId(creatorId);
		verifyNoMoreInteractions(groupService);
	}

	@Test
	void getGroupById_ok() {
		var groupId = 1L;
		var response = GroupResponse.builder().withId(groupId).withName("G1").build();

		when(groupService.getGroupById(groupId)).thenReturn(response);

		webTestClient.get()
			.uri(BASE_PATH + "/" + groupId)
			.exchange()
			.expectStatus().isOk()
			.expectBody(GroupResponse.class)
			.isEqualTo(response);

		verify(groupService).getGroupById(groupId);
		verifyNoMoreInteractions(groupService);
	}

	@Test
	void createGroup_ok_returns201_andLocationHeader() {
		var request = GroupRequest.builder()
			.withName("Team A")
			.withDescription("Beskrivning")
			.withCreatorId("creator-123")
			.withEmployees(Set.of(10L, 20L))
			.build();

		when(groupService.createGroup(any(GroupRequest.class))).thenReturn(123L);

		webTestClient.post()
			.uri(BASE_PATH)
			.bodyValue(request)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().valueEquals("Location", "/api/notifier/groups/123");

		verify(groupService).createGroup(any(GroupRequest.class));
		verifyNoMoreInteractions(groupService);
	}

	@Test
	void createGroup_invalidBody_returns400() {
		var invalid = GroupRequest.builder()
			.withName("")
			.withDescription("")
			.withCreatorId("")
			.withEmployees(null)
			.build();

		webTestClient.post()
			.uri(BASE_PATH)
			.bodyValue(invalid)
			.exchange()
			.expectStatus().isBadRequest();

		verifyNoMoreInteractions(groupService);
	}

	@Test
	void updateGroup_ok_returnsUpdatedGroup() {
		var groupId = 1L;

		var request = GroupUpdateRequest.builder()
			.withName("New name")
			.withDescription("New desc")
			.withEmployees(Set.of(10L))
			.build();

		var response = GroupResponse.builder()
			.withId(groupId)
			.withName("New name")
			.withDescription("New desc")
			.build();

		when(groupService.updateGroup(eq(groupId), any(GroupUpdateRequest.class))).thenReturn(response);

		webTestClient.put()
			.uri(BASE_PATH + "/" + groupId)
			.bodyValue(request)
			.exchange()
			.expectStatus().isOk()
			.expectBody(GroupResponse.class)
			.isEqualTo(response);

		verify(groupService).updateGroup(eq(groupId), any(GroupUpdateRequest.class));
		verifyNoMoreInteractions(groupService);
	}

	@Test
	void updateGroup_invalidBody_returns400() {
		var groupId = 1L;

		var invalid = GroupUpdateRequest.builder()
			.withName("")
			.withDescription("")
			.withEmployees(null)
			.build();

		webTestClient.put()
			.uri(BASE_PATH + "/" + groupId)
			.bodyValue(invalid)
			.exchange()
			.expectStatus().isBadRequest();

		verifyNoMoreInteractions(groupService);
	}

	@Test
	void deleteGroup_ok_returns204() {
		var groupId = 1L;

		webTestClient.delete()
			.uri(BASE_PATH + "/" + groupId)
			.exchange()
			.expectStatus().isNoContent()
			.expectBody().isEmpty();

		verify(groupService).deleteGroup(groupId);
		verifyNoMoreInteractions(groupService);
	}
}
