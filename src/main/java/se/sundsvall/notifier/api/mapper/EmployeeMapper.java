package se.sundsvall.notifier.api.mapper;

import org.springframework.stereotype.Component;
import se.sundsvall.notifier.api.model.response.EmployeeResponse;
import se.sundsvall.notifier.api.model.response.EmployeeWithOrgNameResponse;
import se.sundsvall.notifier.integration.db.entity.Employee;

@Component
public class EmployeeMapper {

	public EmployeeResponse toResponse(Employee employee) {
		return EmployeeResponse.builder()
			.withPersonId(employee.getPersonId())
			.withOrgId(employee.getOrgId())
			.withFirstName(employee.getFirstName())
			.withLastName(employee.getLastName())
			.withEmail(employee.getEmail())
			.withWorkPhone(employee.getWorkPhone())
			.withWorkMobile(employee.getWorkMobile())
			.withWorkTitle(employee.getWorkTitle())
			.build();
	}

	public EmployeeWithOrgNameResponse toResponseWithOrgName(Employee employee) {
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
}
