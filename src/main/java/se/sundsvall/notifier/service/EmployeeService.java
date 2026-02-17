package se.sundsvall.notifier.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	public Page<EmployeeWithOrgNameResponse> getEmployeesWithSearch(String search, Pageable pageable) {

		String[] terms = search.trim().toLowerCase().split("\\s+");
		String searchTerm1 = terms[0];
		String searchTerm2 = null;
		if (terms.length > 1) {
			searchTerm2 = terms[1];
		}
		if (pageable.getSort().isUnsorted()) {
			pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by("firstName").ascending().and(
					Sort.by("lastName").ascending()));
		}
		return employeeRepository.findByFirstNameStartingWithOrLastNameStartingWithOrWorkTitleStartingWith(searchTerm1, searchTerm2, pageable).map(mapper::mapToEmployeeWithOrgNameResponse);
	}

	public List<EmployeeManagerResponse> getAllEmployeeManagers() {
		return employeeRepository.findAllByManagerCodeIsNotNull().stream().map(mapper::mapToEmployeeManagerResponse).toList();
	}

}
