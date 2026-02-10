package se.sundsvall.notifier.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.notifier.api.mapper.EmployeeMapper;
import se.sundsvall.notifier.api.model.response.EmployeeWithOrgNameResponse;
import se.sundsvall.notifier.integration.db.repository.EmployeeRepository;

@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;

	public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
		this.employeeRepository = employeeRepository;
		this.employeeMapper = employeeMapper;
	}

	public List<EmployeeWithOrgNameResponse> getEmployeesByOrg(String orgId) {
		return employeeRepository.findByOrgId(orgId)
			.stream().map(employeeMapper::toResponseWithOrgName).toList();
	}

	public List<EmployeeWithOrgNameResponse> getEmployeesByOrgList(List<String> orgId) {
		if (orgId == null) {
			throw new IllegalArgumentException("org id is required");
		}

		var employees = employeeRepository.findByOrgIdIn(orgId);

		if (employees.isEmpty()) {
			throw Problem.valueOf(Status.NOT_FOUND);
		}

		return employees.stream().map(employeeMapper::toResponseWithOrgName).toList();
	}

	public List<EmployeeWithOrgNameResponse> getAllEmployees() {
		return employeeRepository.findAll().stream().map(employeeMapper::toResponseWithOrgName).toList();
	}

}
