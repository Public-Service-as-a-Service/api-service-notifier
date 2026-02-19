package se.sundsvall.notifier.api;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.sundsvall.notifier.api.model.response.OrganizationResponse;
import se.sundsvall.notifier.service.OrganizationService;

@WebMvcTest(
	controllers = OrganizationResource.class,
	excludeAutoConfiguration = {
		SecurityAutoConfiguration.class,
		OAuth2ClientAutoConfiguration.class,
		OAuth2ResourceServerAutoConfiguration.class
	})
class OrganizationResourceTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private OrganizationService service;

	@Test
	void getOne_successful_test() throws Exception {
		var response = mock(OrganizationResponse.class);

		when(service.getSpecificOrg("id")).thenReturn(response);

		mvc.perform(get("/api/notifier/organization/id")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		verify(service).getSpecificOrg("id");
	}

	@Test
	void getAll_successfulTest() throws Exception {
		var response = List.of(
			new OrganizationResponse("1", "ParentorgId1", "orgId1", "name1", 3),
			new OrganizationResponse("2", "ParentorgId2", "orgId2", "name2", 4));

		when(service.getAllOrganizations()).thenReturn(response);

		mvc.perform(get("/api/notifier/organization/organizations"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2));

		verify(service).getAllOrganizations();

	}

	@Test
	void getWithList_successful() throws Exception {
		var response = List.of(
			new OrganizationResponse("1", "ParentorgId1", "orgId1", "name1", 3),
			new OrganizationResponse("2", "ParentorgId2", "orgId2", "name2", 4));

		when(service.getOrgsById(List.of("orgId1", "orgId2"))).thenReturn(response);

		mvc.perform(get("/api/notifier/organization/ids").param("orgId", "orgId1", "orgId2"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2));

		verify(service).getOrgsById(List.of("orgId1", "orgId2"));
	}

	@Test
	void getOrgChildrenAndDescendants_successful() throws Exception {
		var response = List.of(
			new OrganizationResponse("1", "ParentorgId1", "orgId1", "name1", 3),
			new OrganizationResponse("2", "ParentorgId2", "orgId2", "name2", 4));

		when(service.getOrgChildrenAndDescendantsWithId("orgId1")).thenReturn(response);

		mvc.perform(get("/api/notifier/organization/{orgId}/children/descendants", "orgId1")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		verify(service).getOrgChildrenAndDescendantsWithId("orgId1");
	}

	@Test
	void getOrgAndChildren_successful() throws Exception {
		var response = List.of(
			new OrganizationResponse("1", "ParentorgId1", "orgId1", "name1", 3),
			new OrganizationResponse("2", "ParentorgId2", "orgId2", "name2", 4));

		when(service.getChildrenReplaceDuplicateDescendantsWithRoot("orgId1")).thenReturn(response);

		mvc.perform(get("/api/notifier/organization/{orgId}/children", "orgId1")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		verify(service).getChildrenReplaceDuplicateDescendantsWithRoot("orgId1");
	}
}
