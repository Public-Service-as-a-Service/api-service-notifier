package se.sundsvall.notifier.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.notifier.api.mapper.EmployeeMapper;
import se.sundsvall.notifier.api.model.response.EmployeeResponse;
import se.sundsvall.notifier.integration.repository.EmployeeRepository;

@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;

	public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
		this.employeeRepository = employeeRepository;
		this.employeeMapper = employeeMapper;
	}

	public List<EmployeeResponse> getEmployeesByOrg(String orgId) {
		if (orgId == null || orgId.isBlank()) {
			throw new IllegalArgumentException("org id is required");
		}

		return employeeRepository.findByOrgId(orgId)
			.stream().map(employeeMapper::toResponse).toList();
	}

	public List<EmployeeResponse> getEmployeesByOrgList(List<String> orgId) {
		if (orgId == null) {
			throw new IllegalArgumentException("org id is required");
		}
		var result = employeeRepository.findByOrgIdIn(orgId)
			.stream().map(employeeMapper::toResponse).toList();

		if (result.isEmpty()) {
			throw Problem.valueOf(Status.NOT_FOUND);
		}

		return result;
	}

	public List<EmployeeResponse> getAllEmployees() {
		return employeeRepository.findAll().stream().map(employeeMapper::toResponse).toList();
	}

}
