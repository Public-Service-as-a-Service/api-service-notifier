package se.sundsvall.notifier.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.notifier.api.model.response.OrganizationResponse;
import se.sundsvall.notifier.integration.db.repository.OrganizationRepository;
import se.sundsvall.notifier.service.mapper.EntityToResponseMapper;

@Service
public class OrganizationService {

	private final EntityToResponseMapper mapper;
	private final OrganizationRepository organizationRepository;

	public OrganizationService(EntityToResponseMapper mapper, OrganizationRepository organizationRepository) {
		this.mapper = mapper;
		this.organizationRepository = organizationRepository;
	}

	public List<OrganizationResponse> getAllOrganizations() {
		return organizationRepository.findAll()
			.stream().map(mapper::mapToOrganizationResponse).toList();
	}

	public OrganizationResponse getSpecificOrg(String orgId) {
		if (orgId == null) {
			throw new IllegalArgumentException("orgid is required");
		}

		return organizationRepository.findByOrgId(orgId).map(mapper::mapToOrganizationResponse)
			.orElseThrow(() -> Problem.valueOf(Status.NOT_FOUND, "orgnanization with id '%s' not found".formatted(orgId)));
	}

	public List<OrganizationResponse> getOrgsById(List<String> orgId) {
		if (orgId == null || orgId.isEmpty()) {
			throw new IllegalArgumentException("orgid is required");
		}
		var result = organizationRepository.findByOrgIdIn(orgId).stream().map(mapper::mapToOrganizationResponse).toList();

		if (result.isEmpty()) {
			throw Problem.valueOf(Status.NOT_FOUND, "No organization found.");
		}
		return result;
	}

	public List<OrganizationResponse> getOrgChildrenAndDescendantsWithId(String orgId) {
		var result = organizationRepository.findOrgWithChildrenAndDescendants(orgId).stream().map(mapper::mapToOrganizationResponse).toList();

		if (result.isEmpty()) {
			throw Problem.valueOf(Status.NOT_FOUND, "No organization with id '%s' could be found".formatted(orgId));
		}

		return result;
	}

	public List<OrganizationResponse> getOrgAndChildrenWithId(String orgId) {
		var result = organizationRepository.findOrgAndChildren(orgId).stream().map(mapper::mapToOrganizationResponse).toList();

		if (result.isEmpty()) {
			throw Problem.valueOf(Status.NOT_FOUND, "No organization with id '%s' could be found".formatted(orgId));
		}

		return result;
	}
}
