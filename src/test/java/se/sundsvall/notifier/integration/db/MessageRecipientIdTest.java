package se.sundsvall.notifier.integration.db;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.core.AllOf.allOf;

import org.junit.jupiter.api.Test;
import se.sundsvall.notifier.integration.db.entity.MessageRecipientId;

public class MessageRecipientIdTest {

	@Test
	void testBean() {
		org.hamcrest.MatcherAssert.assertThat(MessageRecipientId.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanEqualsFor("messageId", "employeeId"),
			hasValidBeanToString()));
	}
}
