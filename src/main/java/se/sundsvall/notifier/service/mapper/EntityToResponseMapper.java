package se.sundsvall.notifier.service.mapper;

import java.util.HashSet;
import java.util.Set;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import se.sundsvall.notifier.api.model.response.EmployeeManagerResponse;
import se.sundsvall.notifier.api.model.response.EmployeeResponse;
import se.sundsvall.notifier.api.model.response.EmployeeWithOrgNameResponse;
import se.sundsvall.notifier.api.model.response.GroupResponse;
import se.sundsvall.notifier.api.model.response.OrganizationResponse;
import se.sundsvall.notifier.integration.db.entity.Employee;
import se.sundsvall.notifier.integration.db.entity.Group;
import se.sundsvall.notifier.integration.db.entity.Organization;

@Component
@NoArgsConstructor
public class EntityToResponseMapper {

	public GroupResponse mapToGroupResponse(Group group) {
		Set<Employee> employees = group.getEmployees();
		Set<EmployeeResponse> response = new HashSet<>();

		for (Employee employee : employees) {
			response.add(mapToEmployeeResponse(employee));
		}

		return GroupResponse.builder()
			.withId(group.getId())
			.withName(group.getName())
			.withDescription(group.getDescription())
			.withCreatorId(group.getCreatorId())
			.withCreatedAt(group.getCreatedAt())
			.withEmployees(response)
			.build();

	}

	public EmployeeResponse mapToEmployeeResponse(Employee employee) {
		return EmployeeResponse.builder()
			.withId(employee.getId())
			.withPersonId(employee.getPersonId())
			.withOrgId(employee.getOrgId())
			.withFirstName(employee.getFirstName())
			.withLastName(employee.getLastName())
			.withEmail(employee.getEmail())
			.withWorkMobile(employee.getWorkMobile())
			.withWorkPhone(employee.getWorkPhone())
			.withWorkTitle(employee.getWorkTitle())
			.build();
	}

	public EmployeeManagerResponse mapToEmployeeManagerResponse(Employee employee) {
		return EmployeeManagerResponse.builder()
			.withId(employee.getId())
			.withPersonId(employee.getPersonId())
			.withOrgId(employee.getOrgId())
			.withFirstName(employee.getFirstName())
			.withLastName(employee.getLastName())
			.withEmail(employee.getEmail())
			.withWorkMobile(employee.getWorkMobile())
			.withWorkPhone(employee.getWorkPhone())
			.withWorkTitle(employee.getWorkTitle())
			.withManagerCode(employee.getManagerCode())
			.build();
	}

	public EmployeeWithOrgNameResponse mapToEmployeeWithOrgNameResponse(Employee employee) {
		var orgName = employee.getOrganization().getName();
		return EmployeeWithOrgNameResponse.builder()
			.withId(employee.getId())
			.withPersonId(employee.getPersonId())
			.withOrgId(employee.getOrgId())
			.withFirstName(employee.getFirstName())
			.withLastName(employee.getLastName())
			.withEmail(employee.getEmail())
			.withWorkPhone(employee.getWorkPhone())
			.withWorkMobile(employee.getWorkMobile())
			.withWorkTitle(employee.getWorkTitle())
			.withOrgName(orgName)
			.build();
	}

	public OrganizationResponse mapToOrganizationResponse(Organization organization) {
		return OrganizationResponse.builder()
			.withCompanyId(organization.getCompanyId())
			.withParentOrgId(organization.getParentOrgId())
			.withOrgId(organization.getOrgId())
			.withName(organization.getName())
			.withTreeLevel(organization.getTreeLevel())
			.build();
	}
}
