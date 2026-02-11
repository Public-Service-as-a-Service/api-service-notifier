package se.sundsvall.notifier.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.notifier.api.mapper.OrganizationMapper;
import se.sundsvall.notifier.api.model.response.OrganizationResponse;
import se.sundsvall.notifier.integration.db.repository.OrganizationRepository;

@Service
public class OrganizationService {

	private final OrganizationMapper organizationMapper;
	private final OrganizationRepository organizationRepository;

	public OrganizationService(OrganizationMapper organizationMapper, OrganizationRepository organizationRepository) {
		this.organizationMapper = organizationMapper;
		this.organizationRepository = organizationRepository;
	}

	public List<OrganizationResponse> getAllOrganizations() {
		return organizationRepository.findAll()
			.stream().map(organizationMapper::toResponse).toList();
	}

	public OrganizationResponse getSpecificOrg(String orgId) {
		if (orgId == null) {
			throw new IllegalArgumentException("orgid is required");
		}

		return organizationRepository.findByOrgId(orgId).map(organizationMapper::toResponse)
			.orElseThrow(() -> Problem.valueOf(Status.NOT_FOUND, "orgnanization with id '%s' not found".formatted(orgId)));
	}

	public List<OrganizationResponse> getOrgsById(List<String> orgId) {
		if (orgId == null || orgId.isEmpty()) {
			throw new IllegalArgumentException("orgid is required");
		}
		var result = organizationRepository.findByOrgIdIn(orgId).stream().map(organizationMapper::toResponse).toList();

		if (result.isEmpty()) {
			throw Problem.valueOf(Status.NOT_FOUND, "No organization found.");
		}
		return result;
	}

	public List<OrganizationResponse> getOrgChildrenAndDescendantsWithId(String orgId) {
		var result = organizationRepository.findOrgWithChildrenAndDescendants(orgId).stream().map(organizationMapper::toResponse).toList();

		if (result.isEmpty()) {
			throw Problem.valueOf(Status.NOT_FOUND, "No organization with id '%s' could be found".formatted(orgId));
		}

		return result;
	}

	public List<OrganizationResponse> getOrgAndChildrenWithId(String orgId) {
		var result = organizationRepository.findOrgAndChildren(orgId).stream().map(organizationMapper::toResponse).toList();

		if (result.isEmpty()) {
			throw Problem.valueOf(Status.NOT_FOUND, "No organization with id '%s' could be found".formatted(orgId));
		}

		return result;
	}
}
