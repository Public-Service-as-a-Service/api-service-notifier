package se.sundsvall.notifier.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

		assertEquals("CompanyId", response.companyId());
		assertEquals("parentOrgId", response.parentOrgId());
		assertEquals("orgId", response.orgId());
		assertEquals("orgName", response.name());
		assertEquals(2, response.treeLevel());
	}
}
