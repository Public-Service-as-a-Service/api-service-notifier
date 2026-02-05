package se.sundsvall.notifier.integration.db;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToStringExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.AllOf.allOf;

import com.google.code.beanmatchers.BeanMatchers;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.sundsvall.notifier.integration.db.entity.Employee;
import se.sundsvall.notifier.integration.db.entity.Member;
import se.sundsvall.notifier.integration.db.entity.UserGroup;

class MemberEntityTest {

	@BeforeAll
	static void setUp() {
		BeanMatchers.registerValueGenerator(
			() -> LocalDateTime.now().minusDays(1),
			LocalDateTime.class);
	}

	@Test
	void testBean() {
		org.hamcrest.MatcherAssert.assertThat(Member.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanEqualsFor("id"),
			hasValidBeanHashCodeFor("id"),
			hasValidBeanToStringExcluding("employee", "group")));
	}

	@Test
	void withGroup_null() {
		Member member = new Member();
		member.withGroup(null);
		assertThat(member.getGroup()).isNull();
	}

	@Test
	void withEmployeeAndGroup_setsBothAndSyncsBothIds() {
		var employee = new Employee();
		employee.setId(1L);

		var group = new UserGroup();
		group.setId(2L);

		var member = new Member();

		member.withEmployeeAndGroup(employee, group);

		assertThat(member.getEmployee()).isSameAs(employee);
		assertThat(member.getGroup()).isSameAs(group);
		assertThat(member.getId().getEmployeeId()).isEqualTo(1L);
		assertThat(member.getId().getGroupId()).isEqualTo(2L);
	}

	@Test
	void onCreate_setsJoinedAt_whenNull() {
		var member = new Member();
		member.setJoinedAt(null);

		member.onCreate();

		assertThat(member.getJoinedAt()).isNotNull();
	}

	@Test
	void onCreate_doesNotChangeWhenSet() {
		var member = new Member();
		var t = LocalDateTime.now().minusDays(5);
		member.setJoinedAt(t);

		member.onCreate();

		assertThat(member.getJoinedAt()).isEqualTo(t);
	}

	@Test
	void testBuilderMethods() {
		final var employee = new Employee();
		final var group = new UserGroup();
		final var joinedAt = LocalDateTime.now();

		final var member = Member.create()
			.withEmployee(employee)
			.withGroup(group)
			.withJoinedAt(joinedAt);

		assertThat(member.getEmployee()).isSameAs(employee);
		assertThat(member.getGroup()).isSameAs(group);
		assertThat(member.getJoinedAt()).isEqualTo(joinedAt);
	}
}
