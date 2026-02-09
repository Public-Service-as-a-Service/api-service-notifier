package se.sundsvall.notifier.api.mapper;

import org.springframework.stereotype.Component;
import se.sundsvall.notifier.api.model.response.OrganizationResponse;
import se.sundsvall.notifier.integration.db.entity.Organization;

@Component
public class OrganizationMapper {

	public OrganizationResponse toResponse(Organization organization) {
		return OrganizationResponse.builder()
			.withCompanyId(organization.getCompanyId())
			.withParentOrgId(organization.getParentOrgId())
			.withOrgId(organization.getOrgId())
			.withName(organization.getName())
			.withTreeLevel(organization.getTreeLevel())
			.build();
	}
}
