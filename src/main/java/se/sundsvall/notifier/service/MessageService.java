package se.sundsvall.notifier.service;

import java.util.List;
import org.springframework.stereotype.Service;
import se.sundsvall.notifier.api.model.request.MessageRequest;
import se.sundsvall.notifier.api.model.request.Priority;
import se.sundsvall.notifier.api.model.response.MessageResponse;
import se.sundsvall.notifier.integration.db.entity.Employee;
import se.sundsvall.notifier.integration.db.entity.MessageRecipient;
import se.sundsvall.notifier.integration.db.entity.MessageRecipientId;
import se.sundsvall.notifier.integration.db.repository.EmployeeRepository;
import se.sundsvall.notifier.integration.db.repository.MessageRepository;
import se.sundsvall.notifier.integration.smssender.MessageStatus;
import se.sundsvall.notifier.integration.smssender.SmsDto;
import se.sundsvall.notifier.integration.smssender.SmsSenderIntegration;
import se.sundsvall.notifier.integration.teamssender.TeamsSenderDTO;
import se.sundsvall.notifier.integration.teamssender.TeamsSenderIntegration;
import se.sundsvall.notifier.service.mapper.MessageMapper;

@Service
public class MessageService {

	private final MessageRepository messageRepository;
	private final EmployeeRepository employeeRepository;
	private final MessageMapper messageMapper;
	private final SmsSenderIntegration smsSenderIntegration;
	private final TeamsSenderIntegration teamsSenderIntegration;

	public MessageService(MessageRepository messageRepository,
		EmployeeRepository employeeRepository,
		MessageMapper messageMapper, SmsSenderIntegration smsSenderIntegration,
		TeamsSenderIntegration teamsSenderIntegration) {
		this.messageRepository = messageRepository;
		this.employeeRepository = employeeRepository;
		this.messageMapper = messageMapper;
		this.smsSenderIntegration = smsSenderIntegration;
		this.teamsSenderIntegration = teamsSenderIntegration;
	}

	public void createMessage(MessageRequest messageRequest) {
		MessageStatus smsStatus = MessageStatus.NOT_SENT;
		// Inte implementerad då status för denna är osäker
		Long userGroup = null;
		if (messageRequest.groupId() != null) {

		}

		// sparar meddelandes så att embeddedId kan skapas
		var savedMessage = messageRepository.save(messageMapper.toEntity(messageRequest));

		var employees = employeeRepository.findAllById(messageRequest.recipientEmployeeIds());
		if (employees.size() != messageRequest.recipientEmployeeIds().size()) {
			System.out.println("Could not find all recipients");
		}
		for (Employee employee : employees) {

			// Skickar Teams meddelande
			if (messageRequest.sendTeams() && employee.getEmail() != null) {
				TeamsSenderDTO teamsDto = TeamsSenderDTO.builder()
					.withMessage(messageRequest.content())
					.withRecipient(employee.getEmail())
					.build();
				teamsSenderIntegration.sendTeamsMessage("2218", teamsDto);
			}

			// Skickar sms meddelande
			if (messageRequest.sendSms() && employee.getWorkMobile() != null) {
				SmsDto sms = SmsDto.builder()
					.withSender(messageRequest.sender())
					.withMessage(savedMessage.getContent())
					.withMobileNumber(employee.getWorkMobile())
					.withPriority(Priority.HIGH)
					.build();
				smsStatus = smsSenderIntegration.sendSms("2281", sms);
			}

			// skapar embedded id för recipient
			MessageRecipientId recipientId = new MessageRecipientId();
			recipientId.setMessageId(savedMessage.getId());
			recipientId.setEmployeeId(employee.getId());

			MessageRecipient messageRecipient = MessageRecipient.builder()
				.withEmployee(employee)
				.withDeliveryStatus(smsStatus == MessageStatus.SENT ? MessageRecipient.DeliveryStatus.DELIVERED : MessageRecipient.DeliveryStatus.FAILED)
				.build();

			savedMessage.addRecipient(messageRecipient);
		}
		messageRepository.save(savedMessage);
	}

	public List<MessageResponse> getMessages(String sender) {
		return messageRepository.findAllBySender(sender)
			.stream()
			.map(messageMapper::entityToMessageResponse)
			.toList();
	}

	public void deleteMessages(Long id) {
		messageRepository.deleteById(id);
	}
}
