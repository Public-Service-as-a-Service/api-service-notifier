package se.sundsvall.notifier.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.sundsvall.notifier.api.model.request.EmployeeResponse;
import se.sundsvall.notifier.integration.db.entity.Employee;

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

		assertEquals("personId", response.personId());
		assertEquals("orgId", response.orgId());
		assertEquals("firstName", response.firstName());
		assertEquals("lastName", response.lastName());
		assertEquals("email@test.se", response.email());
		assertEquals("0601122333", response.workPhone());
		assertEquals("0701122333", response.workMobile());
		assertEquals("Arbetstitel", response.workTitle());
	}
}
