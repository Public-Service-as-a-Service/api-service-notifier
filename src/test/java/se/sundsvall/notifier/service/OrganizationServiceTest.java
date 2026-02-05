package se.sundsvall.notifier.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.notifier.api.mapper.OrganizationMapper;
import se.sundsvall.notifier.api.model.request.OrganizationResponse;
import se.sundsvall.notifier.integration.db.entity.Organization;
import se.sundsvall.notifier.integration.repository.OrganizationRepository;

@ExtendWith(MockitoExtension.class)
public class OrganizationServiceTest {
	@Mock
	OrganizationMapper organizationMapper;

	@Mock
	OrganizationRepository organizationRepository;

	@Test
	void getAllOrganizations() {
		var service = new OrganizationService(organizationMapper, organizationRepository);

		var org1 = new Organization();
		var org2 = new Organization();

		var response1 = mock(OrganizationResponse.class);
		var response2 = mock(OrganizationResponse.class);

		when(organizationRepository.findAll()).thenReturn(List.of(org1, org2));
		when(organizationMapper.toResponse(org1)).thenReturn(response1);
		when(organizationMapper.toResponse(org2)).thenReturn(response2);

		var result = service.getAllOrganizations();

		assertEquals(List.of(response1, response2), result);
		verify(organizationRepository).findAll();
		verify(organizationMapper).toResponse(org1);
		verify(organizationMapper).toResponse(org2);
	}

	@Test
	void getSpecificOrg_test() {

		var service = new OrganizationService(organizationMapper, organizationRepository);

		var org = new Organization();
		var response = mock(OrganizationResponse.class);

		when(organizationRepository.findByOrgId("orgId")).thenReturn(Optional.of(org));
		when(organizationMapper.toResponse(org)).thenReturn(response);

		var result = service.getSpecificOrg("orgId");

		assertEquals(response, result);
	}

	@Test
	void getOrgsByIds_test() {
		var service = new OrganizationService(organizationMapper, organizationRepository);

		var org1 = new Organization();
		var org2 = new Organization();

		var response1 = mock(OrganizationResponse.class);
		var response2 = mock(OrganizationResponse.class);

		when(organizationRepository.findByOrgIdIn(List.of("Id1", "Id2"))).thenReturn(List.of(org1, org2));
		when(organizationMapper.toResponse(org1)).thenReturn(response1);
		when(organizationMapper.toResponse(org2)).thenReturn(response2);

		var result = service.getOrgsById(List.of("Id1", "Id2"));

		assertEquals(List.of(response1, response2), result);
	}

	@Test
	void getOrgAndChildrenWithId_test() {
		var service = new OrganizationService(organizationMapper, organizationRepository);

		var org1 = new Organization();
		var org2 = new Organization();

		var response1 = mock(OrganizationResponse.class);
		var response2 = mock(OrganizationResponse.class);

		when(organizationRepository.findOrgWithChildren("Id1")).thenReturn(List.of(org1, org2));
		when(organizationMapper.toResponse(org1)).thenReturn(response1);
		when(organizationMapper.toResponse(org2)).thenReturn(response2);

		var result = service.getOrgAndChildrenWithId("Id1");

		assertEquals(List.of(response1, response2), result);
	}

	@Test
	void getOrgsByIds_Null_test() {
		var service = new OrganizationService(organizationMapper, organizationRepository);

		var exception = assertThrows(IllegalArgumentException.class, () -> service.getOrgsById(null));

		assertEquals("orgid is required", exception.getMessage());
		verifyNoInteractions(organizationRepository, organizationMapper);
	}
}
