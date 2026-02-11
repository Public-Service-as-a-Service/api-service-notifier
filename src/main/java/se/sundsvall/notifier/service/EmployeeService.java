package se.sundsvall.notifier.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.notifier.api.model.response.EmployeeWithOrgNameResponse;
import se.sundsvall.notifier.integration.db.repository.EmployeeRepository;
import se.sundsvall.notifier.service.mapper.GroupEmployeeOrganizationMapper;

@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final GroupEmployeeOrganizationMapper mapper;

	public EmployeeService(EmployeeRepository employeeRepository, GroupEmployeeOrganizationMapper mapper) {
		this.employeeRepository = employeeRepository;
		this.mapper = mapper;
	}

	public List<EmployeeWithOrgNameResponse> getEmployeesByOrg(String orgId) {
		return employeeRepository.findByOrgId(orgId)
			.stream().map(mapper::mapToEmployeeWithOrgNameResponse).toList();
	}

	public List<EmployeeWithOrgNameResponse> getEmployeesByOrgList(List<String> orgId) {
		if (orgId == null) {
			throw new IllegalArgumentException("org id is required");
		}

		var employees = employeeRepository.findByOrgIdIn(orgId);

		if (employees.isEmpty()) {
			throw Problem.valueOf(Status.NOT_FOUND);
		}

		return employees.stream().map(mapper::mapToEmployeeWithOrgNameResponse).toList();
	}

	public List<EmployeeWithOrgNameResponse> getAllEmployees() {
		return employeeRepository.findAll().stream().map(mapper::mapToEmployeeWithOrgNameResponse).toList();
	}

}
