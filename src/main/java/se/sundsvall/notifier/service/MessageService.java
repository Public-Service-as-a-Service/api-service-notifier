package se.sundsvall.notifier.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.notifier.api.model.request.MessageRequest;
import se.sundsvall.notifier.api.model.request.MessageType;
import se.sundsvall.notifier.api.model.response.MessageResponse;
import se.sundsvall.notifier.integration.db.entity.Employee;
import se.sundsvall.notifier.integration.db.entity.MessageRecipient;
import se.sundsvall.notifier.integration.db.repository.EmployeeRepository;
import se.sundsvall.notifier.integration.db.repository.MessageRepository;
import se.sundsvall.notifier.integration.smssender.MessageStatus;
import se.sundsvall.notifier.integration.smssender.SmsSenderIntegration;
import se.sundsvall.notifier.integration.teamssender.TeamsSenderIntegration;
import se.sundsvall.notifier.service.mapper.MessageMapper;
import se.sundsvall.notifier.service.utility.PhoneNumberUtil;

@Service
public class MessageService {

	private final MessageRepository messageRepository;
	private final EmployeeRepository employeeRepository;
	private final MessageMapper messageMapper;
	private final SmsSenderIntegration smsSenderIntegration;
	private final TeamsSenderIntegration teamsSenderIntegration;
	private final PhoneNumberUtil phoneNumberUtil;

	public MessageService(MessageRepository messageRepository,
		EmployeeRepository employeeRepository,
		MessageMapper messageMapper, SmsSenderIntegration smsSenderIntegration,
		TeamsSenderIntegration teamsSenderIntegration, PhoneNumberUtil phoneNumberUtil) {
		this.messageRepository = messageRepository;
		this.employeeRepository = employeeRepository;
		this.messageMapper = messageMapper;
		this.smsSenderIntegration = smsSenderIntegration;
		this.teamsSenderIntegration = teamsSenderIntegration;
		this.phoneNumberUtil = phoneNumberUtil;
	}

	@Transactional
	public void createMessage(MessageRequest messageRequest) {

		var savedMessage = messageRepository.save(messageMapper.toEntity(messageRequest));
		var employees = employeeRepository.findAllById(messageRequest.recipientEmployeeIds());

		for (Employee employee : employees) {

			var delivered = sendMessageToEmployee(employee, messageRequest);
			MessageRecipient messageRecipient = messageMapper.toMessageRecipient(employee, delivered);

			savedMessage.addRecipient(messageRecipient);
		}
		messageRepository.save(savedMessage);
	}

	public MessageResponse getMessageById(String sender, Long messageId) {
		var message = messageRepository.findBySenderAndId(sender, messageId)
			.orElseThrow(() -> Problem.valueOf(Status.NOT_FOUND, "Message with id: " + messageId + " not found"));
		return messageMapper.entityToMessageResponse(message);
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

	public MessageRecipient.DeliveryStatus sendMessageToEmployee(Employee employee, MessageRequest messageRequest) {
		boolean teamsSuccess = false;
		MessageStatus smsSuccess = MessageStatus.NOT_SENT;
		boolean isTeamsMessage = messageRequest.messageType() == MessageType.TEAMS || messageRequest.messageType() == MessageType.TEAMS_AND_SMS;
		boolean isSmsMessage = messageRequest.messageType() == MessageType.SMS || messageRequest.messageType() == MessageType.TEAMS_AND_SMS;

		if (isTeamsMessage && employee.getEmail() != null) {
			teamsSuccess = teamsSenderIntegration.sendTeamsMessage("2281",
				messageMapper.toSendTeamsDto(messageRequest.content(), employee.getEmail()));
		}

		String phoneNumber = phoneNumberUtil.cleanPhoneNumber(employee.getWorkMobile());
		if (isSmsMessage && phoneNumber != null) {
			smsSuccess = smsSenderIntegration.sendSms("2281",
				messageMapper.toSendSmsDto(messageRequest.content(), phoneNumber));
		}
		return (teamsSuccess || smsSuccess == MessageStatus.SENT)
			? MessageRecipient.DeliveryStatus.DELIVERED
			: MessageRecipient.DeliveryStatus.FAILED;
	}
}
