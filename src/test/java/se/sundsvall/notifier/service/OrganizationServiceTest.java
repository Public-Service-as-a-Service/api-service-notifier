package se.sundsvall.notifier.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.notifier.api.model.response.OrganizationResponse;
import se.sundsvall.notifier.integration.db.entity.Organization;
import se.sundsvall.notifier.integration.db.repository.OrganizationRepository;
import se.sundsvall.notifier.service.mapper.GroupEmployeeOrganizationMapper;

@ExtendWith(MockitoExtension.class)
public class OrganizationServiceTest {
	@Mock
	private GroupEmployeeOrganizationMapper mapper;

	@Mock
	private OrganizationRepository organizationRepository;

	@Test
	void getAllOrganizations() {
		var service = new OrganizationService(mapper, organizationRepository);

		var org1 = new Organization();
		var org2 = new Organization();

		var response1 = mock(OrganizationResponse.class);
		var response2 = mock(OrganizationResponse.class);

		when(organizationRepository.findAll()).thenReturn(List.of(org1, org2));
		when(mapper.mapToOrganizationResponse(org1)).thenReturn(response1);
		when(mapper.mapToOrganizationResponse(org2)).thenReturn(response2);

		var result = service.getAllOrganizations();

		assertThat(List.of(response1, response2)).isEqualTo(result);
		verify(organizationRepository).findAll();
		verify(mapper).mapToOrganizationResponse(org1);
		verify(mapper).mapToOrganizationResponse(org2);
	}

	@Test
	void getSpecificOrg_test() {

		var service = new OrganizationService(mapper, organizationRepository);

		var org = new Organization();
		var response = mock(OrganizationResponse.class);

		when(organizationRepository.findByOrgId("orgId")).thenReturn(Optional.of(org));
		when(mapper.mapToOrganizationResponse(org)).thenReturn(response);

		var result = service.getSpecificOrg("orgId");

		assertThat(result).isEqualTo(response);
	}

	@Test
	void getOrgsByIds_test() {
		var service = new OrganizationService(mapper, organizationRepository);

		var org1 = new Organization();
		var org2 = new Organization();

		var response1 = mock(OrganizationResponse.class);
		var response2 = mock(OrganizationResponse.class);

		when(organizationRepository.findByOrgIdIn(List.of("Id1", "Id2"))).thenReturn(List.of(org1, org2));
		when(mapper.mapToOrganizationResponse(org1)).thenReturn(response1);
		when(mapper.mapToOrganizationResponse(org2)).thenReturn(response2);

		var result = service.getOrgsById(List.of("Id1", "Id2"));

		assertThat(List.of(response1, response2)).isEqualTo(result);
	}

	@Test
	void getOrgChildrenAndDescendantsWithId_test() {
		var service = new OrganizationService(mapper, organizationRepository);

		var org1 = new Organization();
		var org2 = new Organization();

		var response1 = mock(OrganizationResponse.class);
		var response2 = mock(OrganizationResponse.class);

		when(organizationRepository.findOrgWithChildrenAndDescendants("Id1")).thenReturn(List.of(org1, org2));
		when(mapper.mapToOrganizationResponse(org1)).thenReturn(response1);
		when(mapper.mapToOrganizationResponse(org2)).thenReturn(response2);

		var result = service.getOrgChildrenAndDescendantsWithId("Id1");

		assertThat(List.of(response1, response2)).isEqualTo(result);
	}

	@Test
	void getOrgAndChildrenWithId_test() {
		var service = new OrganizationService(mapper, organizationRepository);

		var org1 = new Organization();
		var org2 = new Organization();

		var response1 = mock(OrganizationResponse.class);
		var response2 = mock(OrganizationResponse.class);

		when(organizationRepository.findOrgAndChildren("Id1")).thenReturn(List.of(org1, org2));
		when(mapper.mapToOrganizationResponse(org1)).thenReturn(response1);
		when(mapper.mapToOrganizationResponse(org2)).thenReturn(response2);

		var result = service.getOrgAndChildrenWithId("Id1");

		assertThat(List.of(response1, response2)).isEqualTo(result);
	}

	@Test
	void getOrgsByIds_Null_test() {
		var service = new OrganizationService(mapper, organizationRepository);

		var exception = assertThrows(IllegalArgumentException.class, () -> service.getOrgsById(null));

		assertThat("orgid is required").isEqualTo(exception.getMessage());
		verifyNoInteractions(organizationRepository, mapper);
	}

	@Test
	void getSpecificOrg_Id_Null_test() {
		var service = new OrganizationService(mapper, organizationRepository);

		var exception = assertThrows(IllegalArgumentException.class, () -> service.getSpecificOrg(null));

		assertThat("orgid is required").isEqualTo(exception.getMessage());
		verifyNoInteractions(organizationRepository, mapper);
	}

	@Test
	void getOrgChildrenAndDescendantsWithId_nothing_found_test() {
		var service = new OrganizationService(mapper, organizationRepository);

		when(organizationRepository.findOrgWithChildrenAndDescendants("1"))
			.thenReturn(List.of());

		var exception = assertThrows(ThrowableProblem.class,
			() -> service.getOrgChildrenAndDescendantsWithId("1"));

		assertThat(exception.getMessage()).contains("No organization with id '1' could be found");
		verify(organizationRepository).findOrgWithChildrenAndDescendants("1");
		verifyNoInteractions(mapper);
	}

	@Test
	void getOrgAndChildrenWithId_nothing_found_test() {
		var service = new OrganizationService(mapper, organizationRepository);

		when(organizationRepository.findOrgAndChildren("1"))
			.thenReturn(List.of());

		var exception = assertThrows(ThrowableProblem.class,
			() -> service.getOrgAndChildrenWithId("1"));

		assertThat(exception.getMessage()).contains("No organization with id '1' could be found");
		verify(organizationRepository).findOrgAndChildren("1");
		verifyNoInteractions(mapper);
	}

	@Test
	void getOrgsById_nothing_found_test() {
		var service = new OrganizationService(mapper, organizationRepository);

		when(organizationRepository.findByOrgIdIn(List.of("1")))
			.thenReturn(List.of());

		var exception = assertThrows(ThrowableProblem.class,
			() -> service.getOrgsById(List.of("1")));

		assertThat(exception.getMessage()).contains("No organization found.");
		verify(organizationRepository).findByOrgIdIn(List.of("1"));
		verifyNoInteractions(mapper);
	}

}
