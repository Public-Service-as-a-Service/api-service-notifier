package se.sundsvall.notifier.integration.db.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.sundsvall.notifier.integration.db.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Set<Employee> findAllByIdIn(Set<Long> ids);

	List<Employee> findByOrgId(String orgId);

	List<Employee> findByOrgIdIn(List<String> orgId);

	@Query("""
		select e
		from Employee e where (
		:s2 is null
			and (
				e.firstName like concat(:s1,'%')
				or e.lastName like concat(:s1,'%')
				or e.workTitle like concat(:s1,'%')
				)
		or
			:s2 is not null
				and
					e.firstName like concat(:s1,'%') and e.lastName like concat(:s2,'%')
					or e.lastName like concat(:s1,'%') and e.firstName like concat(:s2,'%')
					or e.firstName like concat(:s1,'%') and e.workTitle like concat(:s2,'%')
					or e.workTitle like concat(:s1,'%') and e.firstName like concat(:s2,'%')
		)""")
	Page<Employee> findByFirstNameStartingWithOrLastNameStartingWithOrWorkTitleStartingWith(@Param("s1") String searchTerm1, @Param("s2") String searchTerm2, Pageable page);

	List<Employee> findAllByManagerCodeIsNotNull();
}
