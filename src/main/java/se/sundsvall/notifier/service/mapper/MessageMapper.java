package se.sundsvall.notifier.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import se.sundsvall.notifier.api.model.request.MessageRequest;
import se.sundsvall.notifier.api.model.request.Priority;
import se.sundsvall.notifier.api.model.response.MessageRecipientResponse;
import se.sundsvall.notifier.api.model.response.MessageResponse;
import se.sundsvall.notifier.integration.db.entity.Message;
import se.sundsvall.notifier.integration.db.entity.MessageRecipient;
import se.sundsvall.notifier.integration.smssender.SmsDto;

@Component
public class MessageMapper {

	public Message toEntity(MessageRequest messageRequest) {
		return Message.builder()
			.withTitle(messageRequest.title())
			.withContent(messageRequest.content())
			.withSender(messageRequest.sender())
			.build();
	}

	public MessageResponse entityToMessageResponse(Message message) {
		if (message == null) {
			return null;
		}

		return MessageResponse.builder()
			.withId(message.getId())
			.withTitle(message.getTitle())
			.withContent(message.getContent())
			.withSender(message.getSender())
			.withCreatedAt(message.getCreatedAt())
			.withRecipients(toMessageRecipientDtos(message.getRecipients()))
			.build();

	}

	private Set<MessageRecipientResponse> toMessageRecipientDtos(Set<MessageRecipient> messageRecipients) {
		if (messageRecipients == null || messageRecipients.isEmpty()) {
			return Set.of();
		}
		return messageRecipients.stream()
			.map(this::toMessageRecipientDto)
			.collect(Collectors.toSet());

	}

	private MessageRecipientResponse toMessageRecipientDto(MessageRecipient messageRecipient) {
		return MessageRecipientResponse.builder()
			.withEmployeeId(messageRecipient.getEmployee().getId())
			.withWorkTitle(messageRecipient.getWorkTitle())
			.withOrgId(messageRecipient.getOrgId())
			.withDeliveryStatus(messageRecipient.getDeliveryStatus().toString())
			.build();
	}

	public SmsDto toSendSmsDto(String content, String mobileNumber) {

		return SmsDto.builder()
			.withMessage(content)
			.withMobileNumber(mobileNumber)
			.withPriority(Priority.HIGH)
			.build();
	}

}
