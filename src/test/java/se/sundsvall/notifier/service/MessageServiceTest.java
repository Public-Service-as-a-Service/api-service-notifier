package se.sundsvall.notifier.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.notifier.api.model.request.MessageRequest;
import se.sundsvall.notifier.api.model.response.MessageResponse;
import se.sundsvall.notifier.integration.db.entity.Employee;
import se.sundsvall.notifier.integration.db.entity.Message;
import se.sundsvall.notifier.integration.db.repository.EmployeeRepository;
import se.sundsvall.notifier.integration.db.repository.MessageRepository;
import se.sundsvall.notifier.integration.smssender.SmsSenderIntegration;
import se.sundsvall.notifier.service.mapper.MessageMapper;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

	@Mock
	private MessageRepository messageRepository;
	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private MessageMapper messageMapper;

	@Mock
	private SmsSenderIntegration smsSender;

	@InjectMocks
	private MessageService messageService;

	@Test
	void postMessagesTest() {
		var recipients = Set.of(1L, 2L);
		var messageRequest = new MessageRequest(
			"title",
			"content",
			"sender",
			null,
			recipients,
			true,
			true);

		var message = Message.builder().withId(1L).build();

		var employee1 = new Employee();
		employee1.setId(1L);
		var employee2 = new Employee();
		employee2.setId(2L);

		var employees = List.of(employee1, employee2);

		when(messageMapper.toEntity(messageRequest)).thenReturn(message);
		when(messageRepository.save(any(Message.class))).thenReturn(message);
		when(employeeRepository.findAllById(recipients)).thenReturn(employees);

		messageService.createMessage(messageRequest);

		verify(messageMapper).toEntity(messageRequest);
		verify(messageRepository, times(2)).save(any(Message.class));
		verify(employeeRepository).findAllById(recipients);
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
}
