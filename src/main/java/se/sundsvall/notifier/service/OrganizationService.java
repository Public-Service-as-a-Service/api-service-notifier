package se.sundsvall.notifier.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.sundsvall.dept44.problem.Problem;
import se.sundsvall.notifier.api.model.response.OrganizationResponse;
import se.sundsvall.notifier.integration.db.entity.Organization;
import se.sundsvall.notifier.integration.db.repository.OrganizationRepository;
import se.sundsvall.notifier.service.mapper.EntityToResponseMapper;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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
			.orElseThrow(() -> Problem.valueOf(NOT_FOUND, "orgnanization with id '%s' not found".formatted(orgId)));
	}

	public List<OrganizationResponse> getOrgsById(List<String> orgId) {
		if (orgId == null || orgId.isEmpty()) {
			throw new IllegalArgumentException("orgid is required");
		}
		var result = organizationRepository.findByOrgIdIn(orgId).stream().map(mapper::mapToOrganizationResponse).toList();

		if (result.isEmpty()) {
			throw Problem.valueOf(NOT_FOUND, "No organization found.");
		}
		return result;
	}

	public List<OrganizationResponse> getOrgChildrenAndDescendantsWithId(String orgId) {
		var result = organizationRepository.findOrgWithChildrenAndDescendants(orgId).stream().map(mapper::mapToOrganizationResponse).toList();

		if (result.isEmpty()) {
			throw Problem.valueOf(NOT_FOUND, "No organization with id '%s' could be found".formatted(orgId));
		}

		return result;
	}

	public List<OrganizationResponse> getOrgAndChildrenWithId(String orgId) {
		var result = organizationRepository.findOrgAndChildren(orgId).stream().map(mapper::mapToOrganizationResponse).toList();

		if (result.isEmpty()) {
			throw Problem.valueOf(NOT_FOUND, "No organization with id '%s' could be found".formatted(orgId));
		}

		return result;
	}

	public Page<OrganizationResponse> getOrganizationWithSearch(String search, Pageable pageable) {
		String searchLowercase = search.trim().toLowerCase();
		Page<Organization> searchResult = organizationRepository.findByNameContaining(searchLowercase, pageable);
		return searchResult.map(mapper::mapToOrganizationResponse);
	}

	public List<OrganizationResponse> getChildrenReplaceDuplicateDescendantsWithRoot(String orgId) {
		List<Organization> directChildren = organizationRepository.findChildren(orgId);

		if (directChildren.isEmpty()) {
			throw Problem.valueOf(NOT_FOUND, "No children for organization with id '%s' could be found".formatted(orgId));
		}

		List<OrganizationResponse> response = new ArrayList<>();

		for (Organization topChild : directChildren) {
			Organization bottomDuplicateChild = findBottomDuplicateChild(topChild);

			OrganizationResponse bottomResponse = mapper.mapToOrganizationResponse(bottomDuplicateChild);
			if (!bottomDuplicateChild.getOrgId().equals(topChild.getOrgId())) {
				bottomResponse = bottomResponse.toBuilder()
					.withParentOrgId(orgId)
					.withTreeLevel(topChild.getTreeLevel())
					.build();
			}
			response.add(bottomResponse);
		}
		return response;
	}

	private Organization findBottomDuplicateChild(Organization first) {
		Organization current = first;

		while (true) {
			List<Organization> children = organizationRepository.findChildren(current.getOrgId());

			String currentName = current.getName();

			Optional<Organization> nextDuplicate = children.stream()
				.filter(child -> Objects.equals(child.getName(), currentName))
				.findAny();

			if (nextDuplicate.isEmpty()) {
				return current;
			}

			current = nextDuplicate.get();
		}
	}
}
