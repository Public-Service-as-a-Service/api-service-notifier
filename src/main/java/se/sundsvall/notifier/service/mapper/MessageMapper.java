package se.sundsvall.notifier.service.mapper;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import se.sundsvall.notifier.api.model.request.MessageRequest;
import se.sundsvall.notifier.api.model.request.Priority;
import se.sundsvall.notifier.api.model.response.MessageRecipientResponse;
import se.sundsvall.notifier.api.model.response.MessageResponse;
import se.sundsvall.notifier.integration.db.entity.Employee;
import se.sundsvall.notifier.integration.db.entity.Message;
import se.sundsvall.notifier.integration.db.entity.MessageRecipient;
import se.sundsvall.notifier.integration.smssender.SmsDto;
import se.sundsvall.notifier.integration.teamssender.TeamsSenderDTO;

@Component
public class MessageMapper {

	public Message toEntity(MessageRequest messageRequest) {
		return Message.builder()
			.withTitle(messageRequest.title())
			.withContent(messageRequest.content())
			.withSender(messageRequest.sender())
			.withMessageType(messageRequest.messageType())
			.build();
	}

	public MessageResponse entityToMessageResponse(Message message) {
		if (message == null) {
			return null;
		}
		var recipients = message.getRecipients().stream()
			.map(recipient -> MessageRecipientResponse.builder()
				.withEmployeeId(recipient.getEmployee().getId())
				.withFirstName(recipient.getEmployee().getFirstName())
				.withLastName(recipient.getEmployee().getLastName())
				.withWorkTitle(recipient.getWorkTitle())
				.withOrgId(recipient.getOrgId())
				.withOrgName(recipient.getEmployee().getOrganization().getName())
				.withDeliveryStatus(recipient.getDeliveryStatus().toString())
				.build()).collect(Collectors.toSet());

		return MessageResponse.builder()
			.withId(message.getId())
			.withTitle(message.getTitle())
			.withContent(message.getContent())
			.withSender(message.getSender())
			.withCreatedAt(message.getCreatedAt())
			.withRecipients(recipients)
			.build();

	}

	public SmsDto toSendSmsDto(String content, String mobileNumber) {

		return SmsDto.builder()
			.withMessage(content)
			.withMobileNumber(mobileNumber)
			.withPriority(Priority.HIGH)
			.build();
	}

	public TeamsSenderDTO toSendTeamsDto(String messageContent, String email) {
		return TeamsSenderDTO.builder()
			.withMessage(messageContent)
			.withRecipient(email)
			.build();
	}

	public MessageRecipient toMessageRecipient(Employee employee, MessageRecipient.DeliveryStatus deliveryStatus) {
		return MessageRecipient.builder()
			.withEmployee(employee)
			.withDeliveryStatus(deliveryStatus)
			.build();
	}

}
