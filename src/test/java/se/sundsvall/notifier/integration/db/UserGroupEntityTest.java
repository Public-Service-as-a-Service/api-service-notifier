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
import se.sundsvall.notifier.integration.db.entity.UserGroup;

public class UserGroupEntityTest {

	@BeforeAll
	static void setUp() {
		BeanMatchers.registerValueGenerator(
			() -> LocalDateTime.now().minusDays(1),
			LocalDateTime.class);
	}

	@Test
	void testBean() {
		org.hamcrest.MatcherAssert.assertThat(UserGroup.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanEqualsFor("id"),
			hasValidBeanHashCodeFor("id"),
			hasValidBeanToStringExcluding("members")));
	}

	@Test
	void testBuilderMethods() {
		final var name = "name";
		final var description = "description";
		final var creator = new Employee();

		var group = UserGroup.create()
			.withName(name)
			.withDescription(description)
			.withCreator(creator);

		assertThat(group.getName()).isEqualTo(name);
		assertThat(group.getDescription()).isEqualTo(description);
		assertThat(group.getCreator()).isEqualTo(creator);
	}

	@Test
	void addEmployee_null() {
		var group = new UserGroup();

		group.addEmployee(null);

		assertThat(group.getMembers()).isEmpty();
	}

	@Test
	void addEmployee_addedTwice() {
		var group = new UserGroup();

		var employee = new Employee();

		group.addEmployee(employee);
		group.addEmployee(employee);

		assertThat(group.getMembers()).hasSize(1);
	}

	@Test
	void removeEmployee_null() {
		var group = new UserGroup();
		group.addEmployee(new Employee());

		group.removeEmployee(null);

		assertThat(group.getMembers()).hasSize(1);
	}

	@Test
	void removeEmployee_notInGroup() {
		var group = new UserGroup();
		var employee = new Employee();
		var otherEmployee = new Employee();

		group.addEmployee(otherEmployee);
		group.removeEmployee(employee);

		assertThat(group.getMembers()).hasSize(1);
	}

	@Test
	void removeEmployee_removesEmployee() {
		var group = new UserGroup();
		var employee = new Employee();

		group.addEmployee(employee);
		group.removeEmployee(employee);

		assertThat(group.getMembers()).isEmpty();
	}

	@Test
	void onCreate_setsCreatedAt_setsUpdatedAt_whenNull() {
		var group = new UserGroup();
		group.setCreatedAt(null);
		group.setUpdatedAt(null);

		group.onCreate();

		assertThat(group.getCreatedAt()).isNotNull();
		assertThat(group.getUpdatedAt()).isNotNull();
	}

	@Test
	void onUpdate_updatesUpdatedAt() {
		var group = new UserGroup();
		var oldUpdated = LocalDateTime.now().minusDays(1);
		group.setUpdatedAt(oldUpdated);

		group.onUpdate();

		assertThat(group.getUpdatedAt()).isAfter(oldUpdated);
	}

}
