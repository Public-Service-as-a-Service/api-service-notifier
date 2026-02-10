package se.sundsvall.notifier.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import se.sundsvall.notifier.api.model.response.EmployeeResponse;
import se.sundsvall.notifier.api.model.response.EmployeeWithOrgNameResponse;
import se.sundsvall.notifier.integration.db.entity.Employee;
import se.sundsvall.notifier.integration.db.entity.Organization;

public class EmployeeMapperTest {

	@Test
	void employeeMapper_Test() {
		var employeeMapper = new EmployeeMapper();

		var employee = new Employee();

		employee.setPersonId("personId");
		employee.setOrgId("orgId");
		employee.setFirstName("firstName");
		employee.setLastName("lastName");
		employee.setEmail("email@test.se");
		employee.setWorkPhone("0601122333");
		employee.setWorkMobile("0701122333");
		employee.setWorkTitle("Arbetstitel");

		EmployeeResponse response = employeeMapper.toResponse(employee);

		assertThat("personId").isEqualTo(response.personId());
		assertThat("orgId").isEqualTo(response.orgId());
		assertThat("firstName").isEqualTo(response.firstName());
		assertThat("lastName").isEqualTo(response.lastName());
		assertThat("email@test.se").isEqualTo(response.email());
		assertThat("0601122333").isEqualTo(response.workPhone());
		assertThat("0701122333").isEqualTo(response.workMobile());
		assertThat("Arbetstitel").isEqualTo(response.workTitle());
	}

	@Test
	void employeeWithOrgNameMapper_Test() {
		var employeeMapper = new EmployeeMapper();

		var employee = new Employee();
		var organization = new Organization();
		var orgName = organization.getName();

		employee.setId(1L);
		employee.setPersonId("personId");
		employee.setOrgId("orgId");
		employee.setFirstName("firstName");
		employee.setLastName("lastName");
		employee.setEmail("email@test.se");
		employee.setWorkPhone("0601122333");
		employee.setWorkMobile("0701122333");
		employee.setWorkTitle("Arbetstitel");
		employee.setOrganization(organization);

		EmployeeWithOrgNameResponse response = employeeMapper.toResponseWithOrgName(employee);

		assertThat(1L).isEqualTo(response.id());
		assertThat("personId").isEqualTo(response.personId());
		assertThat("orgId").isEqualTo(response.orgId());
		assertThat("firstName").isEqualTo(response.firstName());
		assertThat("lastName").isEqualTo(response.lastName());
		assertThat("email@test.se").isEqualTo(response.email());
		assertThat("0601122333").isEqualTo(response.workPhone());
		assertThat("0701122333").isEqualTo(response.workMobile());
		assertThat("Arbetstitel").isEqualTo(response.workTitle());
		assertThat(orgName).isEqualTo(response.orgName());
	}
}
