package se.sundsvall.notifier.integration.db.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.sundsvall.notifier.integration.db.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Set<Employee> findAllByIdIn(Set<Long> ids);

	List<Employee> findByOrgId(String orgId);

	List<Employee> findByOrgIdIn(List<String> orgId);

	Page<Employee> findByFirstNameStartingWithOrLastNameStartingWith(String firstName, String lastName, Pageable page);

	List<Employee> findAllByManagerCodeIsNotNull();
}
