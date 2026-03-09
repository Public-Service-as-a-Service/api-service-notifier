package se.sundsvall.notifier.service.mapper;

import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.sundsvall.notifier.api.model.request.MessageRequest;
import se.sundsvall.notifier.api.model.request.MessageType;
import se.sundsvall.notifier.integration.db.entity.Employee;
import se.sundsvall.notifier.integration.db.entity.Message;
import se.sundsvall.notifier.integration.db.entity.MessageRecipient;
import se.sundsvall.notifier.integration.db.entity.Organization;

import static org.assertj.core.api.Assertions.assertThat;

class MessageMapperTest {
	private MessageMapper messageMapper;

	@BeforeEach
	void setUp() {
		messageMapper = new MessageMapper();
	}

	@Test
	void toEntityTest() {

		var messageRequest = new MessageRequest("title",
			"content",
			"sender",
			Set.of(1L, 2L, 3L),
			MessageType.SMS);
		var result = messageMapper.toEntity(messageRequest);

		assertThat(result).isNotNull();
		assertThat(result.getTitle()).isEqualTo("title");
		assertThat(result.getSender()).isEqualTo("sender");
		assertThat(result.getContent()).isEqualTo("content");
		assertThat(result.getRecipients()).isNotNull();
	}

	@Test
	void toMessageResponseTest() {
		var createdAt = LocalDateTime.now();
		var organization = new Organization();
		organization.setName("IT Department");

		var employee = new Employee();
		employee.setId(1L);
		employee.setFirstName("John");
		employee.setLastName("Doe");
		employee.setOrganization(organization);

		var messageRecipient = MessageRecipient.builder()
			.withEmployee(employee)
			.withWorkTitle("Developer")
			.withOrgId("123")
			.withDeliveryStatus(MessageRecipient.DeliveryStatus.DELIVERED)
			.build();

		var message = Message.builder()
			.withId(1L)
			.withTitle("title")
			.withContent("content")
			.withSender("sender")
			.withRecipients(Set.of(messageRecipient))
			.withCreatedAt(createdAt)
			.build();

		var result = messageMapper.entityToMessageResponse(message);

		assertThat(result).isNotNull();
		assertThat(result.recipients()).hasSize(1);
		assertThat(result.id()).isEqualTo(1L);
		assertThat(result.title()).isEqualTo("title");
		assertThat(result.content()).isEqualTo("content");
		assertThat(result.sender()).isEqualTo("sender");
		assertThat(result.createdAt()).isEqualTo(createdAt);

		var recipientDto = result.recipients().iterator().next();

		assertThat(recipientDto.employeeId()).isEqualTo(1L);
		assertThat(recipientDto.workTitle()).isEqualTo("Developer");
		assertThat(recipientDto.orgId()).isEqualTo("123");
		assertThat(recipientDto.deliveryStatus()).isEqualTo("DELIVERED");
	}

	@Test
	void toSendTeamsDtoTest() {
		var email = "epost";
		var message = "message";

		var result = messageMapper.toSendTeamsDto(message, email);

		assertThat(result).isNotNull();
		assertThat(result.message()).isEqualTo(message);
		assertThat(result.recipient()).isEqualTo(email);
	}

	@Test
	void toSendSmsDtoTest() {
		var message = "message";
		var phoneNumber = "+467023235";
		var result = messageMapper.toSendSmsDto(message, phoneNumber);
		assertThat(result).isNotNull();
		assertThat(result.message()).isEqualTo(message);
		assertThat(result.mobileNumber()).isEqualTo(phoneNumber);
	}

	@Test
	void toMessageRecipientTest() {
		var employee = new Employee();
		var status = MessageRecipient.DeliveryStatus.DELIVERED;

		var result = messageMapper.toMessageRecipient(employee, status);
		assertThat(result).isNotNull();
		assertThat(result.getEmployee()).isEqualTo(employee);
		assertThat(result.getDeliveryStatus()).isEqualTo(status);
	}
}
