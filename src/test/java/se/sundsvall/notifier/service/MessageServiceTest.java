package se.sundsvall.notifier.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.dept44.problem.Problem;
import se.sundsvall.notifier.api.model.request.MessageRequest;
import se.sundsvall.notifier.api.model.request.MessageType;
import se.sundsvall.notifier.api.model.response.MessageResponse;
import se.sundsvall.notifier.integration.db.entity.Employee;
import se.sundsvall.notifier.integration.db.entity.Message;
import se.sundsvall.notifier.integration.db.entity.MessageRecipient;
import se.sundsvall.notifier.integration.db.repository.EmployeeRepository;
import se.sundsvall.notifier.integration.db.repository.MessageRepository;
import se.sundsvall.notifier.integration.smssender.MessageStatus;
import se.sundsvall.notifier.integration.smssender.SmsSenderIntegration;
import se.sundsvall.notifier.integration.teamssender.TeamsSenderIntegration;
import se.sundsvall.notifier.service.mapper.MessageMapper;
import se.sundsvall.notifier.service.utility.PhoneNumberUtil;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

	@Mock
	private TeamsSenderIntegration teamsSenderIntegration;

	@Mock
	private SmsSenderIntegration smsSenderIntegration;
	@Mock
	private PhoneNumberUtil phoneNumberUtil;
	@Mock
	private MessageRepository messageRepository;

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private MessageMapper messageMapper;

	@InjectMocks
	private MessageService messageService;

	@Test
	void createMessagesTest() {
		var recipients = Set.of(1L, 2L);
		var messageRequest = new MessageRequest(
			"title",
			"content",
			"sender",
			recipients,
			MessageType.TEAMS_AND_SMS);
		MessageRecipient recipient = new MessageRecipient();

		var message = Message.builder().withId(1L).build();

		var employee1 = new Employee();
		employee1.setId(1L);
		var employee2 = new Employee();
		employee2.setId(2L);

		var employees = List.of(employee1, employee2);

		when(messageMapper.toEntity(messageRequest)).thenReturn(message);
		when(messageRepository.save(any(Message.class))).thenReturn(message);
		when(employeeRepository.findAllById(recipients)).thenReturn(employees);
		when(messageMapper.toMessageRecipient(any(), any())).thenReturn(recipient);
		messageService.createMessage(messageRequest);

		verify(messageMapper).toEntity(messageRequest);
		verify(messageRepository, times(2)).save(any(Message.class));
		verify(employeeRepository).findAllById(recipients);
	}

	@Test
	void getMessageByIdTest() {
		var messageId = 1L;
		var emaii = "test@sundsvall.se";
		var message = Message.builder().withId(messageId).withSender(emaii).build();
		var response = MessageResponse.builder().withId(messageId).withSender(emaii).build();

		when(messageRepository.findBySenderAndId(emaii, messageId)).thenReturn(Optional.of(message));
		when(messageMapper.entityToMessageResponse(message)).thenReturn(response);

		var result = messageService.getMessageById(emaii, messageId);
		assertThat(result).isEqualTo(response);
		verify(messageRepository).findBySenderAndId(emaii, messageId);
	}

	@Test
	void getMessagesByIdTest_NotFound() {
		var messageId = 1L;
		var email = "test";

		when(messageRepository.findBySenderAndId(any(), any())).thenReturn(Optional.empty());
		var exception = assertThrows(Throwable.class, () -> messageService.getMessageById(email, messageId));

		verify(messageRepository).findBySenderAndId(any(), any());
		Assertions.assertThat(exception)
			.isInstanceOf(Problem.class)
			.hasMessageContaining("Message with id: " + messageId + " not found");
		verifyNoInteractions(messageMapper);
	}

	@Test
	void getMessagesTest() {
		var sender = "sender";
		var message1 = Message.builder().withId(1L).build();
		var message2 = Message.builder().withId(2L).build();
		var messages = List.of(message1, message2);

		var response1 = MessageResponse.builder().withId(1L).build();
		var response2 = MessageResponse.builder().withId(2L).build();

		when(messageRepository.findAllBySender(sender)).thenReturn(messages);
		when(messageMapper.entityToMessageResponse(message1)).thenReturn(response1);
		when(messageMapper.entityToMessageResponse(message2)).thenReturn(response2);

		var result = messageService.getMessages(sender);

		assertThat(result).hasSize(2);
		assertThat(result).containsExactly(response1, response2);
		verify(messageRepository).findAllBySender(sender);
		verify(messageMapper, times(2)).entityToMessageResponse(any());
	}

	@Test
	void sendMessageTest() {
		Employee employee = new Employee();
		employee.setEmail("test@example.com");
		employee.setWorkMobile("+46701234567");

		var messageRequest = MessageRequest.builder()
			.withContent("content")
			.withSender("sender")
			.withMessageType(MessageType.TEAMS_AND_SMS)
			.build();

		when(phoneNumberUtil.cleanPhoneNumber(anyString()))
			.thenReturn("+46701234567");
		when(teamsSenderIntegration.sendTeamsMessage(anyString(), any()))
			.thenReturn(true);
		when(smsSenderIntegration.sendSms(anyString(), any()))
			.thenReturn(MessageStatus.SENT);

		var result = messageService.sendMessageToEmployee(employee, messageRequest);

		assertThat(result).isEqualTo(MessageRecipient.DeliveryStatus.DELIVERED);
		verify(smsSenderIntegration).sendSms(anyString(), any());
		verify(phoneNumberUtil).cleanPhoneNumber(anyString());
		verify(teamsSenderIntegration).sendTeamsMessage(anyString(), any());
	}

}
