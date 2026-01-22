package se.sundsvall.notifier.integration.smssender;

import generated.se.sundsvall.smssender.SendSmsRequest;
import generated.se.sundsvall.smssender.SendSmsRequest.PriorityEnum;
import generated.se.sundsvall.smssender.Sender;
import org.springframework.stereotype.Component;

@Component
public class SmsSenderIntegrationMapper {

	public SendSmsRequest toSendSmsRequest(final SmsDto dto) {
		if (dto == null) {
			return null;
		}

		return new SendSmsRequest()
			.sender(new Sender().name(dto.sender()))
			.mobileNumber(dto.mobileNumber())
			.message(dto.message())
			.priority(PriorityEnum.HIGH);
	}
}
