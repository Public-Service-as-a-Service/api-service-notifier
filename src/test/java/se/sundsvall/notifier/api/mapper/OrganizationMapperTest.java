package se.sundsvall.notifier.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import se.sundsvall.notifier.api.model.response.OrganizationResponse;
import se.sundsvall.notifier.integration.db.entity.Organization;

public class OrganizationMapperTest {

	@Test
	void toResponse_test() {
		var organisationMapper = new OrganizationMapper();

		var org = new Organization();

		org.setCompanyId("CompanyId");
		org.setParentOrgId("parentOrgId");
		org.setOrgId("orgId");
		org.setName("orgName");
		org.setTreeLevel(2);

		OrganizationResponse response = organisationMapper.toResponse(org);

		assertThat("CompanyId").isEqualTo(response.companyId());
		assertThat("parentOrgId").isEqualTo(response.parentOrgId());
		assertThat("orgId").isEqualTo(response.orgId());
		assertThat("orgName").isEqualTo(response.name());
		assertThat(2).isEqualTo(response.treeLevel());
	}
}
