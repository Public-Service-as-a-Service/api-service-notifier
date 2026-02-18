package se.sundsvall.notifier.integration.db.repository;

import feign.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.sundsvall.notifier.integration.db.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
	Optional<Organization> findByOrgId(String orgId);

	List<Organization> findByOrgIdIn(List<String> orgId);

	@Query(value = """
		WITH RECURSIVE org_tree AS (
		    SELECT o.*
		    FROM organization o
		    WHERE o.org_id = :orgId

		    UNION ALL

		    SELECT child.*
		    FROM organization child
		    JOIN org_tree t
		      ON child.parent_org_id = t.org_id
		)
		SELECT * FROM org_tree
		""", nativeQuery = true)
	List<Organization> findOrgWithChildrenAndDescendants(@Param("orgId") String orgId);

	@Query(value = """
		SELECT o.*
		FROM organization o
		WHERE o.org_id = :orgId

		UNION ALL

		SELECT child.*
		FROM organization child
		WHERE child.parent_org_id = :orgId
		""", nativeQuery = true)
	List<Organization> findOrgAndChildren(@Param("orgId") String orgId);

	@Query(value = """
		SELECT child.*
		FROM organization child
		WHERE child.parent_org_id = :orgId
		""", nativeQuery = true)
	List<Organization> findChildren(@Param("orgId") String orgId);
}
