package se.sundsvall.notifier.api.model.response;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record OrganizationResponse(
	String companyId,
	String parentOrgId,
	String orgId,
	String name,
	int treeLevel) {}
