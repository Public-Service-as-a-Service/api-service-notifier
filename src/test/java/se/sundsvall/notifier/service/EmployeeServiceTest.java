package se.sundsvall.notifier.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.notifier.api.mapper.EmployeeMapper;
import se.sundsvall.notifier.api.model.request.EmployeeResponse;
import se.sundsvall.notifier.integration.db.entity.Employee;
import se.sundsvall.notifier.integration.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

	@Mock
	EmployeeMapper employeeMapper;

	@Mock
	EmployeeRepository employeeRepository;

	@Test
	void byOrg_test() {
		var service = new EmployeeService(employeeRepository, employeeMapper);

		var employee1 = new Employee();
		var employee2 = new Employee();
		var response1 = mock(EmployeeResponse.class);
		var response2 = mock(EmployeeResponse.class);

		when(employeeRepository.findByOrgId("Id")).thenReturn(List.of(employee1, employee2));
		when(employeeMapper.toResponse(employee1)).thenReturn(response1);
		when(employeeMapper.toResponse(employee2)).thenReturn(response2);

		var result = service.getEmployeesByOrg("Id");

		assertEquals(List.of(response1, response2), result);
	}

	@Test
	void byOrgList_test() {
		var service = new EmployeeService(employeeRepository, employeeMapper);

		var employee1 = new Employee();
		var employee2 = new Employee();
		var response1 = mock(EmployeeResponse.class);
		var response2 = mock(EmployeeResponse.class);

		when(employeeRepository.findByOrgIdIn(List.of("Id1", "Id2"))).thenReturn(List.of(employee1, employee2));
		when(employeeMapper.toResponse(employee1)).thenReturn(response1);
		when(employeeMapper.toResponse(employee2)).thenReturn(response2);

		var result = service.getEmployeesByOrgList(List.of("Id1", "Id2"));

		assertEquals(List.of(response1, response2), result);
	}

	@Test
	void getAll_test() {
		var service = new EmployeeService(employeeRepository, employeeMapper);

		var employee1 = new Employee();
		var employee2 = new Employee();
		var response1 = mock(EmployeeResponse.class);
		var response2 = mock(EmployeeResponse.class);

		when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));
		when(employeeMapper.toResponse(employee1)).thenReturn(response1);
		when(employeeMapper.toResponse(employee2)).thenReturn(response2);

		var result = service.getAllEmployees();

		assertEquals(List.of(response1, response2), result);
	}

	@Test
	void byOrg_null_test() {
		var service = new EmployeeService(employeeRepository, employeeMapper);

		var exception = assertThrows(IllegalArgumentException.class, () -> service.getEmployeesByOrg(null));

		assertEquals("org id is required", exception.getMessage());
		verifyNoInteractions(employeeRepository, employeeMapper);
	}
}
