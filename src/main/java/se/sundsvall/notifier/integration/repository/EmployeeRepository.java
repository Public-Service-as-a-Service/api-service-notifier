package se.sundsvall.notifier.integration.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.sundsvall.notifier.integration.db.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findByOrgId(String orgId);

	List<Employee> findByOrgIdIn(List<String> orgId);
}
