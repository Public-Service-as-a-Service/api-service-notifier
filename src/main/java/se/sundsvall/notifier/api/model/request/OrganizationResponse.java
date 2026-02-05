package se.sundsvall.notifier.api.model.request;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record OrganizationResponse(
	String companyId,
	String parentOrgId,
	String orgId,
	String name,
	int treeLevel) {}
