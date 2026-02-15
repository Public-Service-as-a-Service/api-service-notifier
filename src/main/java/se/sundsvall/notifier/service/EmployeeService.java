package se.sundsvall.notifier.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.sundsvall.notifier.api.model.response.EmployeeManagerResponse;
import se.sundsvall.notifier.api.model.response.EmployeeWithOrgNameResponse;
import se.sundsvall.notifier.integration.db.repository.EmployeeRepository;
import se.sundsvall.notifier.service.mapper.EntityToResponseMapper;

@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final EntityToResponseMapper mapper;

	public EmployeeService(EmployeeRepository employeeRepository, EntityToResponseMapper mapper) {
		this.employeeRepository = employeeRepository;
		this.mapper = mapper;
	}

	public List<EmployeeWithOrgNameResponse> getEmployeesByOrg(String orgId) {
		return employeeRepository.findByOrgId(orgId).stream().map(mapper::mapToEmployeeWithOrgNameResponse).toList();
	}

	public List<EmployeeWithOrgNameResponse> getEmployeesByOrgList(List<String> orgId) {
		if (orgId == null) {
			throw new IllegalArgumentException("org id is required");
		}

		return employeeRepository.findByOrgIdIn(orgId).stream().map(mapper::mapToEmployeeWithOrgNameResponse).toList();
	}

	public List<EmployeeWithOrgNameResponse> getAllEmployees() {
		return employeeRepository.findAll().stream().map(mapper::mapToEmployeeWithOrgNameResponse).toList();
	}

	public Page<EmployeeWithOrgNameResponse> getEmployeesWithSearch(String search, Pageable page) {
		return employeeRepository.findByFirstNameStartingWithOrLastNameStartingWith(search.toLowerCase(), search.toLowerCase(), page).map(mapper::mapToEmployeeWithOrgNameResponse);
	}

	public List<EmployeeManagerResponse> getAllEmployeeManagers() {
		return employeeRepository.findAllByManagerCodeIsNotNull().stream().map(mapper::mapToEmployeeManagerResponse).toList();
	}

}
