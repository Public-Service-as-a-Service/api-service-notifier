package se.sundsvall.notifier.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.notifier.api.model.response.EmployeeWithOrgNameResponse;
import se.sundsvall.notifier.integration.db.entity.Employee;
import se.sundsvall.notifier.integration.db.repository.EmployeeRepository;
import se.sundsvall.notifier.service.mapper.GroupEmployeeOrganizationMapper;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

	@Mock
	private GroupEmployeeOrganizationMapper mapper;

	@Mock
	private EmployeeRepository employeeRepository;

	@Test
	void byOrg_test() {
		var service = new EmployeeService(employeeRepository, mapper);

		var employee1 = new Employee();
		var employee2 = new Employee();
		var response1 = mock(EmployeeWithOrgNameResponse.class);
		var response2 = mock(EmployeeWithOrgNameResponse.class);

		when(employeeRepository.findByOrgId("Id")).thenReturn(List.of(employee1, employee2));
		when(mapper.mapToEmployeeWithOrgNameResponse(employee1)).thenReturn(response1);
		when(mapper.mapToEmployeeWithOrgNameResponse(employee2)).thenReturn(response2);

		var result = service.getEmployeesByOrg("Id");

		assertThat(List.of(response1, response2)).isEqualTo(result);
	}

	@Test
	void byOrgList_test() {
		var service = new EmployeeService(employeeRepository, mapper);

		var employee1 = new Employee();
		var employee2 = new Employee();
		var response1 = mock(EmployeeWithOrgNameResponse.class);
		var response2 = mock(EmployeeWithOrgNameResponse.class);

		when(employeeRepository.findByOrgIdIn(List.of("Id1", "Id2"))).thenReturn(List.of(employee1, employee2));
		when(mapper.mapToEmployeeWithOrgNameResponse(employee1)).thenReturn(response1);
		when(mapper.mapToEmployeeWithOrgNameResponse(employee2)).thenReturn(response2);

		var result = service.getEmployeesByOrgList(List.of("Id1", "Id2"));

		assertThat(List.of(response1, response2)).isEqualTo(result);
	}

	@Test
	void getAll_test() {
		var service = new EmployeeService(employeeRepository, mapper);

		var employee1 = new Employee();
		var employee2 = new Employee();
		var response1 = mock(EmployeeWithOrgNameResponse.class);
		var response2 = mock(EmployeeWithOrgNameResponse.class);

		when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));
		when(mapper.mapToEmployeeWithOrgNameResponse(employee1)).thenReturn(response1);
		when(mapper.mapToEmployeeWithOrgNameResponse(employee2)).thenReturn(response2);

		var result = service.getAllEmployees();

		assertThat(List.of(response1, response2)).isEqualTo(result);
	}

	@Test
	void getEmployee_Org_Id_Null_test() {
		var service = new EmployeeService(employeeRepository, mapper);

		var exception = assertThrows(IllegalArgumentException.class, () -> service.getEmployeesByOrgList(null));

		assertThat("org id is required").isEqualTo(exception.getMessage());
		verifyNoInteractions(employeeRepository, mapper);
	}

}
