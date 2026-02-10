package se.sundsvall.notifier.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Test;
import se.sundsvall.notifier.api.model.response.EmployeeResponse;
import se.sundsvall.notifier.integration.db.entity.Employee;
import se.sundsvall.notifier.integration.db.entity.Group;

public class EntityToResponseMapperTest {

	private final EntityToResponseMapper mapper = new EntityToResponseMapper();

	@Test
	void mapToEmployeeResponse() {
		var employee = new Employee();
		employee.setPersonId("p1");
		employee.setOrgId("org1");
		employee.setFirstName("firstTest");
		employee.setLastName("lastTest");
		employee.setEmail("test@test.se");
		employee.setWorkMobile("0701234567");
		employee.setWorkPhone("0601234567");
		employee.setWorkTitle("testPerson");

		EmployeeResponse response = mapper.mapToEmployeeResponse(employee);

		assertThat(response.personId()).isEqualTo("p1");
		assertThat(response.orgId()).isEqualTo("org1");
		assertThat(response.firstName()).isEqualTo("firstTest");
		assertThat(response.lastName()).isEqualTo("lastTest");
		assertThat(response.email()).isEqualTo("test@test.se");
		assertThat(response.workMobile()).isEqualTo("0701234567");
		assertThat(response.workPhone()).isEqualTo("0601234567");
		assertThat(response.workTitle()).isEqualTo("testPerson");

	}

	@Test
	void mapToGroupResponse_withEmployee() {
		var createdAt = LocalDateTime.of(2025, 2, 1, 20, 15);

		var group = Group.builder()
			.withId(1L)
			.withName("testGroup")
			.withDescription("testDescription")
			.withCreatorId("creatorId")
			.withCreatedAt(createdAt)
			.build();

		var emp1 = new Employee();
		emp1.setPersonId("p1");
		emp1.setOrgId("org1");
		emp1.setFirstName("firstName1");
		emp1.setLastName("lastName1");
		emp1.setEmail("test1@test.se");
		emp1.setWorkMobile("0701234567");
		emp1.setWorkPhone("0601234567");
		emp1.setWorkTitle("t1");

		group.setEmployees(Set.of(emp1));

		var response = mapper.mapToGroupResponse(group);

		assertThat(response.employees()).hasSize(1);
		var mapped = response.employees().iterator().next();
		assertThat(mapped.personId()).isEqualTo("p1");
		assertThat(mapped.orgId()).isEqualTo("org1");
	}

	@Test
	void mapToGroupResponse_withoutEmployees() {
		var group = Group.builder()
			.withId(1L)
			.withName("testGroup")
			.withDescription("testDescription")
			.withCreatorId("creatorId")
			.withCreatedAt(LocalDateTime.of(2025, 2, 1, 20, 37))
			.withEmployees(Set.of())
			.build();

		var response = mapper.mapToGroupResponse(group);

		assertThat(response.employees())
			.isNotNull()
			.isEmpty();
	}
}
