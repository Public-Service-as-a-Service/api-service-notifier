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
import java.util.HashSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.sundsvall.notifier.integration.db.entity.Employee;
import se.sundsvall.notifier.integration.db.entity.Member;

class EmployeeEntityTest {

	@BeforeAll
	static void setUp() {
		BeanMatchers.registerValueGenerator(
			() -> LocalDateTime.now().minusDays(1),
			LocalDateTime.class);
	}

	@Test
	void testBean() {
		org.hamcrest.MatcherAssert.assertThat(Employee.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanEqualsFor("id"),
			hasValidBeanHashCodeFor("id"),
			hasValidBeanToStringExcluding("organization", "memberships")));
	}

	@Test
	void addMembership_addsLinkToBothEmployeeAndMember() {
		var employee = new Employee();
		employee.setMemberships(new HashSet<>());

		var member = new Member();
		employee.addMembership(member);

		assertThat(employee.getMemberships()).contains(member);
		assertThat(member.getEmployee()).isSameAs(employee);
	}

	@Test
	void addMembership_null() {
		var employee = new Employee();
		employee.setMemberships(new HashSet<>());

		employee.addMembership(null);

		assertThat(employee.getMemberships()).isEmpty();
	}

	@Test
	void addMembership_addedTwice() {
		var employee = new Employee();
		employee.setMemberships(new HashSet<>());

		var member = new Member();

		employee.addMembership(member);
		employee.addMembership(member);

		assertThat(employee.getMemberships()).containsExactly(member);
		assertThat(member.getEmployee()).isSameAs(employee);
	}

	@Test
	void removeMembership_removesMembershipAndLinkToEmployee() {
		var employee = new Employee();
		employee.setMemberships(new HashSet<>());

		var member = new Member();

		employee.addMembership(member);
		employee.removeMembership(member);

		assertThat(employee.getMemberships()).isEmpty();
		assertThat(member.getEmployee()).isNull();
	}

	@Test
	void removeMembership_null() {
		var employee = new Employee();
		employee.setMemberships(new HashSet<>());

		employee.removeMembership(null);

		assertThat(employee.getMemberships()).isEmpty();
	}
}
