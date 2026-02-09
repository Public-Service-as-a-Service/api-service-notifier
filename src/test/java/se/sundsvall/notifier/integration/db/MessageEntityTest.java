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
import se.sundsvall.notifier.integration.db.entity.Message;
import se.sundsvall.notifier.integration.db.entity.MessageRecipient;
import se.sundsvall.notifier.integration.db.entity.UserGroup;

public class MessageEntityTest {

	@BeforeAll
	static void setUp() {
		BeanMatchers.registerValueGenerator(
			() -> LocalDateTime.now().minusDays(1),
			LocalDateTime.class);
	}

	@Test
	void testBean() {
		org.hamcrest.MatcherAssert.assertThat(Message.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanEqualsFor("id"),
			hasValidBeanHashCodeFor("id"),
			hasValidBeanToStringExcluding("recipients", "group", "sender")));
	}

	@Test
	void testBuilderMethods() {

		final var title = "title";
		final var content = "content";
		final var sender = new Employee();
		final var group = new UserGroup();

		var message = Message.create()
			.withTitle(title)
			.withContent(content)
			.withSender(sender)
			.withGroup(group);

		assertThat(message.getTitle()).isEqualTo(title);
		assertThat(message.getContent()).isEqualTo(content);
		assertThat(message.getSender()).isEqualTo(sender);
		assertThat(message.getGroup()).isEqualTo(group);

	}

	@Test
	void onCreate_createdAt_null() {
		var message = new Message();
		message.setCreatedAt(null);

		message.onCreate();

		assertThat(message.getCreatedAt()).isNotNull();
	}

	@Test
	void onCreate_doesNotChangeWhenSet() {
		var message = new Message();
		var t = LocalDateTime.now().minusDays(5);
		message.setCreatedAt(t);

		message.onCreate();

		assertThat(message.getCreatedAt()).isEqualTo(t);
	}

	@Test
	void addRecipient_addsAndLinks() {
		var message = new Message();
		message.setRecipients(new HashSet<>());

		var recipient = new MessageRecipient();
		message.addRecipient(recipient);

		assertThat(message.getRecipients()).contains(recipient);
		assertThat(recipient.getMessage()).isSameAs(message);
	}

	@Test
	void addRecipient_null() {
		var message = new Message();
		message.setRecipients(new HashSet<>());

		message.addRecipient(null);

		assertThat(message.getRecipients()).isEmpty();
	}

	@Test
	void addRecipient_addedTwice() {
		var message = new Message();
		message.setRecipients(new HashSet<>());

		var recipient = new MessageRecipient();

		message.addRecipient(recipient);
		message.addRecipient(recipient);

		assertThat(message.getRecipients()).hasSize(1);
	}

	@Test
	void removeRecipient_removesAndUnlinks() {
		var message = new Message();
		message.setRecipients(new HashSet<>());

		var recipient = new MessageRecipient();
		message.addRecipient(recipient);

		message.removeRecipient(recipient);

		assertThat(message.getRecipients()).isEmpty();
		assertThat(recipient.getMessage()).isNull();
	}

	@Test
	void removeRecipient_null() {
		var message = new Message();
		message.setRecipients(new HashSet<>());
		message.addRecipient(new MessageRecipient());

		message.removeRecipient(null);

		assertThat(message.getRecipients()).hasSize(1);
	}
}
